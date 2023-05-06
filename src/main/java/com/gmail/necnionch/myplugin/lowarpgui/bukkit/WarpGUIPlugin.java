package com.gmail.necnionch.myplugin.lowarpgui.bukkit;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.MainCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.SetupCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.WarpCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.events.WarpPointAccessCheckEvent;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel.WarpMenuPanel;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel.WarpMenuPanelFloodgate;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Optional;

public final class WarpGUIPlugin extends JavaPlugin {
    private final WarpConfig warpConfig = new WarpConfig(this);

    @Override
    public void onEnable() {
        warpConfig.load();
        Optional.ofNullable(getCommand("warptp")).ifPresent(cmd ->
                cmd.setExecutor(new WarpCommand(this)));
        Optional.ofNullable(getCommand("warpgui")).ifPresent(cmd ->
                cmd.setExecutor(new MainCommand(this)));
        Optional.ofNullable(getCommand("warpguisetup")).ifPresent(cmd ->
                cmd.setExecutor(new SetupCommand(this)));
    }

    @Override
    public void onDisable() {
    }

    public WarpConfig getWarpConfig() {
        return warpConfig;
    }


    public void openGUI(Player player) {
        if (!getServer().getPluginManager().isPluginEnabled("floodgate")) {
            openBukkitGUI(player);
            return;
        }

        FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
        if (floodgatePlayer != null) {
            openFloodgateGUI(floodgatePlayer);
        } else {
            openBukkitGUI(player);
        }
    }

    public void openBukkitGUI(Player player) {
        WarpMenuPanel.open(player);
    }

    public void openFloodgateGUI(FloodgatePlayer player) {
        WarpMenuPanelFloodgate.open(Bukkit.getPlayer(player.getJavaUniqueId()), player);
    }

    public boolean checkAllowedAccess(WarpPoint point, Player player) throws WarpPoint.AccessDenied {
        if (!player.hasPermission("warpgui.access." + point.getId()))
            return false;

        WarpPointAccessCheckEvent event = new WarpPointAccessCheckEvent(point, player);
        getServer().getPluginManager().callEvent(event);

        if (Event.Result.DENY.equals(event.getResult())) {
            if (event.getReason() != null)
                throw event.getReason();
            return false;
        }

        return Event.Result.DEFAULT.equals(event.getResult()) || Event.Result.ALLOW.equals(event.getResult());
    }

}
