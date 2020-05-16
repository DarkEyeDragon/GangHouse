package me.darkeyedragon.ganghouse.listener;

import me.jet315.houses.Core;
import net.brcdev.gangs.event.GangCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GangCreateListener implements Listener {

    private final Core core;

    public GangCreateListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onGangCreate(GangCreateEvent event){
        event.getPlayer().sendMessage("Creating a House for you now...");
        event.getPlayer().performCommand("house purchase");
    }

}
