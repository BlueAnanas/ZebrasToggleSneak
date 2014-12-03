package eu.sajuk.tsdev.zebrastogglesneak;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

@Mod(modid="@MOD_ID@", name="@MOD_NAME@", version="@MOD_VERSION@")
public class ZebrasToggleSneak {

	private static Property PropToggleSneak, PropToggleSprint;
	public static boolean toggleSneak = true;
	public static boolean toggleSprint = false;
	public static boolean saveChanges = false;
	private KeyBinding sneakBinding;
	private KeyBinding sprintBinding;
	private Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		this.config = new Configuration(event.getSuggestedConfigurationFile());
		this.config.load();
		toggleSneak = (PropToggleSneak = this.config.get("default", "toggleSneak", toggleSneak, "Sneak toggle enabled on startup?")).getBoolean(toggleSneak);
		toggleSprint = (PropToggleSprint = this.config.get("default", "toggleSprint", toggleSprint, "Sprint toggle enabled on startup?")).getBoolean(toggleSprint);
		saveChanges = this.config.get("default", "saveChanges", saveChanges, "Automatically remember last state?").getBoolean(saveChanges);
		this.config.save();
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
		
		if (this.sneakBinding.getIsKeyPressed()) {
			toggleSneak = !toggleSneak;
			if (saveChanges) PropToggleSneak.set(toggleSneak);
		}
		if (this.sprintBinding.getIsKeyPressed()) {
			toggleSprint = !toggleSprint;
			if (saveChanges) PropToggleSprint.set(toggleSprint);
		}
	}

}
