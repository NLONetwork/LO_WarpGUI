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
import java.util.List;
import java.util.Locale;

public class MainCommand implements TabExecutor {

    private final WarpGUIPlugin plugin;
    private final WarpConfig config;

    public MainCommand(WarpGUIPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getWarpConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (1 <= args.length) {
                WarpPoint point = config.points().get(args[0].toLowerCase(Locale.ROOT));
                if (point == null) {
                    sender.sendMessage(ChatColor.RED + "指定されたポイントが見つかりません");
                    return true;
                }

                try {
                    point.teleport((Player) sender);
                } catch (WarpPoint.WorldNotFoundError e) {
                    sender.sendMessage(ChatColor.RED + "ワールドが見つかりませんでした");
                }
                return true;
            }
            plugin.openGUI(((Player) sender));
        } else {
            sender.sendMessage(ChatColor.RED + "プレイヤーのみ実行することができます");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }

}
