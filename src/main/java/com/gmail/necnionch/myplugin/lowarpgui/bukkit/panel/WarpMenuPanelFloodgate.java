package com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WarpMenuPanelFloodgate {

    private final WarpConfig config = JavaPlugin.getPlugin(WarpGUIPlugin.class).getWarpConfig();
    private final FloodgatePlayer player;
    private final Player bukkitPlayer;

    public WarpMenuPanelFloodgate(Player bukkitPlayer, FloodgatePlayer player) {
        this.player = player;
        this.bukkitPlayer = bukkitPlayer;
    }

    public static WarpMenuPanelFloodgate open(Player bukkitPlayer, FloodgatePlayer player) {
        WarpMenuPanelFloodgate panel = new WarpMenuPanelFloodgate(bukkitPlayer, player);
        panel.open();
        return panel;
    }

    public void open() {
        List<WarpPoint> points = config.points().values().stream()
                .sorted(Comparator.comparingInt(WarpPoint::getSlot))
                .collect(Collectors.toList());

        List<Runnable> handlers = Lists.newArrayList();

        SimpleForm.Builder b = SimpleForm.builder();

        b.title("ワープポイント一覧");

        for (WarpPoint point : points) {
            b.button(ChatColor.stripColor(point.getDisplayNameOrId()) + " に移動する");
            handlers.add(() -> {
                try {
                    point.teleport(bukkitPlayer);
                } catch (WarpPoint.WorldNotFoundError ex) {
                    bukkitPlayer.sendMessage(ChatColor.RED + "ワールドが見つかりませんでした");
                }
            });
        }

        b.validResultHandler((res) -> {
            handlers.get(res.clickedButtonId()).run();
        });


        player.sendForm(b.build());
    }

}
