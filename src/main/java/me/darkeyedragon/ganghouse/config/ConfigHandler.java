package me.darkeyedragon.ganghouse.config;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigHandler {

    private final Plugin plugin;
    private HashMap<Material, Double> materials;

    public ConfigHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public HashMap<Material, Double> getMaterials() {
        if (materials == null) {
            materials = new HashMap<>();
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("stats.value_blocks");
            Set<String> keys = section.getKeys(false);
            keys.forEach(key -> {
                Double value = section.getDouble(key);
                materials.put(Material.valueOf(key), value);
            });
            return materials;
        }
        return materials;
    }
    public void addMaterial(Material material, double worth) {
        plugin.getConfig().set("stats.value_blocks."+material.name(), worth);
        plugin.saveConfig();
    }
}
