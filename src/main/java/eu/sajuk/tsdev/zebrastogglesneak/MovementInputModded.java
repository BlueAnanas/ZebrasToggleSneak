package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;

public class MovementInputModded extends MovementInput {

	private boolean sprint;
	private GameSettings gameSettings;
	private boolean sneakWasPressed;
	private boolean sprintWasPressed;

	public MovementInputModded(GameSettings gameSettings) {
		this.sprint = false;
		this.gameSettings = gameSettings; 
		this.sneakWasPressed = false;
		this.sprintWasPressed = false;
	}

	public void updatePlayerMoveState() {
		
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;

		if (this.gameSettings.keyBindForward.getIsKeyPressed()) this.moveForward++;
		if (this.gameSettings.keyBindBack.getIsKeyPressed()) this.moveForward--;
		if (this.gameSettings.keyBindLeft.getIsKeyPressed()) this.moveStrafe++;
		if (this.gameSettings.keyBindRight.getIsKeyPressed()) this.moveStrafe--;

		this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
		
		if (ZebrasToggleSneak.toggleSneak) {
			if (this.gameSettings.keyBindSneak.getIsKeyPressed() && !this.sneakWasPressed) this.sneak = !this.sneak;
		} else {
			this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
		}
		if (this.sneak) {
			this.moveStrafe *= 0.3F;
			this.moveForward *= 0.3F;
		}
		this.sneakWasPressed = this.gameSettings.keyBindSneak.getIsKeyPressed();
		
		if (ZebrasToggleSneak.toggleSprint) {
			// sprint conditions same as in net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()
			// therefore sprinting is only possible if on ground, not too hungry etc
			if (this.gameSettings.keyBindSprint.getIsKeyPressed() && !this.sprintWasPressed) this.sprint = !this.sprint;
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if (this.sprint &&  this.moveForward == 1.0F && player.onGround && !player.isUsingItem()
					&& !player.isPotionActive(Potion.blindness)) player.setSprinting(true);
		} else this.sprint = false;
		this.sprintWasPressed = this.gameSettings.keyBindSprint.getIsKeyPressed();
	}
}
