package com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class SetupCommand implements TabExecutor {

    private final WarpGUIPlugin plugin;
    private final WarpConfig config;
    private final List<String[]> lists = Lists.newArrayList();

    public SetupCommand(WarpGUIPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getWarpConfig();

        lists.add(new String[] {"list", null, "ワープポイントの一覧を表示"});
        lists.add(new String[] {"info", "(id)", "ワープポイントの設定内容を表示"});
        lists.add(new String[] {"create", "(id) [表示名]", "ワープポイントを作成"});
        lists.add(new String[] {"pos", "(id)", "テレポート座標を今居る地点に設定"});
        lists.add(new String[] {"slot", "(id) (並び番号)", "表示並び番号を設定 (少ない数から順に)"});
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                executeHelp(sender, label);
            } else if (args[0].equalsIgnoreCase("create")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                String display = (3 < args.length) ? args[2] : null;
                executeCreate(sender, point, display);

            } else if (args[0].equalsIgnoreCase("pos")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                executePosition(sender, getPoint(point));

            } else if (args[0].equalsIgnoreCase("slot")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                String slotInp = getArgument(args, 2, "並び番号 を指定してください");
                int slot;
                try {
                    slot = Integer.parseInt(slotInp);
                } catch (NumberFormatException e) {
                    throw Error.of("数値を指定してください");
                }
                executeSlot(sender, getPoint(point), slot);
                
            } else if (args[0].equalsIgnoreCase("list")) {
                executeList(sender);

            } else if (args[0].equalsIgnoreCase("info")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                executeInfo(sender, getPoint(point));

            } else {
                executeHelp(sender, label);
            }
        } catch (Error e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }


    private void executeHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.WHITE + "[LOWarpGUI] Commands Help");
        sender.sendMessage("");
        lists.forEach((docs) -> {
            String line = docs[0];
            if (docs[1] != null)
                line += " " + ChatColor.AQUA + docs[1];
            sender.sendMessage(ChatColor.WHITE + "  " + label + ChatColor.YELLOW + " " + line);
            sender.sendMessage(ChatColor.DARK_GRAY + "  => " + ChatColor.GRAY + docs[2]);
        });
        sender.sendMessage("");
    }

    private void executeCreate(CommandSender sender, String id, @Nullable String displayName) {

    }

    private void executePosition(CommandSender sender, WarpPoint point) {

    }

    private void executeSlot(CommandSender sender, WarpPoint point, int slot) {

    }

    private void executeList(CommandSender sender) {

    }

    private void executeInfo(CommandSender sender, WarpPoint point) {

    }


    private static final class Error extends java.lang.Error {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public static Error of(String message) {
            return new Error(message);
        }
    }

    private WarpPoint getPoint(String id) {
        id = id.toLowerCase(Locale.ROOT);
        WarpPoint point = config.points().get(id);
        if (point == null)
            throw Error.of("ポイントID " + id + " は設定されていません");
        return point;
    }

    private String getArgument(String[] args, int index, String noSpecifiedMessage) {
        try {
            return args[index];
        } catch (IndexOutOfBoundsException e) {
            throw Error.of(noSpecifiedMessage);
        }
    }

}
