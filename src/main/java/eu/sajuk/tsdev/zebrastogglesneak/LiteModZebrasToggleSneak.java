package eu.sajuk.tsdev.zebrastogglesneak;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "@MOD_ID@.json")
public class LiteModZebrasToggleSneak implements LiteMod, Tickable, Configurable {

	@Expose
	@SerializedName("toggle_sneak")
	public boolean toggleSneak = true;
	@Expose
	@SerializedName("toggle_sprint")
	public boolean toggleSprint = false;
	@Expose
	@SerializedName("display_status")
	public boolean displayStatus = true;
	
	public ZebrasToggleSneak ZTS = new ZebrasToggleSneak();
	private class KeyAndAction {
		public KeyBinding kb; public boolean pressed = false;
		public KeyAndAction(KeyBinding kb) { this.kb = kb; }
	}
	private List<KeyAndAction> kaaList = new ArrayList<KeyAndAction>();		

	public String getName() {
        return "@MOD_NAME@";
    }

    public String getVersion() {
        return "@MOD_VERSION@";
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
    	
    	ZTS.clientTick();
    	if (inGame && minecraft.currentScreen == null) {
    		if (Minecraft.isGuiEnabled()) ZTS.renderGameOverlay();
    		for(KeyAndAction kaa: kaaList) {
    			if (kaa.kb.getIsKeyPressed()) {
    				if (!kaa.pressed) {
    				kaa.pressed=true;
    				ZTS.onKeyInput(kaa.kb);
    				}
    			} else {
    				kaa.pressed=false;    				
    			}
    		}
    	}
    }

    public void init(File configPath) {
    	
    	ZTS.liteLoaded = true;
        // check if forge is installed.
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            ZTS.forgePresent = true;
        } catch (ClassNotFoundException e) {
        	ZTS.forgePresent = false;
        }
        ZTS.toggleSneak = toggleSneak;
        ZTS.toggleSprint = toggleSprint;
        ZTS.displayStatus = displayStatus;
        for(KeyBinding kb: ZTS.getKeyBindings()) {
        	kaaList.add(new KeyAndAction(kb));
        	LiteLoader.getInput().registerKeyBinding(kb);
        }

    }

	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		return ZebrasToggleSneakConfigPanelLl.class;
	}	

}
