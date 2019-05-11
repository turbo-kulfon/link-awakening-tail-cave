package com.la.game_objects.link.draw;

import com.engine.direction.Direction;
import com.engine.direction.IDirection;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.la.game_objects.enemy.RecoilIndicatorController;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;

public class LinkDrawComponent {
	public enum LinkDrawState {
		IDLE,
		WALK,
		SHIELD_DOWN_IDLE,
		SHIELD_UP_IDLE,
		SHIELD_DOWN_WALK,
		SHIELD_UP_WALK,
		PUSH,
		PULL,
		JUMP,
		SWORD_ATTACK,
		ITEM_HOLD,
		FALL,
		BIG_ITEM_GET,
		POWER_UP_ITEM_GET,
		DEAD
	}

	private TextureDrawComponent drawComponent;
	private RecoilIndicatorController recoilIndicatorController;

	private LinkDrawState drawState  = LinkDrawState.IDLE;
	private Direction direction;

	private int frameCounter;
	private float offsetY = 8;
	private boolean hit;

	public LinkDrawComponent(GFXSystem gfxSystem) {
		drawComponent = gfxSystem.createTextureDrawComponent(1);
		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return hit;
			}
		});
	}

	public void switchToTopDownMode() {
		offsetY = 8;
	}
	public void switchToPlatformMode() {
		offsetY = 1;
	}

	public void setPosition(float x, float y) {
		drawComponent.setPosition(x, y);
	}
	public void setVisible(boolean visible) {
		drawComponent.setVisible(visible);
	}
	public void setDrawState(LinkDrawState state) {
		drawState = state;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	public void setFrameCounter(int counter) {
		frameCounter = counter;
	}
	public void setHeight(float height) {
		drawComponent.setHeight(height);
	}
	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public void update() {
		recoilIndicatorController.update();
	}
	public void draw() {
		recoilIndicatorController.draw();
		if(drawState == LinkDrawState.IDLE) {
			if(direction == Direction.LEFT) {
				idleLeft();
			}
			else if(direction == Direction.RIGHT) {
				idleRight();
			}
			else if(direction == Direction.UP) {
				idleUp();
			}
			else if(direction == Direction.DOWN) {
				idleDown();
			}
		}
		else if(drawState == LinkDrawState.WALK) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					idleLeft();
				}
				else {
					walkLeft();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					idleRight();
				}
				else {
					walkRight();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					idleUp();
				}
				else {
					walkUp();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					idleDown();
				}
				else {
					walkDown();
				}
			}
		}
		if(drawState == LinkDrawState.SHIELD_DOWN_IDLE) {
			if(direction == Direction.LEFT) {
				idleShieldDownLeft();
			}
			else if(direction == Direction.RIGHT) {
				idleShieldDownRight();
			}
			else if(direction == Direction.UP) {
				idleShieldDownUp();
			}
			else if(direction == Direction.DOWN) {
				idleShieldDownDown();
			}
		}
		if(drawState == LinkDrawState.SHIELD_DOWN_WALK) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					idleShieldDownLeft();
				}
				else {
					walkShieldDownLeft();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					idleShieldDownRight();
				}
				else {
					walkShieldDownRight();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					idleShieldDownUp();
				}
				else {
					walkShieldDownUp();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					idleShieldDownDown();
				}
				else {
					walkShieldDownDown();
				}
			}
		}
		if(drawState == LinkDrawState.SHIELD_UP_IDLE) {
			if(direction == Direction.LEFT) {
				idleShieldUpLeft();
			}
			else if(direction == Direction.RIGHT) {
				idleShieldUpRight();
			}
			else if(direction == Direction.UP) {
				idleShieldUpUp();
			}
			else if(direction == Direction.DOWN) {
				idleShieldUpDown();
			}
		}
		if(drawState == LinkDrawState.SHIELD_UP_WALK) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					idleShieldUpLeft();
				}
				else {
					walkShieldUpLeft();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					idleShieldUpRight();
				}
				else {
					walkShieldUpRight();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					idleShieldUpUp();
				}
				else {
					walkShieldUpUp();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					idleShieldUpDown();
				}
				else {
					walkShieldUpDown();
				}
			}
		}
		else if(drawState == LinkDrawState.PUSH) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					pushLeft1();
				}
				else {
					pushLeft2();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					pushRight1();
				}
				else {
					pushRight2();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					pushUp1();
				}
				else {
					pushUp2();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					pushDown1();
				}
				else {
					pushDown2();
				}
			}
		}
		else if(drawState == LinkDrawState.JUMP) {
			int lol1 = 30, lol2 = 23, lol3 = 16,lol4 = 9;
			int n = lol1 - (frameCounter % lol1);
			if(direction == Direction.LEFT) {
				if(lol1 > n && n > lol2) {
					jumpLeft1();
				}
				else if(lol2 >= n && n > lol3) {
					jumpLeft2();
				}
				else if(lol3 >= n && n > lol4) {
					jumpLeft3();
				}
				else if(n <= lol4) {
					walkLeft();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(lol1 > n && n > lol2) {
					jumpRight1();
				}
				else if(lol2 >= n && n > lol3) {
					jumpRight2();
				}
				else if(lol3 >= n && n > lol4) {
					jumpRight3();
				}
				else if(n <= lol4) {
					walkRight();
				}
			}
			else if(direction == Direction.UP) {
				if(lol1 > n && n > lol2) {
					jumpUp1();
				}
				else if(lol2 >= n && n > lol3) {
					jumpUp2();
				}
				else if(lol3 >= n && n > lol4) {
					jumpUp3();
				}
				else if(n <= lol4) {
					walkUp();
				}
			}
			else if(direction == Direction.DOWN) {
				if(lol1 > n && n > lol2) {
					jumpDown1();
				}
				else if(lol2 >= n && n > lol3) {
					jumpDown2();
				}
				else if(lol3 >= n && n > lol4) {
					jumpDown3();
				}
				else if(n <= lol4) {
					walkDown();
				}
			}
		}
		else if(drawState == LinkDrawState.ITEM_HOLD) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					itemHoldLeft1();
				}
				else {
					itemHoldLeft2();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					itemHoldRight1();
				}
				else {
					itemHoldRight2();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					itemHoldUp1();
				}
				else {
					itemHoldUp2();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					itemHoldDown1();
				}
				else {
					itemHoldDown2();
				}
			}
		}
		else if(drawState == LinkDrawState.SWORD_ATTACK) {
			if(direction == Direction.LEFT) {
				if(12 >= frameCounter && frameCounter > 8) {
					swordCutLeft1();
				}
				else if(8 >= frameCounter && frameCounter > 4) {
					swordCutLeft2();
				}
				else if(4 >= frameCounter) {
					swordCutLeft3();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(12 >= frameCounter && frameCounter > 8) {
					swordCutRight1();
				}
				else if(8 >= frameCounter && frameCounter > 4) {
					swordCutRight2();
				}
				else if(4 >= frameCounter) {
					swordCutRight3();
				}
			}
			else if(direction == Direction.UP) {
				if(12 >= frameCounter && frameCounter > 8) {
					swordCutUp1();
				}
				else if(8 >= frameCounter && frameCounter > 4) {
					swordCutUp2();
				}
				else if(4 >= frameCounter) {
					swordCutUp3();
				}
			}
			else if(direction == Direction.DOWN) {
				if(12 >= frameCounter && frameCounter > 8) {
					swordCutDown1();
				}
				else if(8 >= frameCounter && frameCounter > 4) {
					swordCutDown2();
				}
				else if(4 >= frameCounter) {
					swordCutDown3();
				}
			}
		}
		else if(drawState == LinkDrawState.FALL) {
			if(60 > frameCounter && frameCounter > 40) {
				fall1();
			}
			else if(40 >= frameCounter && frameCounter > 20) {
				fall2();
			}
			else if(20 >= frameCounter && frameCounter >= 0) {
				fall3();
			}
		}
		else if(drawState == LinkDrawState.PULL) {
			if(direction == Direction.LEFT) {
				if(frameCounter % 15 <= 7) {
					pullLeft1();
				}
				else {
					pullLeft2();
				}
			}
			else if(direction == Direction.RIGHT) {
				if(frameCounter % 15 <= 7) {
					pullRight1();
				}
				else {
					pullRight2();
				}
			}
			else if(direction == Direction.UP) {
				if(frameCounter % 15 <= 7) {
					pushUp1();
				}
				else {
					pullUp();
				}
			}
			else if(direction == Direction.DOWN) {
				if(frameCounter % 15 <= 7) {
					pushDown1();
				}
				else {
					pullDown();
				}
			}
		}
		else if(drawState == LinkDrawState.POWER_UP_ITEM_GET) {
			drawComponent.setTexture(0, 177, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setSpriteOffsetX(-3);
			drawComponent.setSpriteOffsetY(-offsetY);
		}
		else if(drawState == LinkDrawState.BIG_ITEM_GET) {
			drawComponent.setTexture(16, 177, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setSpriteOffsetX(-3);
			drawComponent.setSpriteOffsetY(-offsetY);
		}
		else if(drawState == LinkDrawState.DEAD) {
			drawComponent.setTexture(32, 177, 14, 16);
			drawComponent.setSize(14, 16);
			drawComponent.setSpriteOffsetX(-2);
			drawComponent.setSpriteOffsetY(-offsetY);
		}
	}

	public void turnRight() {
		direction = IDirection.getRightDirection(direction);
	}
	public void setLayer(int layer) {
		drawComponent.setLayer(layer);
	}

	private void idleLeft() {
		drawComponent.setTexture(0, 0, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleRight() {
		drawComponent.setTexture(90, 0, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleUp() {
		drawComponent.setTexture(53, 0, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleDown() {
		drawComponent.setTexture(27, 0, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-1.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void walkLeft() {
		drawComponent.setTexture(14, 1, 13, 15);
		drawComponent.setSize(13, 15);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY+1);
	}
	private void walkRight() {
		drawComponent.setTexture(77, 1, 13, 15);
		drawComponent.setSize(13, 15);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY+1);
	}
	private void walkUp() {
		drawComponent.setTexture(65, 1, 12, 15);
		drawComponent.setSize(12, 15);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY+1);
	}
	private void walkDown() {
		drawComponent.setTexture(40, 0, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-0.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void idleShieldDownLeft() {
		idleLeft();
	}
	private void idleShieldDownRight() {
		drawComponent.setTexture(13, 47, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-2);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleShieldDownUp() {
		drawComponent.setTexture(57, 47, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleShieldDownDown() {
		drawComponent.setTexture(28, 47, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void walkShieldDownLeft() {
		walkLeft();
	}
	private void walkShieldDownRight() {
		drawComponent.setTexture(0, 48, 13, 15);
		drawComponent.setSize(13, 15);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY+1);
	}
	private void walkShieldDownUp() {
		drawComponent.setTexture(71, 47, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void walkShieldDownDown() {
		drawComponent.setTexture(42, 47, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-2.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void idleShieldUpLeft() {
		drawComponent.setTexture(0, 79, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-3.0f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleShieldUpRight() {
		drawComponent.setTexture(0, 63, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-0f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleShieldUpUp() {
		drawComponent.setTexture(56, 63, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void idleShieldUpDown() {
		drawComponent.setTexture(29, 63, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-1.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void walkShieldUpLeft() {
		drawComponent.setTexture(14, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-3);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void walkShieldUpRight() {
		drawComponent.setTexture(14, 63, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void walkShieldUpUp() {
		drawComponent.setTexture(70, 63, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void walkShieldUpDown() {
		drawComponent.setTexture(42, 63, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void jumpLeft1() {
		drawComponent.setTexture(0, 128, 16, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpLeft2() {
		drawComponent.setTexture(16, 128, 14, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpLeft3() {
		drawComponent.setTexture(30, 128, 16, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}

	private void jumpRight1() {
		drawComponent.setTexture(0, 111, 16, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpRight2() {
		drawComponent.setTexture(16, 111, 14, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpRight3() {
		drawComponent.setTexture(30, 111, 16, 17);
		drawComponent.setSize(16, 17);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}

	private void jumpUp1() {
		drawComponent.setTexture(46, 128, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpUp2() {
		drawComponent.setTexture(58, 128, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpUp3() {
		drawComponent.setTexture(70, 128, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}

	private void jumpDown1() {
		drawComponent.setTexture(46, 111, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpDown2() {
		drawComponent.setTexture(58, 111, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}
	private void jumpDown3() {
		drawComponent.setTexture(70, 111, 12, 17);
		drawComponent.setSize(12, 17);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY-1);
	}

	private void swordCutLeft1() {
		drawComponent.setTexture(29, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutLeft2() {
		drawComponent.setTexture(44, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-4f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutLeft3() {
		drawComponent.setTexture(44, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-6f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void swordCutRight1() {
		drawComponent.setTexture(59, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutRight2() {
		drawComponent.setTexture(74, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutRight3() {
		drawComponent.setTexture(74, 79, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void swordCutUp1() {
		drawComponent.setTexture(27, 95, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-0f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutUp2() {
		drawComponent.setTexture(41, 95, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-0f);
		drawComponent.setSpriteOffsetY(-offsetY - 1);
	}
	private void swordCutUp3() {
		drawComponent.setTexture(41, 95, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-0f);
		drawComponent.setSpriteOffsetY(-offsetY - 3);
	}

	private void swordCutDown1() {
		drawComponent.setTexture(0, 95, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void swordCutDown2() {
		drawComponent.setTexture(13, 95, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY + 1);
	}
	private void swordCutDown3() {
		drawComponent.setTexture(13, 95, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY + 3);
	}

	private void pushLeft1() {
		drawComponent.setTexture(0, 16, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-4f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushLeft2() {
		drawComponent.setTexture(15, 16, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-4f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushRight1() {
		drawComponent.setTexture(0, 32, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushRight2() {
		drawComponent.setTexture(86, 16, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushUp1() {
		drawComponent.setTexture(56, 16, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushUp2() {
		drawComponent.setTexture(72, 16, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushDown1() {
		drawComponent.setTexture(30, 16, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-1.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pushDown2() {
		drawComponent.setTexture(44, 16, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-0.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void fall1() {
		drawComponent.setTexture(64, 161, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY + 3);
	}
	private void fall2() {
		drawComponent.setTexture(79, 169, 8, 8);
		drawComponent.setSize(8, 8);
		drawComponent.setSpriteOffsetX(0f);
		drawComponent.setSpriteOffsetY(-offsetY + 7);
	}
	private void fall3() {
		drawComponent.setTexture(79, 165, 4, 4);
		drawComponent.setSize(4, 4);
		drawComponent.setSpriteOffsetX(2f);
		drawComponent.setSpriteOffsetY(0);
	}

	private void pullLeft1() {
		drawComponent.setTexture(68, 95, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pullLeft2() {
		drawComponent.setTexture(48, 161, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffsetX(3f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pullRight1() {
		drawComponent.setTexture(54, 95, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pullRight2() {
		drawComponent.setTexture(0, 161, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffsetX(-9f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void pullUp() {
		drawComponent.setTexture(32, 161, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffsetX(-3f);
		drawComponent.setSpriteOffsetY(-offsetY+4);
	}
	private void pullDown() {
		drawComponent.setTexture(16, 161, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffsetX(-2.5f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	private void itemHoldLeft1() {
		drawComponent.setTexture(0, 145, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldLeft2() {
		drawComponent.setTexture(14, 145, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldRight1() {
		drawComponent.setTexture(75, 145, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldRight2() {
		drawComponent.setTexture(89, 145, 13, 16);
		drawComponent.setSize(13, 16);
		drawComponent.setSpriteOffsetX(-2f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldUp1() {
		drawComponent.setTexture(51, 145, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldUp2() {
		drawComponent.setTexture(63, 145, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldDown1() {
		drawComponent.setTexture(27, 145, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}
	private void itemHoldDown2() {
		drawComponent.setTexture(39, 145, 12, 16);
		drawComponent.setSize(12, 16);
		drawComponent.setSpriteOffsetX(-1f);
		drawComponent.setSpriteOffsetY(-offsetY);
	}

	public void remove() {
		drawComponent.remove();
	}
}
