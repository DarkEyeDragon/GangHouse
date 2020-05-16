package me.darkeyedragon.ganghouse;

import co.aikar.commands.BukkitCommandManager;
import me.darkeyedragon.ganghouse.command.GangHouseCommand;
import me.darkeyedragon.ganghouse.config.ConfigHandler;
import me.darkeyedragon.ganghouse.listener.*;
import me.darkeyedragon.ganghouse.storage.StorageHandler;
import me.jet315.houses.Core;
import net.brcdev.gangs.GangsPlusApi;
import net.brcdev.gangs.gang.Gang;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class Ganghouse extends JavaPlugin {

    private ConfigHandler configHandler;
    private StorageHandler storageHandler;
    private Core housesCore;
    private BukkitCommandManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        housesCore = Core.getInstance();
        configHandler = new ConfigHandler(this);
        storageHandler = new StorageHandler(housesCore);
        manager = new BukkitCommandManager(this);
        try {
            storageHandler.createColumnIfNotExist();
        } catch (SQLException exception) {
            getLogger().info("Database column already added. Ignoring");
        }
        //Event handlers
        getServer().getPluginManager().registerEvents(new GangCreateListener(housesCore), this);
        getServer().getPluginManager().registerEvents(new GangJoinListener(housesCore), this);
        getServer().getPluginManager().registerEvents(new GangDisbandListener(housesCore), this);
        getServer().getPluginManager().registerEvents(new ChestInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        manager.registerCommand(new GangHouseCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            housesCore.getDb().getSQLConnection().close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public StorageHandler getStorageHandler() {
        return storageHandler;
    }

    public Core getHousesCore() {
        return housesCore;
    }
}
