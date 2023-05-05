package com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MainCommand implements TabExecutor {

    private final WarpGUIPlugin plugin;

    public MainCommand(WarpGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
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
