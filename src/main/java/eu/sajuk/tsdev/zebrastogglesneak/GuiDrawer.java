package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiDrawer extends Gui {

	private final Minecraft mc = Minecraft.getMinecraft();
	private final ZebrasToggleSneak ZTS;
	private final MovementInputModded MIM;
	private int mcDisplayWidth = -1, mcDisplayHeight = -1;
	private int rectX1, rectX2;
	private int rectSnY1, rectSnY2, rectSpY1, rectSpY2;
	private String hPos, vPos;
	private String[] hPosOptions, vPosOptions;
	private String sprintTxt, sneakTxt;

	public GuiDrawer(ZebrasToggleSneak zTS, MovementInputModded mIM) {
		super();
		ZTS = zTS;
		MIM = mIM;
	}
	
	public void setDrawPosition(String hPos, String vPos, String[] hPosOptions, String[] vPosOptions) {
		
		this.hPos = hPos; this.vPos = vPos;
		this.hPosOptions = hPosOptions; this.vPosOptions = vPosOptions;
		sprintTxt = I18n.format("zebrastogglesneak.display.label.sprint");
		sneakTxt = I18n.format("zebrastogglesneak.display.label.sneak");
        mcDisplayWidth = -1;
        mcDisplayHeight = -1;
	}

	@SubscribeEvent
	public void afterDraw (RenderGameOverlayEvent.Post event) {

		if (ZTS.displayStatus && event.type == ElementType.ALL) {
			computeDrawPosIfChanged();
			drawRect(rectX1, rectSnY1, rectX2, rectSnY2, ZTS.toggleSneak?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRendererObj, sneakTxt, rectX1 + 2, rectSnY1 + 2,
					MIM.sneak?colorPack(255,255,0,96):colorPack(64,64,64,128));
			drawRect(rectX1, rectSpY1, rectX2, rectSpY2, ZTS.toggleSprint?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRendererObj, sprintTxt, rectX1 + 2, rectSpY1 + 2,
					MIM.sprint?colorPack(255,255,0,96):colorPack(64,64,64,128));
		}

		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		drawString(mc.fontRendererObj, MIM.debugFlySpeed(),
				scaledresolution.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(MIM.debugFlySpeed()) / 2,
				scaledresolution.getScaledHeight() - 6 * mc.fontRendererObj.FONT_HEIGHT - 2,
				colorPack(255,255,0,96));
		drawString(mc.fontRendererObj, MIM.debugMovement(),
				scaledresolution.getScaledWidth() / 2 - mc.fontRendererObj.getStringWidth(MIM.debugMovement()) / 2,
				scaledresolution.getScaledHeight() - 7 * mc.fontRendererObj.FONT_HEIGHT - 4,
				colorPack(255,255,0,96));
		
	}

	public void computeDrawPosIfChanged() {
		
		if ((mcDisplayWidth == mc.displayWidth) && (mcDisplayHeight == mc.displayHeight)) return;
		
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		
        int displayWidth = scaledresolution.getScaledWidth();
		int textWidth = Math.max(mc.fontRendererObj.getStringWidth(sprintTxt), mc.fontRendererObj.getStringWidth(sneakTxt));
        if (hPos.equals(hPosOptions[2])) {
        	rectX2 = displayWidth - 2;
        	rectX1 = rectX2 - 2 - textWidth - 2;
        } else if (hPos.equals(hPosOptions[1])) {
        	rectX1 = (displayWidth / 2) - (textWidth / 2) - 2;
        	rectX2 = rectX1 + 2 + textWidth + 2;        	
        } else {
        	rectX1 = 2;
        	rectX2 = rectX1 + 2 + textWidth + 2;        	
        }

        int displayHeight = scaledresolution.getScaledHeight();
		int textHeight = mc.fontRendererObj.FONT_HEIGHT;
        if (vPos.equals(vPosOptions[2])) {
        	rectSpY2 = displayHeight - 2;
        	rectSpY1 = rectSpY2 - 2 - textHeight - 2;
        	rectSnY2 = rectSpY1 - 2;
        	rectSnY1 = rectSnY2 - 2 - textHeight - 2;
        } else if (vPos.equals(vPosOptions[1])) {
        	rectSnY1 = (displayHeight / 2) - 1 - 2 - textHeight - 2;
        	rectSnY2 = rectSnY1 + 2 + textHeight + 2;
        	rectSpY1 = rectSnY2 + 2;
        	rectSpY2 = rectSpY1 + 2 + textHeight + 2;
        } else {
        	rectSnY1 = 2;
        	rectSnY2 = rectSnY1 + 2 + textHeight + 2;
        	rectSpY1 = rectSnY2 + 2;
        	rectSpY2 = rectSpY1 + 2 + textHeight + 2;
        }
		
        mcDisplayWidth = mc.displayWidth;
        mcDisplayHeight = mc.displayHeight;
	}

	private int colorPack (int red, int green, int blue, int alpha){
		return ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255) | ((alpha & 255) << 24);
	}
	
}
