package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class MovementInputModded extends MovementInput {

	public boolean sprint;
	private GameSettings gameSettings;
	private ZebrasToggleSneak ZTS;
	private boolean sneakWasPressed;
	private boolean sprintWasPressed;

	public MovementInputModded(GameSettings gameSettings, ZebrasToggleSneak ZTS) {
		this.sprint = false;
		this.gameSettings = gameSettings;
		this.ZTS = ZTS;
		this.sneakWasPressed = false;
		this.sprintWasPressed = false;
	}

	public void updatePlayerMoveState() {
		
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (gameSettings.keyBindForward.isKeyDown()) moveForward++;
		if (gameSettings.keyBindBack.isKeyDown()) moveForward--;
		if (gameSettings.keyBindLeft.isKeyDown()) moveStrafe++;
		if (gameSettings.keyBindRight.isKeyDown()) moveStrafe--;

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
			if (sprint &&  moveForward == 1.0F && player.onGround && !player.isUsingItem()
					&& !player.isPotionActive(Potion.blindness)) player.setSprinting(true);
		} else sprint = false;
		sprintWasPressed = gameSettings.keyBindSprint.isKeyDown();
	}
}
