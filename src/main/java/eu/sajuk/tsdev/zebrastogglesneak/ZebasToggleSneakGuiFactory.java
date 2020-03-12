package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;

public class ZebasToggleSneakGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new ZebrasToggleSneakConfigGui(parentScreen);
	}

	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return null; }

    public static class ZebrasToggleSneakConfigGui extends GuiConfig
    {
    	
    	public ZebrasToggleSneakConfigGui(GuiScreen parent)
    	{
			super(parent,
			(new ConfigElement(ZebrasToggleSneak.config.getCategory(Configuration.CATEGORY_GENERAL))).getChildElements(),
			"@MOD_ID@", false, false, I18n.format("zebrastogglesneak.config.panel.title"));
    }
    }


}
