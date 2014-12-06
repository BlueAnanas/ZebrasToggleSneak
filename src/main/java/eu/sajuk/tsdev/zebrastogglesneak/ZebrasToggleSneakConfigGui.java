package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ZebrasToggleSneakConfigGui extends GuiConfig {

	public ZebrasToggleSneakConfigGui(GuiScreen parent) {
		super(parent,
				(new ConfigElement(ZebrasToggleSneak.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
				"@MOD_ID@", false, false, "@MOD_NAME@", GuiConfig.getAbridgedConfigPath(ZebrasToggleSneak.config.toString()));
    }
}
