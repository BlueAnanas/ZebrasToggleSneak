package eu.sajuk.tsdev.zebrastogglesneak;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class ZebrasToggleSneak {

	public static boolean liteLoaded = false;
	public static boolean forgePresent = false;
	public static boolean toggleSneak = true;
	public static boolean toggleSprint = false;
	private static KeyBinding sneakBinding;
	private static KeyBinding sprintBinding;

	public static void registerKeyBinding() {
		
		ZebrasToggleSneak.sneakBinding = new KeyBinding("key.toggle.sneak", Keyboard.KEY_G, "key.categories.toggle");
		Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, ZebrasToggleSneak.sneakBinding);
		ZebrasToggleSneak.sprintBinding = new KeyBinding("key.toggle.sprint", Keyboard.KEY_V, "key.categories.toggle");
		Minecraft.getMinecraft().gameSettings.keyBindings = ArrayUtils.add(Minecraft.getMinecraft().gameSettings.keyBindings, ZebrasToggleSneak.sprintBinding);
	}

	public static void clientTick(Minecraft mc) {
		
		if ((mc.thePlayer != null) && (!(mc.thePlayer.movementInput instanceof MovementInputModded))) {
			mc.thePlayer.movementInput = new MovementInputModded(mc.gameSettings);
		}
	}

	public static void onKeyInput() {
		
		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) return;
		
		if (ZebrasToggleSneak.sneakBinding.getIsKeyPressed()) toggleSneak = !toggleSneak;
		if (ZebrasToggleSneak.sprintBinding.getIsKeyPressed()) toggleSprint = !toggleSprint;
	}

}
