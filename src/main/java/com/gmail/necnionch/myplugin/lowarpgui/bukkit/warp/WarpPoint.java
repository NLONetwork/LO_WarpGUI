package com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.WarpGUIPlugin;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;

public class WarpPoint {

    private final WarpGUIPlugin plugin = JavaPlugin.getPlugin(WarpGUIPlugin.class);
    private final String id;
    private @Nullable String displayName;
    private int slot;
    private @Nullable Material itemIcon;
    private @Nullable String imageIcon;
    private String world;
    private double x;
    private double y;
    private double z;
    private int yaw;

    public WarpPoint(String id, @Nullable String displayName, int slot, String world, double x, double y, double z, int yaw) {
        this.id = id;
        this.displayName = displayName;
        this.slot = slot;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public String getId() {
        return id;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public String getDisplayNameOrId() {
        return Optional.ofNullable(displayName)
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .orElse(id);
    }

    public int getSlot() {
        return slot;
    }

    public @Nullable Material getItemIcon() {
        return itemIcon;
    }

    public @Nullable String getImageIcon() {
        return imageIcon;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public int getYaw() {
        return yaw;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setItemIcon(@Nullable Material itemIcon) {
        this.itemIcon = itemIcon;
    }

    public void setImageIcon(@Nullable String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public void setWorld(String world) {
        this.world = world;
    }


    public void setPosition(World world, Location location) {
        this.world = world.getName();
        this.x = location.getBlockX() + 0.5;
        this.y = location.getY();
        this.z = location.getBlockZ() + 0.5;
        this.yaw = (Math.round(location.getYaw() / 45f) & 0x7) * 45;
    }

    public Location createLocation() throws WorldNotFoundError {
        World world = Bukkit.getWorld(this.world);
        if (world == null)
            throw new WorldNotFoundError(this.world);

        return new Location(world, x, y, z, yaw, 0);
    }

    public void teleport(Entity entity) throws WorldNotFoundError {
        entity.teleport(createLocation());
        entity.sendMessage(ChatColor.GOLD + getDisplayNameOrId() + ChatColor.GOLD + "に移動しました！");
    }


    public void save(ConfigurationSection config) {
        config.set("display", displayName);
        config.set("slot", slot);
        config.set("world", world);
        config.set("x", x);
        config.set("y", y);
        config.set("z", z);
        config.set("d", yaw);
        config.set("icon_item", Optional.ofNullable(itemIcon)
                .map(Enum::name)
                .map(String::toLowerCase)
                .orElse(null));
        config.set("icon_image", imageIcon);
    }

    public static WarpPoint read(String id, ConfigurationSection section) {
        String display = section.getString("display");
        int slot = section.getInt("slot");
        String world = section.getString("world");
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        int yaw = section.getInt("d");

        String tmp = section.getString("icon_item");
        Material icon = null;
        if (tmp != null) {
            try {
                icon = Material.valueOf(tmp.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
            }
        }
        WarpPoint point = new WarpPoint(id, display, slot, world, x, y, z, yaw);
        point.setItemIcon(icon);
        point.setImageIcon(section.getString("icon_image"));
        return point;
    }

    public boolean checkAllowedAccess(Player player) throws AccessDenied {
        return plugin.checkAllowedAccess(this, player);
    }

    public boolean isAllowedAccess(Player player) {
        try {
            return plugin.checkAllowedAccess(this, player);
        } catch (AccessDenied e) {
            return false;
        }
    }


    public static boolean allowedIdNaming(String value) {
        return value.toLowerCase(Locale.ROOT).matches("[a-z0-9_-]+");
    }


    public static final class WorldNotFoundError extends Exception {

        private final String world;

        public WorldNotFoundError(String world) {
            this.world = world;
        }

        public String getWorld() {
            return world;
        }

    }

    public static final class AccessDenied extends Exception {

        private @Nullable final String message;

        public AccessDenied(@Nullable String reasonMessage) {
            message = reasonMessage;
        }

        @Override
        public @Nullable String getMessage() {
            return message;
        }

    }

}
