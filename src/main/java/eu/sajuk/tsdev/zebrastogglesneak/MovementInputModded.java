package eu.sajuk.tsdev.zebrastogglesneak;

import java.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class MovementInputModded extends MovementInput {

	public boolean sprint;
	private GameSettings gameSettings;
	private ZebrasToggleSneak ZTS;
	private Minecraft mc;
	private int sneakWasPressed;
	private int sprintWasPressed;
	private EntityPlayerSP player;
	private float originalFlySpeed = -1.0F;
	private float boostedFlySpeed;

	public MovementInputModded(GameSettings gameSettings, ZebrasToggleSneak ZTS) {
		this.sprint = false;
		this.gameSettings = gameSettings;
		this.ZTS = ZTS;
		this.mc = Minecraft.getMinecraft(); // we'll need replace the static ref by a link passed as parameter
		this.sneakWasPressed = 0;
		this.sprintWasPressed = 0;
	}

	public void updatePlayerMoveState() {
		
		player = mc.thePlayer;
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (gameSettings.keyBindForward.isKeyDown()) moveForward++;
		if (gameSettings.keyBindBack.isKeyDown()) moveForward--;
		if (gameSettings.keyBindLeft.isKeyDown()) moveStrafe++;
		if (gameSettings.keyBindRight.isKeyDown()) moveStrafe--;

		jump = gameSettings.keyBindJump.isKeyDown();
		
		if (ZTS.toggleSneak) {
			if (gameSettings.keyBindSneak.isKeyDown()) {
				if (sneakWasPressed == 0) {
					if (sneak) {
						sneakWasPressed = -1;
					} else if (player.isRiding() || player.capabilities.isFlying) {
						sneakWasPressed = ZTS.keyHoldTicks + 1;
					} else {
						sneakWasPressed = 1;
					}
					sneak = !sneak;
				} else if (sneakWasPressed > 0){
					sneakWasPressed++;
				}
			} else {
				if ((ZTS.keyHoldTicks > 0) && (sneakWasPressed > ZTS.keyHoldTicks)) sneak = false;
				sneakWasPressed = 0;
			}
		} else {
			sneak = gameSettings.keyBindSneak.isKeyDown();
		}
		
		if (sneak) {
			moveStrafe *= 0.3F;
			moveForward *= 0.3F;
		}
		
		if (ZTS.toggleSprint) {
			if (gameSettings.keyBindSprint.isKeyDown()) {
				if (sprintWasPressed == 0) {
					if (sprint) {
						sprintWasPressed = -1;
					} else if (player.capabilities.isFlying) {
						sprintWasPressed = ZTS.keyHoldTicks + 1;
					} else {
						sprintWasPressed = 1;
					}
					sprint = !sprint;
				} else if (sprintWasPressed > 0){
					sprintWasPressed++;
				}
			} else {
				if ((ZTS.keyHoldTicks > 0) && (sprintWasPressed > ZTS.keyHoldTicks)) sprint = false;
				sprintWasPressed = 0;
			}
		} else sprint = false;
		
		// sprint conditions same as in net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()
		// check for hungry or flying. But nvm, if conditions not met, sprint will 
		// be canceled there afterwards anyways
		if (sprint && moveForward == 1.0F && player.onGround && !player.isUsingItem()
				&& !player.isPotionActive(Potion.blindness)) player.setSprinting(true);
		
		if (ZTS.flyBoost && player.capabilities.isCreativeMode && player.capabilities.isFlying 
				&& (mc.getRenderViewEntity() == player) && sprint) {
			
			if (originalFlySpeed < 0.0F || this.player.capabilities.getFlySpeed() != boostedFlySpeed)
				originalFlySpeed = this.player.capabilities.getFlySpeed();
			boostedFlySpeed = originalFlySpeed * ZTS.flyBoostFactor;
			player.capabilities.setFlySpeed(boostedFlySpeed);
			
			if (sneak) player.motionY -= 0.15D * (double)(ZTS.flyBoostFactor - 1.0F);
			if (jump) player.motionY += 0.15D * (double)(ZTS.flyBoostFactor - 1.0F);
				
		} else {
			if (player.capabilities.getFlySpeed() == boostedFlySpeed)
				this.player.capabilities.setFlySpeed(originalFlySpeed);
			originalFlySpeed = -1.0F;
		}

	}
	
	public String displayText() {
		
		// This is a slightly refactored version of Deez's UpdateStatus( ... ) function
		// found here https://github.com/DouweKoopmans/ToggleSneak/blob/master/src/main/java/deez/togglesneak/CustomMovementInput.java
		
		String displayText = "";
		boolean isFlying = mc.thePlayer.capabilities.isFlying;
		boolean isRiding = mc.thePlayer.isRiding();
		boolean isHoldingSneak = gameSettings.keyBindSneak.isKeyDown();
		boolean isHoldingSprint = gameSettings.keyBindSprint.isKeyDown();
		
		if (isFlying) {
			if (originalFlySpeed > 0.0F) {
				displayText += "[Flying (" + (new DecimalFormat("#.0")).format(boostedFlySpeed/originalFlySpeed) + "x Boost)]  ";								
			} else {
				displayText += "[Flying]  ";				
			}
		}
		if (isRiding) displayText += "[Riding]  ";
		
		if (sneak) {

			if (isFlying) displayText += "[Descending]  ";
			else if (isRiding) displayText += "[Dismounting]  ";
			else if (isHoldingSneak) displayText += "[Sneaking (Key Held)]  ";
			else displayText += "[Sneaking (Toggled)]  ";

		} else if (sprint && !isFlying && !isRiding) {

			if (isHoldingSprint) displayText += "[Sprinting (Key Held)]";
			else displayText += "[Sprinting (Toggled)]";
		}
		
		return displayText.trim();
	}
}
