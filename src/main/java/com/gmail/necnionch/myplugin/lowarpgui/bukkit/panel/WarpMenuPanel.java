package com.gmail.necnionch.myplugin.lowarpgui.bukkit.panel;

import com.gmail.necnionch.myplugin.infogui.bukkit.gui.Panel;
import com.gmail.necnionch.myplugin.infogui.bukkit.gui.PanelItem;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WarpMenuPanel extends Panel {

    private final WarpConfig config = JavaPlugin.getPlugin(WarpGUIPlugin.class).getWarpConfig();

    public WarpMenuPanel(Player player) {
        super(player, 9, "ワープポイント一覧", new ItemStack(Material.AIR));
    }

    public static WarpMenuPanel open(Player player) {
        WarpMenuPanel panel = new WarpMenuPanel(player);
        panel.open();
        return panel;
    }


    @Override
    public int getSize() {
        int line = (int) Math.ceil(config.points().size() / 9f);
        return Math.max(9, Math.min(line * 9, 54));
    }

    @Override
    public PanelItem[] build() {
        PanelItem[] slots = new PanelItem[getSize()];

        List<WarpPoint> points = config.points().values().stream()
                .sorted(Comparator.comparingInt(WarpPoint::getSlot))
                .collect(Collectors.toList());

        for (int i = 0; i < points.size() && i < getSize(); i++) {
            WarpPoint point = points.get(i);
            slots[i] = PanelItem.create(point.isAllowedAccess(getPlayer()) ? Material.SUNFLOWER : Material.BARRIER)
                    .name(point.getDisplayNameOrId() + ChatColor.GOLD + " に移動する")
                    .build()
                    .setClickListener((ev, p) -> {
                        try {
                            point.teleport(p);
                        } catch (WarpPoint.WorldNotFoundError ex) {
                            p.sendMessage(ChatColor.RED + "ワールドが見つかりませんでした");
                        }
                    });
        }

        return slots;
    }

}
