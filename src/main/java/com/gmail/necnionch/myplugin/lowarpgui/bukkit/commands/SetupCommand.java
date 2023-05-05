package com.gmail.necnionch.myplugin.lowarpgui.bukkit.commands;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.config.WarpConfig;
import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        lists.add(new String[] {"remove", "(id)", "ワープポイントを削除"});
        lists.add(new String[] {"reload", null, "設定ファイルを再読み込み"});
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        try {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                executeHelp(sender, label);
            } else if (args[0].equalsIgnoreCase("create")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                String display = (3 <= args.length) ? args[2] : null;
                executeCreate(sender, label, point, display);

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

            } else if (args[0].equalsIgnoreCase("remove")) {
                String point = getArgument(args, 1, "ポイントID を指定してください");
                executeRemove(sender, getPoint(point));

            } else if (args[0].equalsIgnoreCase("reload")) {
                executeReload(sender);

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
        if (args.length <= 1) {
            String tmp = args[0].toLowerCase(Locale.ROOT);
            return Stream.of("list", "info", "create", "pos", "slot", "remove", "reload")
                    .filter(s -> s.startsWith(tmp))
                    .collect(Collectors.toList());
        } else if (args[0].toLowerCase(Locale.ROOT).matches("info|pos|slot|remove")) {
            String tmp = args[1].toLowerCase(Locale.ROOT);
            return config.points().keySet().stream()
                    .filter(s -> s.startsWith(tmp))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    private void executeHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.WHITE + "[LOWarpGUI] Commands Help");
        sender.sendMessage("");
        lists.forEach((docs) -> {
            String line = docs[0];
            if (docs[1] != null)
                line += " " + ChatColor.AQUA + docs[1];
            sender.sendMessage(ChatColor.WHITE + "  /" + label + ChatColor.YELLOW + " " + line);
            sender.sendMessage(ChatColor.DARK_GRAY + "  => " + ChatColor.GRAY + docs[2]);
        });
        sender.sendMessage("");
    }

    private void executeCreate(CommandSender sender, String label, String id, @Nullable String displayName) {
        if (!WarpPoint.allowedIdNaming(id))
            throw Error.of("ID に半角英数字以外を使用することはできません");

        id = id.toLowerCase(Locale.ROOT);
        if (config.points().containsKey(id))
            throw Error.of("ポイントID " + id + " は既に設定されています");

        WarpPoint point = new WarpPoint(id, displayName, 0, "", 0, 0, 0, 0);
        boolean savedLocation = false;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            point.setPosition(player.getWorld(), player.getLocation());
            savedLocation = true;
        }

        config.points().put(id, point);
        config.save();

        sender.sendMessage(ChatColor.GOLD + "ポイント " + id + " を" + (savedLocation ? "現在地で" : "") + "登録しました");
        sender.sendMessage("/" + label + " pos (id) を実行し座標を" + (savedLocation ? "変更できます" : "設定してください"));
    }

    private void executePosition(CommandSender sender, WarpPoint point) {
        Player player = getPlayer(sender);

        point.setPosition(player.getWorld(), player.getLocation());
        config.save();

        sender.sendMessage(ChatColor.GOLD + "ポイント " + point.getId() + " の座標を現在地に設定しました");
    }

    private void executeSlot(CommandSender sender, WarpPoint point, int slot) {
        point.setSlot(slot);
        config.save();
        sender.sendMessage(ChatColor.GOLD + "ポイント " + point.getId() + " の表示並び番号を " + slot + " に設定しました");
    }

    private void executeList(CommandSender sender) {
        Map<String, WarpPoint> points = config.points();

        if (points.isEmpty()) {
            throw Error.of("設定されているポイントが1つもありません");
        }

        sender.sendMessage(ChatColor.WHITE + "[LOWarpGUI] Warp Point List");
        points.values().stream()
                .sorted(Comparator.comparingInt(WarpPoint::getSlot))
                .forEachOrdered(point -> {
                    String sb = "  " + ChatColor.YELLOW +
                            String.format("%-10s", point.getId()) +
                            ChatColor.GRAY +
                            "  -  " +
                            ChatColor.WHITE +
                            point.getWorld() +
                            ChatColor.GRAY + ", " + ChatColor.WHITE +
                            point.getX() +
                            ChatColor.GRAY + ", " + ChatColor.WHITE +
                            point.getY() +
                            ChatColor.GRAY + ", " + ChatColor.WHITE +
                            point.getZ() +
                            (point.getDisplayName() != null ? ChatColor.WHITE + "  (" + ChatColor.GOLD + point.getDisplayNameOrId() + ChatColor.WHITE + ")" : "");
                    sender.sendMessage(sb);
                });
    }

    private void executeInfo(CommandSender sender, WarpPoint point) {
        sender.sendMessage(ChatColor.WHITE + "[LOWarpGUI] Warp Point List");
        sender.sendMessage("");
        sender.sendMessage("  ポイントID: " + point.getId() + ChatColor.GRAY + "  (表示並び番: " + point.getSlot()  + ")");
        sender.sendMessage("  表示名: " + ChatColor.GOLD + point.getDisplayNameOrId());
        sender.sendMessage("  座標: " + String.format("%s, %.1f, %.1f, %.1f", point.getWorld(), point.getX(),  point.getY(), point.getZ()));
        sender.sendMessage("");
    }

    private void executeRemove(CommandSender sender, WarpPoint point) {
        if (config.points().remove(point.getId(), point)) {
            config.save();
            sender.sendMessage(ChatColor.GOLD + "ポイント " + point.getId() + " を削除しました");
        }
    }

    private void executeReload(CommandSender sender) {
        if (config.load()) {
            sender.sendMessage(ChatColor.GOLD + "設定ファイルを再読み込みしました。");
        } else {
            sender.sendMessage(ChatColor.RED + "設定ファイルを再読み込みできませんでした。");
        }
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

    private Player getPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender);
        }
        throw Error.of("プレイヤーのみ実行することができます");
    }

}
