package eu.sajuk.tsdev.zebrastogglesneak;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;

@Mod(modid="@MOD_ID@", name="@MOD_NAME@", version="@MOD_VERSION@", guiFactory = "eu.sajuk.tsdev.zebrastogglesneak.ZebasToggleSneakGuiFactory")
public class ZebrasToggleSneak {

	public static Configuration config;
	public static boolean toggleSneak = true;
	public static boolean toggleSprint = false;
	public static boolean saveChanges = false;
	private KeyBinding sneakBinding;
	private KeyBinding sprintBinding;

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
		
		this.sneakBinding = new KeyBinding("key.toggle.sneak", 34, "key.categories.toggle");
		this.sprintBinding = new KeyBinding("key.toggle.sprint", 47, "key.categories.toggle");

		ClientRegistry.registerKeyBinding(this.sneakBinding);
		ClientRegistry.registerKeyBinding(this.sprintBinding);
		
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if ((mc.thePlayer != null) && (!(mc.thePlayer.movementInput instanceof MovementInputModded))) {
			mc.thePlayer.movementInput = new MovementInputModded(mc.gameSettings);
		}
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		
		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) return;
		
		if (this.sneakBinding.getIsKeyPressed()) toggleSneak = !toggleSneak;
		if (this.sprintBinding.getIsKeyPressed()) toggleSprint = !toggleSprint;
	}

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {

    	if (eventArgs.modID.equals("@MOD_ID@")) syncConfig();
    }
    
    public void syncConfig() {

		toggleSprint = config.getBoolean("Sneak function enabled", Configuration.CATEGORY_GENERAL, toggleSneak, "Will the sneak toggle function be enabled on startup?");
		toggleSprint = config.getBoolean("Sprint function enabled", Configuration.CATEGORY_GENERAL, toggleSprint, "Will the sprint toggle function be enabled on startup?");

        config.save();
	}

}
