package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid="@MOD_ID@", name="@MOD_NAME@", version="@MOD_VERSION@", guiFactory = "eu.sajuk.tsdev.zebrastogglesneak.ZebasToggleSneakGuiFactory")
public class FMLZebrasToggleSneak {

	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.setCategoryComment(Configuration.CATEGORY_GENERAL, "ATTENTION: Editing this file manually is no longer necessary. \n" +
				"Use the Mods button on Minecraft's home screen to modify these settings.");
		syncConfig();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
        if (willBeLiteLoaded()) {
            ZebrasToggleSneak.liteLoaded = true; 
            return;
        }

        ZebrasToggleSneak.registerKeyBinding();

		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent event)
	{
		ZebrasToggleSneak.clientTick(Minecraft.getMinecraft());
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		ZebrasToggleSneak.onKeyInput();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {

		if (eventArgs.modID.equals("@MOD_ID@")) syncConfig();
	}

	public void syncConfig() {

		ZebrasToggleSneak.toggleSneak = config.getBoolean(I18n.format("config.panel.sneak"), Configuration.CATEGORY_GENERAL, ZebrasToggleSneak.toggleSneak, I18n.format("config.panel.h.sneak"));
		ZebrasToggleSneak.toggleSprint = config.getBoolean(I18n.format("config.panel.sprint"), Configuration.CATEGORY_GENERAL, ZebrasToggleSneak.toggleSprint, I18n.format("config.panel.h.sprint"));

		config.save();
	}
	
    private boolean willBeLiteLoaded() {
        try {
            Class.forName("com.mumfrey.liteloader.core.LiteLoader");
            Class.forName("eu.sajuk.tsdev.zebrastogglesneak.LiteModZebrasToggleSneak");
            // Add -zebrastogglesneak.ignoreLiteMod=true to JVM args to ignore
            if ("true".equals(System.getProperty("zebrastogglesneak.ignoreLiteMod"))) {
                return false;
            }
            return true;

        } catch (ClassNotFoundException e) {
        }
        return false;
    }

}
