package com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SetupCommand implements TabExecutor {

    private final WarpGUIPlugin plugin;

    public SetupCommand(WarpGUIPlugin plugin) {
        this.plugin = plugin;

        // /warpgui
        // /warpguisetup create (id) [display]
        // /warpguisetup pos (id)
        // /warpguisetup slot (index)
        // /warpguisetup list
        // /warpguisetup info (id)
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

}
