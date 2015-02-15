package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.mumfrey.liteloader.client.gui.GuiCheckbox;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;

public class ZebrasToggleSneakConfigPanelLl extends Gui implements ConfigPanel {

	/* Line spacing, in points. */
	private final static int SPACING = 16;

	/* Handy references. */
	private LiteModZebrasToggleSneak mod;
	private Minecraft mc;

	/* Gui components. */
	private List<GuiButton> buttons;
	private GuiButton activeButton;
	private GuiCheckbox sneakFunctionEnabled;
	private GuiCheckbox sprintFunctionEnabled;
	private GuiCheckbox statusDisplayEnabled;

	public ZebrasToggleSneakConfigPanelLl() {
		mc = Minecraft.getMinecraft();
	}
	
	/* Get the title to display for the panel. */
	@Override
	public String getPanelTitle() {
		return I18n.format("config.panel.title");
	}

	/* Get the height of the panel in points. */
	@Override
	public int getContentHeight() {
		return SPACING * buttons.size();
	}

	/* On opening of panel, instantiate the user interface components. */
	@Override
	public void onPanelShown(ConfigPanelHost host) {
	    mod = (LiteModZebrasToggleSneak) host.getMod();
	    int id = 0;
	    int line = 0;
	    buttons = new ArrayList<GuiButton>();
	    buttons.add(sneakFunctionEnabled =
	        new GuiCheckbox(id++, 10, SPACING * line++, I18n.format("config.panel.sneak")));
	    sneakFunctionEnabled.checked = mod.toggleSneak;
	    buttons.add(sprintFunctionEnabled =
	        new GuiCheckbox(id++, 10, SPACING * line++, I18n.format("config.panel.sprint")));
	    sprintFunctionEnabled.checked = mod.toggleSprint;
	    buttons.add(statusDisplayEnabled =
		        new GuiCheckbox(id++, 10, SPACING * line++, I18n.format("config.panel.display")));
	    statusDisplayEnabled.checked = mod.displayStatus;
	}

	@Override
	public void onPanelResize(ConfigPanelHost host) {} /* maybe redraw it? */

	/** On closing of panel, save current configuration to disk. */
	@Override
	public void onPanelHidden() {
		LiteLoader.getInstance().writeConfig(mod);
	}

	@Override
	public void onTick(ConfigPanelHost host) {} /* no tick activity */

	/* Draw the configuration panel's elements every refresh. */
	@Override
	public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
		for (GuiButton button : buttons) {
			button.drawButton(mc, mouseX, mouseY);
		}
	}

	/* On click, activate button under cursor if one exists. */
	@Override
	public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
		if (sneakFunctionEnabled.mousePressed(mc, mouseX, mouseY)) {
			activeButton = sneakFunctionEnabled;
			sneakFunctionEnabled.playPressSound(mc.getSoundHandler());
			sneakFunctionEnabled.checked = !sneakFunctionEnabled.checked;
			mod.ZTS.toggleSneak = mod.toggleSneak = sneakFunctionEnabled.checked;
		} else if (sprintFunctionEnabled.mousePressed(mc, mouseX, mouseY)) {
			activeButton = sprintFunctionEnabled;
			sprintFunctionEnabled.playPressSound(mc.getSoundHandler());
			sprintFunctionEnabled.checked = !sprintFunctionEnabled.checked;
			mod.ZTS.toggleSprint = mod.toggleSprint = sprintFunctionEnabled.checked;			
		} else if (statusDisplayEnabled.mousePressed(mc, mouseX, mouseY)) {
			activeButton = statusDisplayEnabled;
			statusDisplayEnabled.playPressSound(mc.getSoundHandler());
			statusDisplayEnabled.checked = !statusDisplayEnabled.checked;
			mod.ZTS.displayStatus = mod.displayStatus = statusDisplayEnabled.checked;			
		}
	}

	/* On release of click, deactivate the selected button (if any). */
	@Override
	public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
		if (activeButton != null) {
			activeButton.mouseReleased(mouseX, mouseY); /* whatever this does, is noop currently */
			activeButton = null;
		}
	}

	@Override
	public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

	/* Allow ESCAPE and RETURN keys to close the configuration panel. */
	@Override
	public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
			host.close(); /* maybe decide if save or cancel (write only on return) */
		}
	}

}
