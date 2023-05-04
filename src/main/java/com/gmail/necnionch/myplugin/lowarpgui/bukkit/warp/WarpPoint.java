package com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;

public class WarpPoint {

    private final String id;
    private @Nullable String displayName;
    private int slot;
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
        this.yaw = (int) (location.getYaw() - 22.5) / 45 % 45;
    }

    public Location createLocation() throws WorldNotFoundError {
        World world = Bukkit.getWorld(this.world);
        if (world == null)
            throw new WorldNotFoundError(this.world);

        return new Location(world, x, y, z, yaw, 0);
    }


    public void save(ConfigurationSection config) {
        config.set("display", displayName);
        config.set("slot", slot);
        config.set("world", world);
        config.set("x", x);
        config.set("y", y);
        config.set("z", z);
        config.set("d", yaw);
    }

    public static WarpPoint read(String id, ConfigurationSection section) {
        String display = section.getString("display");
        int slot = section.getInt("slot");
        String world = section.getString("world");
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        int yaw = section.getInt("d");

        return new WarpPoint(id, display, slot, world, x, y, z, yaw);
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

}
