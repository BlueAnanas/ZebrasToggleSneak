package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.config.GuiConfig;

public class ZebrasToggleSneakConfigGui extends GuiConfig {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ZebrasToggleSneakConfigGui(GuiScreen parent) {
		super(parent,
				(new ConfigElement(ZebrasToggleSneak.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
				"@MOD_ID@", false, false, "@MOD_NAME@", GuiConfig.getAbridgedConfigPath(ZebrasToggleSneak.config.toString()));
    }
}
