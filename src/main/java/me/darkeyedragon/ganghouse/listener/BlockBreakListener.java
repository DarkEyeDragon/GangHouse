package me.darkeyedragon.ganghouse.listener;

import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.BukkitUtil;
import me.darkeyedragon.ganghouse.Ganghouse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakListener implements Listener {

    private final Ganghouse ganghouse;

    public BlockBreakListener(Ganghouse ganghouse) {
        this.ganghouse = ganghouse;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        if(ganghouse.getConfigHandler().getMaterials().containsKey(event.getBlock().getType())){
            Plot plot = BukkitUtil.getLocation(event.getPlayer().getLocation()).getPlotAbs();
            double blockWealth = ganghouse.getConfigHandler().getMaterials().get(event.getBlock().getType());
            ganghouse.getStorageHandler().getWealth(plot.owner).thenAccept(wealth ->{
                ganghouse.getStorageHandler().setWealth(plot.owner, wealth-blockWealth);
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event){
        if(ganghouse.getConfigHandler().getMaterials().containsKey(event.getBlock().getType())){
            Plot plot = BukkitUtil.getLocation(event.getPlayer().getLocation()).getPlotAbs();
            double blockWealth = ganghouse.getConfigHandler().getMaterials().get(event.getBlock().getType());
            ganghouse.getStorageHandler().getWealth(plot.owner).thenAccept(wealth -> ganghouse.getStorageHandler().setWealth(plot.owner, wealth+blockWealth));
        }
    }
}
