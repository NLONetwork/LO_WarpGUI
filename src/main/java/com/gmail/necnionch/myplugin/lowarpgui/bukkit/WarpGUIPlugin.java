package com.gmail.necnionch.myplugin.lowarpgui.bukkit;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class WarpGUIPlugin extends JavaPlugin {
    private final WarpConfig warpConfig = new WarpConfig(this);

    @Override
    public void onEnable() {
        warpConfig.load();

    }

    @Override
    public void onDisable() {
    }

    public WarpConfig getWarpConfig() {
        return warpConfig;
    }

}
