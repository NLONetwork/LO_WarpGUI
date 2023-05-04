package com.gmail.necnionch.myplugin.lowarpgui.bukkit.config;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import com.gmail.necnionch.myplugin.lowarpgui.common.BukkitConfigDriver;
import com.google.common.collect.Maps;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class WarpConfig extends BukkitConfigDriver {
    private final Map<String, WarpPoint> points = Maps.newHashMap();

    public WarpConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onLoaded(FileConfiguration config) {
        if (super.onLoaded(config)) {
            points.clear();

            ConfigurationSection targets = config.getConfigurationSection("targets");
            if (targets != null) {
                for (String key : targets.getKeys(false)) {
                    ConfigurationSection child = targets.getConfigurationSection(key);
                    if (child != null) {
                        WarpPoint point = WarpPoint.read(key, child);
                        points.put(key, point);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean save() {
        config.set("targets", null);
        ConfigurationSection targets = config.createSection("targets");
        points.values().forEach(p -> {
           p.save(targets.createSection(p.getId()));
        });
        return super.save();
    }

}
