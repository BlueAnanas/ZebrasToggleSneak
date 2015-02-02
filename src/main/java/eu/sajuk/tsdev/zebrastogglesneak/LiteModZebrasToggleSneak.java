package eu.sajuk.tsdev.zebrastogglesneak;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;

import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;

public class LiteModZebrasToggleSneak implements Tickable {

    public String getName() {
        return "@MOD_NAME@";
    }

    public String getVersion() {
        return "@MOD_VERSION@";
    }

    @Override
    public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
    	ZebrasToggleSneak.clientTick(minecraft);
    }

    public void init(File configPath) {
    	ZebrasToggleSneak.liteLoaded = true;
        String relativeConfig = "@MOD_ID@";
        File liteConfigDir = new File(LiteLoader.getCommonConfigFolder(), relativeConfig);
        File mcConfigDir = new File(new File(LiteLoader.getGameDirectory(), "config"), relativeConfig);

        // If forge old exist and liteloader configs don't, copy over.
        if (!liteConfigDir.exists() && mcConfigDir.exists()) {
            try {
                FileUtils.copyDirectory(mcConfigDir, liteConfigDir);
            } catch (IOException e) {
                // Old configs found, but unable to convert.
            }
        }
        // check if forge is installed.
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            ZebrasToggleSneak.forgePresent = true;
        } catch (ClassNotFoundException e) {
        	ZebrasToggleSneak.forgePresent = false;
        }
    }

	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
		// TODO Auto-generated method stub
	}	

}
