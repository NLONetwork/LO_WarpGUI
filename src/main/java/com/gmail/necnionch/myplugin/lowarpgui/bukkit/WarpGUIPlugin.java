package com.gmail.necnionch.myplugin.lowarpgui.bukkit;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

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


    public void openGUI(Player player) {
        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if (floodgatePlayer != null) {
            openFloodgateGUI(floodgatePlayer);
        } else {
            openBukkitGUI(player);
        }
    }

    public void openBukkitGUI(Player player) {

    }

    public void openFloodgateGUI(FloodgatePlayer player) {

    }



}
