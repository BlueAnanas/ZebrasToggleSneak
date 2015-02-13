package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ZebrasToggleSneakConfigGui extends GuiConfig {

	public ZebrasToggleSneakConfigGui(GuiScreen parent) {
		super(parent,
				(new ConfigElement(FMLZebrasToggleSneak.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
				"@MOD_ID@", false, false, I18n.format("config.panel.title"),
				GuiConfig.getAbridgedConfigPath(FMLZebrasToggleSneak.config.toString()));
    }
}
