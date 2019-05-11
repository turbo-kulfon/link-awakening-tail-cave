package com.engine.component.sword;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.direction.Direction;
import com.engine.direction.IDirection;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;

public class SwordComponent implements ISwordComponent {
	public interface SwordComponentDependency {
		float getX();
		float getY();
		float getCenterX();
		float getCenterY();
		Direction getOwnerDirection();
		boolean recoil(float enemyCX, float enemyCY);

		void onUpdate(SwordState swordState, int counter, Direction spinAttakcDirection);

		boolean isPieceOfPowerActive();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private SoundSystem soundSystem;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	private SwordComponentDependency dependency;

	private int uniqueID;

	private SwordState state = SwordState.NONE;
	private boolean buttonPressed, lastButton, buttonBlock;
	private int counter, blinkCounter;
	private Direction spinAttackDirection;
	private boolean clockwiseSpinAttack, diagonalSpin, cutHitHide;

	public SwordComponent(
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			SoundSystem soundSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.soundSystem = soundSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCollisionResponse((collidedID)-> {
			SwordHit swordHit = aspectSystem.getAspect(collidedID, AspectType.SWORD_HIT);
			if(swordHit != null) {
				SwordHitResult result = swordHit.hit(
					spatialComponent.getX(), spatialComponent.getY(),
					spatialComponent.getW(), spatialComponent.getH(),
					dependency.getCenterX(), dependency.getCenterY(), dependency.getOwnerDirection(),
					getDmg(), dependency.isPieceOfPowerActive(), state, counter);
				if(result.type == SwordHitResultType.DEFLECT) {
					if(dependency.recoil(result.enemyCX, result.enemyCY) == true) {
						soundSystem.deflect();
						roomFactory.createSpark(spatialComponent.getCenterX(), spatialComponent.getCenterY());
					}
				}
				else if(result.type == SwordHitResultType.HIT) {
					if(state == SwordState.HOLD || state == SwordState.POWER_UP) {
						state = SwordState.THRUST_KEY_BLOCK;
						counter = 15;
					}
					else if(state == SwordState.CUT) {
						cutHitHide = true;
					}
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, true, spatialComponent));
	}

	@Override
	public void attack() {
		buttonPressed = true;
	}
	@Override
	public void thrust() {
		if(state == SwordState.HOLD || state == SwordState.POWER_UP) {
			state = SwordState.THRUST_NO_KEY_BLOCK;
			counter = 15;
		}
	}

	public void setDependency(SwordComponentDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public void update() {
		if(state == SwordState.NONE) {
			if(buttonPressed == false) {
				buttonBlock = false;
			}
			if(buttonPressed == true && buttonBlock == false) {
				initializeComponents();
				state = SwordState.CUT;
				soundSystem.playSwordSlash();
				counter = 12;				
			}
		}
		else if(state == SwordState.CUT) {
			counter -= 1;
			if(counter <= 0) {
				if(cutHitHide == false) {
					if(buttonPressed == true) {
						state = SwordState.HOLD;
						counter = 30;
					}
					else {
						state = SwordState.NONE;
						removeComponents();
					}
				}
				else {
					state = SwordState.NONE;
					removeComponents();
					cutHitHide = false;
					buttonBlock = true;
				}
			}
			else {
				if(buttonPressed == true) {
					if(lastButton == false) {
						state = SwordState.CUT;
						soundSystem.playSwordSlash();
						counter = 12;						
					}
				}
			}
		}
		else if(state == SwordState.HOLD) {
			counter -= 1;
			if(counter <= 0) {
				if(buttonPressed == true) {
					state = SwordState.POWER_UP;
					soundSystem.swordPoweredUp();
				}
				else {
					state = SwordState.NONE;
					removeComponents();
				}
			}
			else {
				if(buttonPressed == false) {
					state = SwordState.NONE;
					removeComponents();
				}
			}
		}
		else if(state == SwordState.POWER_UP) {
			if(buttonPressed == false) {
				state = SwordState.SPIN;
				spinAttackDirection = dependency.getOwnerDirection();
				if(spinAttackDirection == Direction.RIGHT) {
					clockwiseSpinAttack = true;
				}
				else {
					clockwiseSpinAttack = false;
				}
				soundSystem.swordSpin();
				counter = 24;
			}
			else {
				blinkCounter += 1;
				if(blinkCounter > 8) {
					blinkCounter = 0;
				}
			}
		}
		else if(state == SwordState.THRUST_KEY_BLOCK) {
			counter -= 1;
			if(counter <= 0) {
				state = SwordState.NONE;
				removeComponents();
			}
			buttonBlock = true;
		}
		else if(state == SwordState.THRUST_NO_KEY_BLOCK) {
			counter -= 1;
			if(counter <= 0) {
				if(buttonPressed == false) {
					state = SwordState.NONE;
					removeComponents();
				}
				else {
					state = SwordState.HOLD;
					counter = 30;
				}
			}
		}
		else if(state == SwordState.SPIN) {
			counter -= 1;
			buttonBlock = true;
			if(24 >= counter && counter > 21) {
				spinAttackDirection = dependency.getOwnerDirection();
				diagonalSpin = true;
			}
			else if(21 >= counter && counter > 18) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
				}
				diagonalSpin = false;
			}
			else if(18 >= counter && counter > 15) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());				
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
				}
				diagonalSpin = true;
			}
			else if(15 >= counter && counter > 12) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);			
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
				}
				diagonalSpin = false;
			}
			else if(12 >= counter && counter > 9) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);			
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
				}
				diagonalSpin = true;
			}
			else if(9 >= counter && counter > 6) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);			
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
				}
				diagonalSpin = false;
			}
			else if(6 >= counter && counter > 3) {
				if(clockwiseSpinAttack == false) {
					spinAttackDirection = IDirection.getLeftDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);
					spinAttackDirection = IDirection.getLeftDirection(spinAttackDirection);				
				}
				else {
					spinAttackDirection = IDirection.getRightDirection(dependency.getOwnerDirection());
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
					spinAttackDirection = IDirection.getRightDirection(spinAttackDirection);
				}
				diagonalSpin = true;
			}
			else if(3 >= counter) {
				spinAttackDirection = dependency.getOwnerDirection();
				diagonalSpin = false;
			}
			if(counter <= 0) {
				state = SwordState.NONE;
				removeComponents();
			}
		}

		setSwordPosition();
		lastButton = buttonPressed;
		buttonPressed = false;

		dependency.onUpdate(state, counter, spinAttackDirection);
	}

	@Override
	public void stop() {
		removeComponents();
		state = SwordState.NONE;
		cutHitHide = false;
		blinkCounter = 0;
	}

	@Override
	public void draw() {
		setSwordPosition();
		drawComponent.setSize(spatialComponent.getW(), spatialComponent.getH());
		drawComponent.setInvert(false, 1, 0.69f, 0.19f);
		if(state == SwordState.CUT) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				swordCutLeft();
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				swordCutRight();
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				swordCutUp();
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				swordCutDown();
			}
		}
		else if(state == SwordState.HOLD) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				swordHoldLeft();
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				swordHoldRight();
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				swordHoldUp();
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				swordHoldDown();
			}
		}
		else if(state == SwordState.THRUST_KEY_BLOCK || state == SwordState.THRUST_NO_KEY_BLOCK) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				swordHoldLeft();
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				swordHoldRight();
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				swordHoldUp();
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				swordHoldDown();
			}
		}
		else if(state == SwordState.POWER_UP) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				swordHoldLeft();
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				swordHoldRight();
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				swordHoldUp();
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				swordHoldDown();
			}
			if(blinkCounter > 4) {
				drawComponent.setInvert(true, 1, 0.69f, 0.19f);
			}
			else {
				drawComponent.setInvert(false, 1, 0.69f, 0.19f);
			}
		}
		else if(state == SwordState.SPIN) {
			spinAttack();
		}
	}

	@Override
	public boolean allowMove() {
		return state == SwordState.NONE || state == SwordState.HOLD || state == SwordState.POWER_UP;
	}
	@Override
	public boolean allowChangeDirection() {
		return state == SwordState.NONE;
	}

	@Override
	public float getSwordCenterX() {
		return spatialComponent.getCenterX();
	}
	@Override
	public float getSwordCenterY() {
		return spatialComponent.getCenterY();
	}
	@Override
	public SwordState getSwordState() {
		return state;
	}

	@Override
	public void remove() {
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}

	private void setSwordPosition() {
		if(state == SwordState.CUT) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				if(12 >= counter && counter > 8) {
					spatialComponent.setX(dependency.getX() - 2);
					spatialComponent.setY(dependency.getY() - 16 - 8);
					spatialComponent.setSize(6, 16);
				}
				else if(8 >= counter && counter > 4) {
					spatialComponent.setX(dependency.getX() - 15);
					spatialComponent.setY(dependency.getY() - 14 - 8);
					spatialComponent.setSize(15, 15);
				}
				else if(4 >= counter) {
					spatialComponent.setX(dependency.getX() - 21);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				if(12 >= counter && counter > 8) {
					spatialComponent.setX(dependency.getX() + 7);
					spatialComponent.setY(dependency.getY() - 16 - 8);
					spatialComponent.setSize(6, 16);
				}
				else if(8 >= counter && counter > 4) {
					spatialComponent.setX(dependency.getX() + 10);
					spatialComponent.setY(dependency.getY() - 14 - 8);
					spatialComponent.setSize(15, 15);
				}
				else if(4 >= counter) {
					spatialComponent.setX(dependency.getX() + 10 + 5);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				if(12 >= counter && counter > 8) {
					spatialComponent.setX(dependency.getX() + 10);
					spatialComponent.setY(dependency.getY() - 9 - 0);
					spatialComponent.setSize(16, 6);
				}
				else if(8 >= counter && counter > 4) {
					spatialComponent.setX(dependency.getX() + 9);
					spatialComponent.setY(dependency.getY() - 15 - 8);
					spatialComponent.setSize(15, 15);
				}
				else if(4 >= counter) {
					spatialComponent.setX(dependency.getX() - 3);
					spatialComponent.setY(dependency.getY() - 19 - 8);
					spatialComponent.setSize(6, 16);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				if(12 >= counter && counter > 8) {
					spatialComponent.setX(dependency.getX() - 16);
					spatialComponent.setY(dependency.getY() - 1 - 0);
					spatialComponent.setSize(16, 6);
				}
				else if(8 >= counter && counter > 4) {
					spatialComponent.setX(dependency.getX() - 0 - 15);
					spatialComponent.setY(dependency.getY() + 3);
					spatialComponent.setSize(15, 15);
				}
				else if(4 >= counter) {
					spatialComponent.setX(dependency.getX() + 5);
					spatialComponent.setY(dependency.getY() + 17 - 8);
					spatialComponent.setSize(6, 16);
				}
			}
		}
		else if(state == SwordState.HOLD || state == SwordState.POWER_UP) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				spatialComponent.setX(dependency.getX() - 15);
				spatialComponent.setY(dependency.getY() - 0 - 0);
				spatialComponent.setSize(16, 6);
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				spatialComponent.setX(dependency.getX() + 8);
				spatialComponent.setY(dependency.getY() - 0 - 0);
				spatialComponent.setSize(16, 6);
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				spatialComponent.setX(dependency.getX() - 3);
				spatialComponent.setY(dependency.getY() - 21);
				spatialComponent.setSize(6, 16);
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				spatialComponent.setX(dependency.getX() + 5);
				spatialComponent.setY(dependency.getY() + 6);
				spatialComponent.setSize(6, 16);
			}
		}
		else if(state == SwordState.THRUST_KEY_BLOCK || state == SwordState.THRUST_NO_KEY_BLOCK) {
			if(dependency.getOwnerDirection() == Direction.LEFT) {
				if(counter >= 7) {
					spatialComponent.setX(dependency.getX() - 21);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
				else {
					spatialComponent.setX(dependency.getX() - 15);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.RIGHT) {
				if(counter >= 7) {
					spatialComponent.setX(dependency.getX() + 10 + 5);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
				else {
					spatialComponent.setX(dependency.getX() + 8);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.UP) {
				if(counter >= 7) {
					spatialComponent.setX(dependency.getX() - 3);
					spatialComponent.setY(dependency.getY() - 19 - 8);
					spatialComponent.setSize(6, 16);
				}
				else {
					spatialComponent.setX(dependency.getX() - 3);
					spatialComponent.setY(dependency.getY() - 21);
					spatialComponent.setSize(6, 16);
				}
			}
			else if(dependency.getOwnerDirection() == Direction.DOWN) {
				if(counter >= 7) {
					spatialComponent.setX(dependency.getX() + 5);
					spatialComponent.setY(dependency.getY() + 17 - 8);
					spatialComponent.setSize(6, 16);
				}
				else {
					spatialComponent.setX(dependency.getX() + 5);
					spatialComponent.setY(dependency.getY() + 6);
					spatialComponent.setSize(6, 16);
				}
			}
		}
		else if(state == SwordState.SPIN) {
			if(spinAttackDirection == Direction.LEFT) {
				if(diagonalSpin == false) {				
					spatialComponent.setX(dependency.getX() - 21);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
				else {
					if(clockwiseSpinAttack == false) {
						spatialComponent.setX(dependency.getX() - 0 - 15);
						spatialComponent.setY(dependency.getY() + 3);
						spatialComponent.setSize(15, 15);
					}
					else {
						spatialComponent.setX(dependency.getX() - 15);
						spatialComponent.setY(dependency.getY() - 14 - 8);
						spatialComponent.setSize(15, 15);
					}
				}
			}
			else if(spinAttackDirection == Direction.RIGHT) {
				if(diagonalSpin == false) {
					spatialComponent.setX(dependency.getX() + 10 + 5);
					spatialComponent.setY(dependency.getY() - 0 - 0);
					spatialComponent.setSize(16, 6);
				}
				else {
					if(clockwiseSpinAttack == false) {
						spatialComponent.setX(dependency.getX() + 10);
						spatialComponent.setY(dependency.getY() - 14 - 8);
						spatialComponent.setSize(15, 15);
					}
					else {
						spatialComponent.setX(dependency.getX() - 0 + 15);
						spatialComponent.setY(dependency.getY() + 3);
						spatialComponent.setSize(15, 15);
					}
				}
			}
			else if(spinAttackDirection == Direction.UP) {
				if(diagonalSpin == false) {
					spatialComponent.setX(dependency.getX() - 3);
					spatialComponent.setY(dependency.getY() - 19 - 8);
					spatialComponent.setSize(6, 16);							
				}
				else {
					if(clockwiseSpinAttack == false) {
						spatialComponent.setX(dependency.getX() - 15);
						spatialComponent.setY(dependency.getY() - 14 - 8);
						spatialComponent.setSize(15, 15);
					}
					else {
						spatialComponent.setX(dependency.getX() + 10);
						spatialComponent.setY(dependency.getY() - 14 - 8);
						spatialComponent.setSize(15, 15);
					}
				}
			}
			else if(spinAttackDirection == Direction.DOWN) {
				if(diagonalSpin == false) {
					spatialComponent.setX(dependency.getX() + 5);
					spatialComponent.setY(dependency.getY() + 17 - 8);	
					spatialComponent.setSize(6, 16);			
				}
				else {
					if(clockwiseSpinAttack == false) {
						spatialComponent.setX(dependency.getX() - 0 + 15);
						spatialComponent.setY(dependency.getY() + 3);
						spatialComponent.setSize(15, 15);
					}
					else {
						spatialComponent.setX(dependency.getX() - 0 - 15);
						spatialComponent.setY(dependency.getY() + 3);
						spatialComponent.setSize(15, 15);
					}
				}
			}
		}
	}
	private void initializeComponents() {
		drawComponent.setVisible(true);
		spatialComponent.setActive(true);
		
	}
	private void removeComponents() {
		drawComponent.setVisible(false);
		spatialComponent.setActive(false);
		blinkCounter = 0;
	}

	private void swordCutLeft() {
		if(12 >= counter && counter > 8) {
			drawComponent.setTexture(85, 32, 6, 16);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(8 >= counter && counter > 4) {
			drawComponent.setTexture(45, 32, 15, 15);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(4 >= counter) {
			drawComponent.setTexture(60, 32, 16, 6);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
	}
	private void swordHoldLeft() {
		drawComponent.setTexture(60, 32, 16, 6);
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}
	private void swordCutRight() {
		if(12 >= counter && counter > 8) {
			drawComponent.setTexture(85, 32, 6, 16);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(8 >= counter && counter > 4) {
			drawComponent.setTexture(15, 32, 15, 15);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(4 >= counter) {
			drawComponent.setTexture(60, 38, 16, 6);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
	}
	private void swordHoldRight() {
		drawComponent.setTexture(60, 38, 16, 6);
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}
	private void swordCutUp() {
		if(12 >= counter && counter > 8) {
			drawComponent.setTexture(60, 38, 16, 6);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(8 >= counter && counter > 4) {
			drawComponent.setTexture(15, 32, 15, 15);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(4 >= counter) {
			drawComponent.setTexture(85, 32, 6, 16);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
	}
	private void swordHoldUp() {
		drawComponent.setTexture(85, 32, 6, 16);
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}
	private void swordCutDown() {
		if(12 >= counter && counter > 8) {
			drawComponent.setTexture(60, 32, 16, 6);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(8 >= counter && counter > 4) {
			drawComponent.setTexture(30, 32, 15, 15);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(4 >= counter) {
			drawComponent.setTexture(91, 32, 6, 16);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
	}
	private void swordHoldDown() {
		drawComponent.setTexture(91, 32, 6, 16);
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}
	private void spinAttack() {
		if(spinAttackDirection == Direction.LEFT) {
			if(diagonalSpin == false) {			
				drawComponent.setTexture(60, 32, 16, 6);
				drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			}
			else {
				if(clockwiseSpinAttack == false) {
					drawComponent.setTexture(30, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
				else {
					drawComponent.setTexture(45, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
			}
		}
		else if(spinAttackDirection == Direction.RIGHT) {
			if(diagonalSpin == false) {
				drawComponent.setTexture(60, 38, 16, 6);
				drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());		
			}
			else {
				if(clockwiseSpinAttack == false) {
					drawComponent.setTexture(15, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
				else {
					drawComponent.setTexture(85, 48, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
			}
		}
		else if(spinAttackDirection == Direction.UP) {
			if(diagonalSpin == false) {
				drawComponent.setTexture(85, 32, 6, 16);
				drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			}
			else {
				if(clockwiseSpinAttack == false) {
					drawComponent.setTexture(45, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
				else {
					drawComponent.setTexture(15, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
			}
		}
		else if(spinAttackDirection == Direction.DOWN) {
			if(diagonalSpin == false) {
				drawComponent.setTexture(91, 32, 6, 16);
				drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());			
			}
			else {
				if(clockwiseSpinAttack == false) {
					drawComponent.setTexture(85, 48, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
				else {
					drawComponent.setTexture(30, 32, 15, 15);
					drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
				}
			}
		}
	}

	private int getDmg() {
		int result = 0;
//		if(state == SwordState.HOLD || state == SwordState.POWER_UP) {
//			return 1;
//		}
		if(state == SwordState.SPIN) {
			result = 4;
		}
		else {
			result = 2;
		}
		if(dependency.isPieceOfPowerActive() == true) {
			result *= 2;
		}
		return result;
	}
}
