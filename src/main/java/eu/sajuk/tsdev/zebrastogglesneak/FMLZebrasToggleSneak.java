package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

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

		ZebrasToggleSneak.toggleSprint = config.getBoolean("Sneak function enabled", Configuration.CATEGORY_GENERAL, ZebrasToggleSneak.toggleSneak, "Will the sneak toggle function be enabled on startup?");
		ZebrasToggleSneak.toggleSprint = config.getBoolean("Sprint function enabled", Configuration.CATEGORY_GENERAL, ZebrasToggleSneak.toggleSprint, "Will the sprint toggle function be enabled on startup?");

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
