package me.darkeyedragon.ganghouse.listener;

import me.jet315.houses.Core;
import me.jet315.houses.manager.HousePlayer;
import net.brcdev.gangs.event.PlayerJoinGangEvent;
import net.brcdev.gangs.event.PlayerLeaveGangEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class GangJoinListener implements Listener {

    private final Core core;

    public GangJoinListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onGangJoin(PlayerJoinGangEvent event){
        Player player = event.getGang().getOwner().getPlayer();
        if(player == null) return;
        player.performCommand("house trust "+event.getPlayer().getName());
    }

    @EventHandler
    public void onGangLeave(PlayerLeaveGangEvent event){
        Player player = event.getGang().getOwner().getPlayer();
        if(player == null && event.getGang().getOwner() == event.getPlayer()) return;
        player.performCommand("house untrust "+event.getPlayer().getName());
    }
}
