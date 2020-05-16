package me.darkeyedragon.ganghouse.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.darkeyedragon.ganghouse.Ganghouse;
import net.brcdev.gangs.GangsPlugin;
import net.brcdev.gangs.GangsPlusApi;
import net.brcdev.gangs.gang.Gang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


@CommandAlias("ganghouse|gh")
public class GangHouseCommand extends BaseCommand {

    private final Ganghouse plugin;

    public GangHouseCommand(Ganghouse plugin) {
        this.plugin = plugin;
    }


    @HelpCommand
    @Default
    public static void onHelp(CommandSender sender, CommandHelp help) {
        sender.sendMessage("====== Ganghouse Help ======");
        help.showHelp();
    }

    @Subcommand("add")
    @CommandPermission("gh.add")
    public void onAdd(CommandSender sender, double price) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            try {
                Material material = player.getItemInHand().getType();
                plugin.getConfigHandler().addMaterial(material, price);
                player.sendMessage(ChatColor.GREEN + "Successfully added " + material + " with a value of " + price);
            } catch (NumberFormatException ex) {
                player.sendMessage(ChatColor.RED + "Please enter a valid number.");
            }

        }
    }

    @Subcommand("list")
    @CommandPermission("gh.list")
    public void onList(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "==============" + ChatColor.YELLOW + " [List of materials] " + ChatColor.GREEN + "==============");
        plugin.getConfigHandler().getMaterials().forEach((material, value) -> sender.sendMessage(ChatColor.GRAY + "Material: " + ChatColor.AQUA + material.name() + ChatColor.GRAY + " Value: " + ChatColor.AQUA + value));
    }

    @Subcommand("top")
    @CommandPermission("gh.top")
    public void onTop(CommandSender sender, @Optional Integer page) {
        if(page == null){
            page = 1;
        }
        plugin.getStorageHandler().getWealthMap(page - 1, 10).thenAccept(uuidDoubleMap -> {
            sender.sendMessage(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "#  Player (gang)                       Wealth");
            sender.sendMessage("");
            AtomicInteger index = new AtomicInteger(1);

            uuidDoubleMap.forEach((uuid, aDouble) -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                String name;
                if (offlinePlayer.getName() != null) {
                    name = offlinePlayer.getName();
                } else {
                    name = "Unknown";
                }
                int charsToAdd = 25 - name.length();
                char[] chars = new char[charsToAdd];
                Arrays.fill(chars, ' ');
                String charString = new String(chars);
                sender.sendMessage(index.getAndIncrement() + "  " + offlinePlayer.getName() + " ("+GangsPlugin.getInstance().getGangManager().getPlayersGang(offlinePlayer).getName() + ")" + charString + aDouble);

            });
        });
    }
}
/*else if (args[0].equalsIgnoreCase("wealth")) {
            if (sender.hasPermission("gh.wealth")) {
                if (args[1].equalsIgnoreCase("reset")) {
                    if (sender.hasPermission("gh.wealth.reset")) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                        plugin.getStorageHandler().setWealth(offlinePlayer.getUniqueId(), 0);
                        sender.sendMessage("Successfully reset wealth of " + offlinePlayer.getName());
                    }
                } else if (args[1].equalsIgnoreCase("list")) {
                    int page = 1;
                    if (args.length > 2) {
                        page = Integer.parseInt(args[2]);
                    }

                });
            }
        }*/