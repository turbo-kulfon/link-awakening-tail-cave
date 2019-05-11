package com.la.game_states;

import com.engine.IInputPort;
import com.engine.ISystemPort;
import com.engine.aspect.AspectSystem;
import com.engine.aspect.IAspectSystem;
import com.engine.double_buffer.DoubleBuffer;
import com.engine.double_buffer.IDoubleBuffer;
import com.engine.event_system.EventSystem;
import com.engine.event_system.IEventSystem;
import com.engine.game_object.GameObjectSystem;
import com.engine.game_object.IGameObject;
import com.engine.game_object.IGameObjectSystem;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.core.DrawLayerStandard;
import com.engine.gfx.core.DrawLayerZSort;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.id_manager.UniqueIDManager;
import com.engine.observer.IObserver;
import com.engine.observer.Observatory;
import com.engine.sound.ISoundPort;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileMapSystem;
import com.engine.util.IRNG;
import com.engine.util.RNG;
import com.la.equipment.EquipmentSystem;
import com.la.equipment.IEquipmentSystem;
import com.la.equipment.StatusPanel;
import com.la.factory.EnemyFactory;
import com.la.factory.EnemyFactory.EnemyFactoryDependency;
import com.la.factory.EventFactory;
import com.la.factory.GameStateFactory;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IGameStateFactory;
import com.la.factory.RoomFactory;
import com.la.game_objects.link.ILinkData;
import com.la.game_objects.link.controller.LnkExample;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.MapDisplaySystem;
import com.la.observer.LinkFallObserverData;
import com.la.power_up_item_system.PowerUpItemSystem;
import com.la.power_up_item_system.PowerUpItemSystem.PowerUpHitCallback;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoomCreator;
import com.la.room.IRoomMap;
import com.la.room.RoomCreator;
import com.la.room.RoomMap;
import com.la.room.rooms.BatTown;
import com.la.room.rooms.BladeTrapRoom;
import com.la.room.rooms.CenterRoom;
import com.la.room.rooms.CompassRoom;
import com.la.room.rooms.DungeonBossEntrance;
import com.la.room.rooms.DungeonBossRoom;
import com.la.room.rooms.EntranceRoom;
import com.la.room.rooms.FeatherPath1;
import com.la.room.rooms.FeatherPath2;
import com.la.room.rooms.FeatherRoom;
import com.la.room.rooms.FloorButtonRoom;
import com.la.room.rooms.FullMoonCelloInstrumentRoom;
import com.la.room.rooms.KeyBridge;
import com.la.room.rooms.KeyLockRoom;
import com.la.room.rooms.MapRoom;
import com.la.room.rooms.MiniBossEntranceRoom;
import com.la.room.rooms.MiniBossRoom;
import com.la.room.rooms.NightmareKeyRoom;
import com.la.room.rooms.NorthRoom1;
import com.la.room.rooms.NorthRoom2;
import com.la.room.rooms.NorthRoom3;
import com.la.room.rooms.OneWayExitRoom;
import com.la.room.rooms.SecretRoom;
import com.la.room.rooms.SecretRoomEntrance;
import com.la.room.rooms.ThreeOfAKindRoom;
import com.la.room.rooms.Underground1;
import com.la.room.rooms.Underground2;
import com.la.room.rooms.Underground3;
import com.la.room.rooms.Underground4;
import com.la.room.teleport_sys.TeleportSystem;
import com.la.room.transition.HoleTeleportSystem;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IRoomTransition;
import com.la.room.transition.MapLocation;
import com.la.room.transition.RoomTransition;

public class MainGameState implements IGameState {
	private IInputPort inputPort;
	private ISoundPort soundPort;
	private ISystemPort systemPort;

	private IGameStateSystem gameStateSystem;
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private SpatialSystem spatialSystem;
	private IEventSystem eventSystem;

	private StatusPanel statusPanel;
	private IEquipmentSystem equipmentSystem;
	private IMapDisplaySystem mapDisplaySystem;
	private PowerUpItemSystem powerUpItemSystem;

	private IGameObjectSystem gameObjectSystem;
	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private IOutsideViewCheck outsideViewCheck;
	private ILinkData linkData;

