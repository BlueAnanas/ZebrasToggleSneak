package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid="@MOD_ID@", name="@MOD_NAME@", version="@MOD_VERSION@", guiFactory = "eu.sajuk.tsdev.zebrastogglesneak.ZebasToggleSneakGuiFactory")
public class FMLZebrasToggleSneak {

	public static Configuration config;
	private ZebrasToggleSneak ZTS = new ZebrasToggleSneak();
	private List<KeyBinding> kbList;

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
            ZTS.liteLoaded = true; 
            return;
        }
        kbList = ZTS.getKeyBindings();
        for(KeyBinding kb: kbList) ClientRegistry.registerKeyBinding(kb);

		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		
		ZTS.clientTick();
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		for(KeyBinding kb: kbList) {
			if (kb.getIsKeyPressed()) ZTS.onKeyInput(kb);
		}
	}

	@SubscribeEvent
	public void afterDraw (RenderGameOverlayEvent.Post event) {

		if (event.type == ElementType.ALL) ZTS.renderGameOverlay();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {

		if (eventArgs.modID.equals("@MOD_ID@")) syncConfig();
	}

	public void syncConfig() {

		ZTS.toggleSneak = config.getBoolean(I18n.format("config.panel.sneak"), Configuration.CATEGORY_GENERAL, ZTS.toggleSneak, I18n.format("config.panel.h.sneak"));
		ZTS.toggleSprint = config.getBoolean(I18n.format("config.panel.sprint"), Configuration.CATEGORY_GENERAL, ZTS.toggleSprint, I18n.format("config.panel.h.sprint"));
		ZTS.displayStatus = config.getBoolean(I18n.format("config.panel.display"), Configuration.CATEGORY_GENERAL, ZTS.displayStatus, I18n.format("config.panel.h.display"));

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
