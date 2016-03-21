package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
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
public class ZebrasToggleSneak {

	public static Configuration config;
	public boolean toggleSneak = true;
	public boolean toggleSprint = false;
	public boolean flyBoost = false;
	public float flyBoostFactor = 4.0F;
	public int keyHoldTicks = 7;
	public boolean displayStatus = true;
	private final String displayHPosOpts[] = {"left", "center", "right"};
	public String displayHPos = displayHPosOpts[0];
	private final String displayVPosOpts[] = {"top", "middle", "bottom"};
	public String displayVPos = displayVPosOpts[1];
	private KeyBinding sneakBinding;
	private KeyBinding sprintBinding;
	private List<KeyBinding> kbList;
	private final Minecraft mc = Minecraft.getMinecraft();
	private final MovementInputModded mim = new MovementInputModded(mc.gameSettings, this);
	public final GuiDrawer guiDrawer = new GuiDrawer(this, mim);

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
        kbList = getKeyBindings();
        for(KeyBinding kb: kbList) ClientRegistry.registerKeyBinding(kb);

		FMLCommonHandler.instance().bus().register(this); // register the onConfigChanged(...)
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {

		if (eventArgs.modID.equals("@MOD_ID@")) syncConfig();
	}

	public void syncConfig() {

		toggleSneak = config.getBoolean("toggleSneakEnabled", Configuration.CATEGORY_GENERAL, toggleSneak, "Will the sneak toggle function be enabled on startup?", "zebrastogglesneak.config.panel.sneak");
		toggleSprint = config.getBoolean("toggleSprintEnabled", Configuration.CATEGORY_GENERAL, toggleSprint, "Will the sprint toggle function be enabled on startup?", "zebrastogglesneak.config.panel.sprint");
		flyBoost = config.getBoolean("flyBoostEnabled", Configuration.CATEGORY_GENERAL, flyBoost, "Fly boost activated by sprint key in creative mode", "zebrastogglesneak.config.panel.flyboost");
		flyBoostFactor = config.getFloat("flyBoostFactor", Configuration.CATEGORY_GENERAL, flyBoostFactor, 1.0F, 8.0F, "Speed multiplier for fly boost", "zebrastogglesneak.config.panel.flyboostfactor");
		keyHoldTicks = config.getInt("keyHoldTicks", Configuration.CATEGORY_GENERAL, keyHoldTicks, 0, 200, "Minimum key hold time in ticks to prevent toggle", "zebrastogglesneak.config.panel.keyholdticks");
		displayStatus = config.getBoolean("displayEnabled", Configuration.CATEGORY_GENERAL, displayStatus, "Status of the toggle function displayed", "zebrastogglesneak.config.panel.display");
		displayHPos = config.getString("displayHPosition", Configuration.CATEGORY_GENERAL, displayHPos, "Horizontal position of onscreen display", displayHPosOpts, "zebrastogglesneak.config.panel.hpos");
		displayVPos = config.getString("displayVPosition", Configuration.CATEGORY_GENERAL, displayVPos, "Vertical position of onscreen display", displayVPosOpts, "zebrastogglesneak.config.panel.vpos");
		guiDrawer.setDrawPosition(displayHPos, displayVPos, displayHPosOpts, displayVPosOpts);
		config.save();
	}

	public List<KeyBinding> getKeyBindings() {
		
		List<KeyBinding> list = new ArrayList<KeyBinding>();		
		list.add(sneakBinding = new KeyBinding("zebrastogglesneak.key.toggle.sneak", Keyboard.KEY_G, "zebrastogglesneak.key.categories"));
		list.add(sprintBinding = new KeyBinding("zebrastogglesneak.key.toggle.sprint", Keyboard.KEY_V, "zebrastogglesneak.key.categories"));
		return list;
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
	
		if (displayStatus) MinecraftForge.EVENT_BUS.register(guiDrawer);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		
		clientTick();
	}

	public void clientTick() {
		
		if ((mc.thePlayer != null) && (!(mc.thePlayer.movementInput instanceof MovementInputModded))) {
			mc.thePlayer.movementInput = mim;
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		for(KeyBinding kb: kbList) {
			if (kb.isKeyDown()) onKeyInput(kb);
		}
	}

	public void onKeyInput(KeyBinding kb) {
		
		if ((mc.currentScreen instanceof GuiChat)) return;
		
		if (kb == sneakBinding) toggleSneak = !toggleSneak;
		if (kb == sprintBinding) toggleSprint = !toggleSprint;
	}

}
