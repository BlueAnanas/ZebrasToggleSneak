package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.ScaledResolution;
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
	private String sprintTxt, sneakTxt, onlyTxt;

	public GuiDrawer(ZebrasToggleSneak zTS, MovementInputModded mIM) {
		super();
		ZTS = zTS;
		MIM = mIM;
	}
	
	public void setDrawPosition(String hPos, String vPos, String[] hPosOptions, String[] vPosOptions) {
		
		this.hPos = hPos; this.vPos = vPos;
		this.hPosOptions = hPosOptions; this.vPosOptions = vPosOptions;
		this.sprintTxt = I18n.format("zebrastogglesneak.display.label.sprint");
		this.sneakTxt = I18n.format("zebrastogglesneak.display.label.sneak");
        mcDisplayWidth = -1;
        mcDisplayHeight = -1;
	}

	@SubscribeEvent
	public void afterDraw (RenderGameOverlayEvent.Post event) {

		if (event.getType() != ElementType.ALL) return;
		if (ZTS.displayStatus() == 1) {
			computeDrawPosIfChanged();
			drawRect(rectX1, rectSnY1, rectX2, rectSnY2, ZTS.toggleSneak?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRenderer , this.sneakTxt, rectX1 + 2, rectSnY1 + 2,
					MIM.sneak?colorPack(255,255,0,96):colorPack(64,64,64,128));
			drawRect(rectX1, rectSpY1, rectX2, rectSpY2, ZTS.toggleSprint?colorPack(0,0,196,196):colorPack(196,196,196,64));	    	
			drawString(mc.fontRenderer , this.sprintTxt, rectX1 + 2, rectSpY1 + 2,
					MIM.sprint?colorPack(255,255,0,96):colorPack(64,64,64,128));
		} else if (ZTS.displayStatus() == 2) {
			// no optimization here - I don't like the text only display anyway
	        computeTextPos(onlyTxt = MIM.displayText());
			drawString(mc.fontRenderer , onlyTxt, rectX1, rectSnY1, colorPack(255,255,255,192));
		}
	}

	public void computeDrawPosIfChanged() {
		
		if ((mcDisplayWidth == mc.displayWidth) && (mcDisplayHeight == mc.displayHeight)) return;
		
        ScaledResolution scaledresolution = new ScaledResolution(mc);
		
        int displayWidth = scaledresolution.getScaledWidth();
		int textWidth = Math.max(mc.fontRenderer .getStringWidth(this.sprintTxt), mc.fontRenderer .getStringWidth(this.sneakTxt));
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
		int textHeight = mc.fontRenderer .FONT_HEIGHT;
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

	public void computeTextPos(String displayTxt) {
		
        ScaledResolution scaledresolution = new ScaledResolution(mc);
		
        int displayWidth = scaledresolution.getScaledWidth();
		int textWidth = mc.fontRenderer .getStringWidth(displayTxt);
        if (hPos.equals(hPosOptions[2])) {
        	rectX1 = displayWidth - textWidth - 2;
        } else if (hPos.equals(hPosOptions[1])) {
        	rectX1 = (displayWidth / 2) - (textWidth / 2) - 2;
        } else {
        	rectX1 = 2;
        	rectX2 = rectX1 + 2 + textWidth + 2;        	
        }

        int displayHeight = scaledresolution.getScaledHeight();
		int textHeight = mc.fontRenderer .FONT_HEIGHT;
        if (vPos.equals(vPosOptions[2])) {
        	rectSnY1 = displayHeight - 2;
        } else if (vPos.equals(vPosOptions[1])) {
        	rectSnY1 = (displayHeight / 2) + textHeight/2;
        } else {
        	rectSnY1 = 2 + textHeight;
        }
	}

	private int colorPack (int red, int green, int blue, int alpha){
		return ((red & 255) << 16) | ((green & 255) << 8) | (blue & 255) | ((alpha & 255) << 24);
	}
	
}