	private RoomFactory roomFactory;
	private IGameStateFactory gameStateFactory;

	public MainGameState(
			GFXSystem gfxSystem,
			ISystemPort systemPort,
			IInputPort inputPort,
			ISoundPort soundPort,
			IGameStateSystem gameStateSystem) {
		this.inputPort = inputPort;
		this.soundPort = soundPort;
		this.systemPort = systemPort;
		this.gameStateSystem = gameStateSystem;
		uniqueIDManager = new UniqueIDManager();
		this.gfxSystem = gfxSystem;
		gfxSystem.addLayer(new DrawLayerStandard());
		gfxSystem.addLayer(new DrawLayerZSort());
		gfxSystem.addLayer(new DrawLayerStandard());
		gfxSystem.addLayer(new DrawLayerStandard());
		gfxSystem.addLayer(new DrawLayerStandard());
		gfxSystem.addLayer(new DrawLayerStandard());
		soundSystem = new SoundSystem(soundPort);
		spatialSystem = new SpatialSystem();
		aspectSystem = new AspectSystem();
		gameObjectSystem = new GameObjectSystem();
		eventSystem = new EventSystem();
		powerUpItemSystem = new PowerUpItemSystem();
		equipmentSystem = new EquipmentSystem(powerUpItemSystem, ()-> {
//			gameStateFactory.createGameOverGameState();
		});
	}

