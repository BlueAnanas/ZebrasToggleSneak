package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;

public class MovementInputModded extends MovementInput {

	private final GameSettings gameSettings;
	public boolean sprint;
	private ZebrasToggleSneak ZTS;
	private boolean sneakWasPressed;
	private boolean sprintWasPressed;

	public MovementInputModded(GameSettings gameSettings, ZebrasToggleSneak ZTS) {
		this.gameSettings = gameSettings;
		this.sprint = false;
		this.ZTS = ZTS;
		this.sneakWasPressed = false;
		this.sprintWasPressed = false;
	}

	public void updatePlayerMoveState() {
		
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (this.forwardKeyDown = gameSettings.keyBindForward.isKeyDown()) moveForward++;
		if (this.backKeyDown = gameSettings.keyBindBack.isKeyDown()) moveForward--;
		if (this.leftKeyDown = gameSettings.keyBindLeft.isKeyDown()) moveStrafe++;
		if (this.rightKeyDown = gameSettings.keyBindRight.isKeyDown()) moveStrafe--;

		jump = gameSettings.keyBindJump.isKeyDown();
		
		if (ZTS.toggleSneak) {
			if (gameSettings.keyBindSneak.isKeyDown() && !sneakWasPressed) sneak = !sneak;
		} else {
			sneak = gameSettings.keyBindSneak.isKeyDown();
		}
		if (sneak) {
			moveStrafe *= 0.3F;
			moveForward *= 0.3F;
		}
		sneakWasPressed = gameSettings.keyBindSneak.isKeyDown();
		
		if (ZTS.toggleSprint) {
			// sprint conditions same as in net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()
			// therefore sprinting is only possible if on ground, not too hungry etc
			if (gameSettings.keyBindSprint.isKeyDown() && !sprintWasPressed) sprint = !sprint;
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (sprint && !player.isSprinting() && !sneak  && moveForward >= 0.8F
					&& (player.getFoodStats().getFoodLevel() > 6 || player.capabilities.allowFlying) 
					&& !player.isHandActive() && !player.isPotionActive(MobEffects.blindness))
				player.setSprinting(true);
		} else sprint = false;
		sprintWasPressed = gameSettings.keyBindSprint.isKeyDown();
	}
}
