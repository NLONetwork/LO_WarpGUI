package com.gmail.necnionch.myplugin.lowarpgui.bukkit;

import com.gmail.necnionch.myplugin.infogui.bukkit.events.InfoGUIMailPanelEvent;
import com.gmail.necnionch.myplugin.infogui.bukkit.gui.PanelItem;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.MainCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.SetupCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands.WarpCommand;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.events.WarpPointAccessCheckEvent;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel.WarpMenuPanel;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel.WarpMenuPanelFloodgate;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Optional;

public final class WarpGUIPlugin extends JavaPlugin implements Listener {
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

        getServer().getPluginManager().registerEvents(this, this);
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
        Player bukkitPlayer = Bukkit.getPlayer(player.getJavaUniqueId());
        if (bukkitPlayer == null)
            return;

        WarpMenuPanelFloodgate.open(bukkitPlayer, player);
    }

    public boolean isBedrockPlayer(Player player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
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


    @EventHandler
    public void onMainPanel(InfoGUIMailPanelEvent event) {
        String name = ChatColor.AQUA + "施設移動";
        event.getSlots()[31] = PanelItem.createItem(Material.ENDER_PEARL, name).setClickListener((e, p) -> {
            if (isBedrockPlayer(event.getPlayer())) {
                event.getPlayer().closeInventory();
                getServer().getScheduler().runTaskLater(this, () ->
                        openGUI(event.getPlayer()), 5);
                return;
            }
            openGUI(event.getPlayer());
        });
    }

}