	@Override
	public void onCreate() {
		powerUpItemSystem.setCallback(new PowerUpHitCallback() {
			@Override
			public void removeGuardianAcorn() {
				equipmentSystem.removeGuardianAcorn();
			}
			@Override
			public void removePieceOfPower() {
				equipmentSystem.removePieceOfPower();
			}
			@Override
			public void onNoPowerUpActive() {
				soundSystem.setPowerUp(false);
				soundSystem.playDungeonMusic();
			}
		});
		RespawnSystem respawnSystem = new RespawnSystem();
		TileMapSystem tileMapSystem = new TileMapSystem();
		Observatory<LinkFallObserverData> linkFallObserver = new Observatory<>();
		IRNG rng = new RNG();

//		equipmentSystem.addGreifenfeder();
		equipmentSystem.addShield();
		equipmentSystem.addSword();
//		equipmentSystem.addKey(0);
//		equipmentSystem.addKey(0);
//		equipmentSystem.addKey(0);
//		equipmentSystem.addNightmareKey(0);
//		equipmentSystem.addBombs(99);
		equipmentSystem.addMagicPowder(23);
//		equipmentSystem.addDungeonMap(0);
//		equipmentSystem.addCompass(0);
		mapDisplaySystem = new MapDisplaySystem(gfxSystem, soundSystem, equipmentSystem);

		statusPanel = new StatusPanel(gfxSystem, soundSystem, equipmentSystem, ()-> {
			gameStateFactory.createGameOverGameState();
		});

		int startRoomID = 0;
		IDoubleBuffer<IGameObject> roomObjects = new DoubleBuffer<>();
//		Link linkGameObject = new Link(60, 44, inputPort, gfxSystem, spatialSystem, uniqueIDManager, aspectSystem, equipmentSystem, new ActionButtonControl(equipmentSystem), linkFallObserver, roomFactory);
//		linkData = linkGameObject;
		roomFactory = new RoomFactory(gameObjectSystem, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, equipmentSystem, tileMapSystem, roomObjects, powerUpItemSystem, rng);
		LnkExample linkGameObject = new LnkExample(70, 100, inputPort, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, equipmentSystem, roomFactory, linkFallObserver);
		linkData = linkGameObject;
		IRoomCreator roomCreator = new RoomCreator(roomFactory);
		IEnemyFactory enemyFactory = new EnemyFactory(gameObjectSystem, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, roomFactory, linkGameObject, roomObjects, tileMapSystem, powerUpItemSystem, rng, new EnemyFactoryDependency() {
			@Override
			public void drawText(String text) {
				gameStateFactory.createTextState(text);
			}
		});
		IEventFactory eventFactory = new EventFactory(soundSystem, eventSystem, aspectSystem, roomFactory, linkGameObject);
		IRoomMap roomMap = new RoomMap();
		IMapLocation mapLocation = new MapLocation(startRoomID);
		gameStateFactory = new GameStateFactory(gameStateSystem, gfxSystem, soundSystem, soundPort, inputPort, systemPort, linkGameObject, aspectSystem, equipmentSystem, rng, roomFactory);

		IRoomTransition roomTransition = new RoomTransition(soundSystem, roomMap, roomObjects, mapLocation, gameStateFactory, eventSystem, mapDisplaySystem, tileMapSystem, respawnSystem, linkGameObject);
		HoleTeleportSystem holeTeleportSystem = new HoleTeleportSystem(roomTransition);
		roomFactory.setHoleTeleportSystem(holeTeleportSystem, linkData, gameStateFactory);
		linkFallObserver.addObserver(new IObserver<LinkFallObserverData>() {
			@Override
			public void update(LinkFallObserverData data) {
				holeTeleportSystem.teleport();
			}
			@Override
			public boolean shouldRemove() {
				return false;
			}
		});
		gameObjectSystem.add(linkGameObject);
		outsideViewCheck = new OutsideViewCheck(
			new OutsideViewCheck.Position() {
				@Override
				public float getX() {
					return linkGameObject.getX();
				}
				@Override
				public float getY() {
					return linkGameObject.getY();
				}
				@Override
				public float getW() {
					return linkGameObject.getW();
				}
				@Override
				public float getH() {
					return linkGameObject.getH();
				}
			}, 
			new OutsideViewCheckCallback() {
				@Override
				public void outsideLeft() {
					roomTransition.goLeft();
				}
				@Override
				public void outsideRight() {
					roomTransition.goRight();
				}
				@Override
				public void outsideUp() {
					roomTransition.goUp();
				}
				@Override
				public void outsideDown() {
					roomTransition.goDown();
				}
			}
		);
		TeleportSystem teleportSystem = new TeleportSystem(soundSystem, roomFactory, roomTransition, linkGameObject, gameStateFactory, 0, 15);

		roomMap.addRoom(new EntranceRoom(roomCreator, roomFactory, roomTransition, mapLocation, teleportSystem, mapDisplaySystem), 0);
		roomMap.addRoom(new KeyBridge(roomCreator, roomFactory, eventFactory, enemyFactory, mapLocation, mapDisplaySystem, soundSystem, respawnSystem), 1);
		roomMap.addRoom(new FloorButtonRoom(soundSystem, roomCreator, roomFactory, enemyFactory, gameStateFactory, mapLocation, mapDisplaySystem, respawnSystem), 2);
		roomMap.addRoom(new CompassRoom(roomCreator, roomFactory, eventFactory, enemyFactory, gameStateFactory, mapLocation, mapDisplaySystem, respawnSystem), 3);
		roomMap.addRoom(new MapRoom(roomCreator, roomFactory, enemyFactory, eventFactory, mapLocation, mapDisplaySystem, respawnSystem, gameStateFactory), 4);
		roomMap.addRoom(new OneWayExitRoom(roomCreator, roomFactory, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 5);
		roomMap.addRoom(new BatTown(roomCreator, roomFactory, eventFactory, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 6);
		roomMap.addRoom(new SecretRoomEntrance(roomCreator, roomFactory, enemyFactory, eventFactory, mapLocation, mapDisplaySystem, gameStateFactory, respawnSystem), 7);
		roomMap.addRoom(new CenterRoom(roomCreator, roomFactory, enemyFactory, mapLocation, mapDisplaySystem, gameStateFactory, respawnSystem), 8);
		roomMap.addRoom(new BladeTrapRoom(roomCreator, roomFactory, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 9);
		roomMap.addRoom(new NorthRoom1(soundSystem, roomCreator, roomFactory, enemyFactory, eventFactory, mapLocation, mapDisplaySystem), 10);
		roomMap.addRoom(new NorthRoom2(roomCreator, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 11);
		roomMap.addRoom(new NorthRoom3(roomCreator, roomFactory, enemyFactory, eventFactory, roomTransition, mapLocation, mapDisplaySystem, respawnSystem), 12);
		roomMap.addRoom(new MiniBossEntranceRoom(roomCreator, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 13);
		roomMap.addRoom(new ThreeOfAKindRoom(soundSystem, roomCreator, roomFactory, enemyFactory, mapLocation, gameStateFactory, mapDisplaySystem, respawnSystem), 14);
		roomMap.addRoom(new MiniBossRoom(soundSystem, roomCreator, roomFactory, enemyFactory, eventFactory, teleportSystem, mapLocation, mapDisplaySystem), 15);
		roomMap.addRoom(new DungeonBossEntrance(roomCreator, roomFactory, enemyFactory, roomTransition, mapLocation, mapDisplaySystem), 16);
		roomMap.addRoom(new DungeonBossRoom(soundSystem, roomCreator, roomFactory, enemyFactory, eventFactory, mapLocation, mapDisplaySystem), 17);
		roomMap.addRoom(new FullMoonCelloInstrumentRoom(roomCreator, roomFactory, eventFactory, mapLocation, mapDisplaySystem), 18);
		roomMap.addRoom(new NightmareKeyRoom(roomCreator, roomFactory, mapLocation, gameStateFactory, mapDisplaySystem, respawnSystem), 19);
		roomMap.addRoom(new KeyLockRoom(roomCreator, roomFactory, enemyFactory, mapLocation, mapDisplaySystem, respawnSystem), 20);
		roomMap.addRoom(new FeatherPath1(roomCreator, roomFactory, roomTransition, mapLocation, mapDisplaySystem), 21);
		roomMap.addRoom(new FeatherPath2(roomCreator, roomFactory, mapLocation, mapDisplaySystem, respawnSystem), 22);
		roomMap.addRoom(new FeatherRoom(roomCreator, roomFactory, enemyFactory, mapLocation, gameStateFactory, mapDisplaySystem, respawnSystem), 23);
		roomMap.addRoom(new SecretRoom(roomCreator, roomFactory, mapLocation, mapDisplaySystem), 24);

		roomMap.addRoom(new Underground1(roomCreator, enemyFactory, mapLocation, respawnSystem), 50);
		roomMap.addRoom(new Underground2(roomCreator, enemyFactory, mapLocation, respawnSystem), 51);
		roomMap.addRoom(new Underground3(roomCreator, mapLocation), 52);
		roomMap.addRoom(new Underground4(roomCreator, mapLocation), 53);
		roomMap.createRoom(startRoomID, 0, 0);
		mapDisplaySystem.setLinkMapPosition(3, 7);

		soundSystem.playDungeonMusic();
	}
	@Override
	public void onRemove() {
		gfxSystem.dispose();
		spatialSystem.dispose();
		aspectSystem.dispose();
	}

	@Override
	public void onPause() {
	}
	@Override
	public void onResume() {
	}

	@Override
	public void pauseUpdate() {
	}
	boolean blk = false;
	@Override
	public void update() {
		if(inputPort.isSelectButtonPressed() == true) {
			if(blk == false) {
//				roomFactory.createFireballDefeated(50, 50);
//				roomFactory.createEnemyDefeatedSmallSpark(16, 16, ()->{});
//				roomFactory.createPowerItemAcquire(50, 50, ()->{});
//				roomFactory.createMagicPowderSprinkle(16, 16);
				blk = true;
				soundSystem.menuOpen();
			}
//			linkData.restoreLastPosition();
			gameStateSystem.pushState(new EquipmentMenuState(gfxSystem, soundSystem, gameStateSystem, statusPanel, equipmentSystem, inputPort, mapDisplaySystem));
		}
		else {
			blk = false;
		}
//		if(inputPort.isStartButtonPressed() == true) {
//			if(blk == false) {
//				gameStateSystem.clear();
//				gameStateFactory.createMainGameState();
//				blk = true;
//			}
//		}
//		else {
//			blk = false;
//		}
		gameObjectSystem.update();

		spatialSystem.update();
		eventSystem.update();
		outsideViewCheck.update();
		statusPanel.update();
	}

	@Override
	public void pauseDraw() {
		localDraw();
	}
	@Override
	public void draw() {
		localDraw();
	}

	private void localDraw() {
		gameObjectSystem.clean();
		statusPanel.draw();
		gameObjectSystem.draw();
	}
}
