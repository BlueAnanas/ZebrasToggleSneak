package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

public class ZebrasToggleSneak extends Gui {

	public boolean liteLoaded = false;
	public boolean forgePresent = false;
	public boolean toggleSneak = true;
	public boolean toggleSprint = false;
	public boolean displayStatus = true;
	private KeyBinding sneakBinding;
	private KeyBinding sprintBinding;
	private final Minecraft mc = Minecraft.getMinecraft();
	private final MovementInputModded mim = new MovementInputModded(mc.gameSettings, this);

	public List<KeyBinding> getKeyBindings() {
		
		List<KeyBinding> list = new ArrayList<KeyBinding>();		
		list.add(sneakBinding = new KeyBinding("key.toggle.sneak", Keyboard.KEY_G, "key.categories.toggle"));
		list.add(sprintBinding = new KeyBinding("key.toggle.sprint", Keyboard.KEY_V, "key.categories.toggle"));
		return list;
	}

	public void clientTick() {
		
		if ((mc.thePlayer != null) && (!(mc.thePlayer.movementInput instanceof MovementInputModded))) {
			mc.thePlayer.movementInput = mim;
		}
	}
	
	public void renderGameOverlay() {
		
		if (!displayStatus) return;
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
//		int displayWidth = scaledresolution.getScaledWidth();
		int displayHeight = scaledresolution.getScaledHeight();
//		int disCenterX = displayWidth / 2;
		int disCenterY = displayHeight / 2;
		
		String sprint=I18n.format("display.label.sneak"), sneak=I18n.format("display.label.sprint");
		int textWidth = Math.max(mc.fontRendererObj.getStringWidth(sprint), mc.fontRendererObj.getStringWidth(sneak));
		
		drawRect(2, disCenterY - 1 - mc.fontRendererObj.FONT_HEIGHT - 2, 4 + textWidth + 4, disCenterY - 1,
				toggleSneak?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
		drawString(mc.fontRendererObj, sneak, 4, disCenterY - 2 - mc.fontRendererObj.FONT_HEIGHT,
				mim.sneak?colorPack(255,255,0,96):colorPack(64,64,64,128));
		drawRect(2, disCenterY + 1, 4 + textWidth + 4, disCenterY + 1 + mc.fontRendererObj.FONT_HEIGHT + 2,
				toggleSprint?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
		drawString(mc.fontRendererObj, sprint, 4, disCenterY + 2,
				mim.sprint?colorPack(255,255,0,96):colorPack(64,64,64,128));
	}

	public void onKeyInput(KeyBinding kb) {
		
		if ((mc.currentScreen instanceof GuiChat)) return;
		
		if (kb == sneakBinding) toggleSneak = !toggleSneak;
		if (kb == sprintBinding) toggleSprint = !toggleSprint;
	}

	private int colorPack (int red, int green, int blue, int alpha){
		return ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255) | ((alpha & 255) << 24);
	}

}
