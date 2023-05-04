package com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

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

}
