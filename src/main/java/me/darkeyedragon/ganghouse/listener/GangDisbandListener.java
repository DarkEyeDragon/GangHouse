package me.darkeyedragon.ganghouse.listener;

import me.jet315.houses.Core;
import net.brcdev.gangs.event.PlayerLeaveGangEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GangDisbandListener implements Listener {

    private final Core core;

    public GangDisbandListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onGangDisband(PlayerLeaveGangEvent event) {
        if (event.getPlayer().getUniqueId() == event.getGang().getOwner().getUniqueId()) {
            event.getPlayer().sendMessage("Deleting your house...");
            boolean executed = Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "house delete " + event.getGang().getOwner().getName());
            if(executed){
                event.getPlayer().sendMessage("Your house has been deleted!");
            }else{
                event.getPlayer().sendMessage(ChatColor.RED + "Can not disband your gang as your house can not be deleted! Try again later or contact an admin");
            }
            event.setCancelled(!executed);
        }
    }
}
