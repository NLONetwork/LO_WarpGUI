package com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WarpCommand implements TabExecutor {

    private final WarpGUIPlugin plugin;
    private final WarpConfig config;

    public WarpCommand(WarpGUIPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getWarpConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "プレイヤーのみ実行することができます");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "ワープポイントを指定してください");

            List<String> allowed = config.points().values().stream()
                    .filter(p -> p.isAllowedAccess(player))
                    .sorted(Comparator.comparingInt(WarpPoint::getSlot))
                    .map(WarpPoint::getId)
                    .collect(Collectors.toList());
            if (!allowed.isEmpty()) {
                sender.sendMessage(ChatColor.WHITE + "移動可能な地点: " + ChatColor.YELLOW + String.join(ChatColor.GRAY + ", " + ChatColor.YELLOW, allowed));
            }
            return true;
        }

        WarpPoint point = config.points().get(args[0].toLowerCase(Locale.ROOT));
        if (point == null) {
            sender.sendMessage(ChatColor.RED + "指定されたポイントが見つかりません");
            return true;
        }

        boolean result;
        try {
            result = point.checkAllowedAccess(player);
        } catch (WarpPoint.AccessDenied e) {
            if (e.getMessage() != null)
                sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }

        if (!result) {
            sender.sendMessage(ChatColor.RED + "このポイントに移動する許可がありません");
            return true;
        }

        try {
            point.teleport(player);
        } catch (WarpPoint.WorldNotFoundError e) {
            sender.sendMessage(ChatColor.RED + "ワールドが見つかりませんでした");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            String tmp = args[0].toLowerCase(Locale.ROOT);
            return config.points().keySet().stream()
                    .filter(s -> s.startsWith(tmp))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
