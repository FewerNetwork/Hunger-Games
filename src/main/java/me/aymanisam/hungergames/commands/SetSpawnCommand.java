package me.aymanisam.hungergames.commands;

import me.aymanisam.hungergames.HungerGames;
import me.aymanisam.hungergames.handlers.LangHandler;
import me.aymanisam.hungergames.handlers.SetSpawnHandler;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor {
    private final LangHandler langHandler;
    private final SetSpawnHandler setSpawnHandler;

    public SetSpawnCommand(HungerGames plugin, SetSpawnHandler setSpawnHandler) {
        this.langHandler = new LangHandler(plugin);
        this.setSpawnHandler = setSpawnHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(langHandler.getMessage("player-only"));
            return true;
        }

        langHandler.getLangConfig(player);

        if (!(player.hasPermission("hungergames.setspawn"))) {
            sender.sendMessage(langHandler.getMessage("no-permission"));
            return true;
        }

        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = stick.getItemMeta();
        assert meta != null;
        meta.setDisplayName(langHandler.getMessage("setspawn.stick-name"));
        stick.setItemMeta(meta);
        List<String> lore = new ArrayList<>();
        lore.add(langHandler.getMessage("setspawn.stick-left"));
        meta.setLore(lore);
        player.getInventory().addItem(stick);
        setSpawnHandler.createSetSpawnConfig(player.getWorld());
        setSpawnHandler.setSpawnConfig.set("spawnpoints", new ArrayList<>());
        setSpawnHandler.saveSetSpawnConfig();
        setSpawnHandler.resetSpawnPoints();
        sender.sendMessage(langHandler.getMessage("setspawn.spawn-reset"));
        return true;
    }
}
