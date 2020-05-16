package me.darkeyedragon.ganghouse.listener;

import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.BukkitUtil;
import me.darkeyedragon.ganghouse.Ganghouse;
import me.darkeyedragon.ganghouse.exception.InvalidPlotWorldException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ChestInteractListener implements Listener {

    private final Ganghouse ganghouse;
    private final Map<Inventory, Double> inventoryWealth = new HashMap<>();
    public ChestInteractListener(Ganghouse ganghouse) {
        this.ganghouse = ganghouse;
    }


    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST) {
            Player player = (Player) event.getPlayer();
            Plot plot = BukkitUtil.getLocation(player.getLocation()).getPlotAbs();
            if(plot == null) return;
            double total = calculateWealth(event.getInventory(), plot);
            inventoryWealth.put(event.getInventory(), total);
        }
    }
    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST) {
            if(!inventoryWealth.containsKey(event.getInventory())) return;
            double wealth = inventoryWealth.get(event.getInventory());
            Player player = (Player) event.getPlayer();
            Plot plot = BukkitUtil.getLocation(player.getLocation()).getPlotAbs();
            if(plot == null) return;
            double total = calculateWealth(event.getInventory(), plot);
            double finalTotal = total - wealth;
            ganghouse.getStorageHandler().getWealth(plot.owner).thenAccept(wealth2 -> ganghouse.getStorageHandler().setWealth(plot.owner, wealth2 + finalTotal));
            inventoryWealth.remove(event.getInventory());
        }
    }
    private double calculateWealth(Inventory inventory, Plot plot){
        if(plot == null) throw new InvalidPlotWorldException("Player is not in a plot world");
        Map<Material, Double> materialPrices = ganghouse.getConfigHandler().getMaterials();
        double total = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) break;
            if (materialPrices.containsKey(itemStack.getType())) {
                double value = ganghouse.getConfigHandler().getMaterials().get(itemStack.getType());
                total += value * itemStack.getAmount();
            }
        }
        return total;
    }
}
