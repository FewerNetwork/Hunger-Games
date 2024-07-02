package me.aymanisam.hungergames.commands;

import me.aymanisam.hungergames.HungerGames;
import me.aymanisam.hungergames.handlers.ArenaHandler;
import me.aymanisam.hungergames.handlers.ChestRefillHandler;
import me.aymanisam.hungergames.handlers.LangHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ChestRefillCommand implements CommandExecutor {
    private final HungerGames plugin;
    private final LangHandler langHandler;
    private final ArenaHandler arenaHandler;
    private final ChestRefillHandler chestRefillHandler;

    public ChestRefillCommand(HungerGames plugin) {
        this.plugin = plugin;
        this.langHandler = new LangHandler(plugin);
        this.arenaHandler = new ArenaHandler(plugin);
        this.chestRefillHandler = new ChestRefillHandler(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(langHandler.getMessage("no-server"));
            return true;
        }

        langHandler.getLangConfig(player);

        if (!player.hasPermission("hungergames.chestrefill")) {
            player.sendMessage(langHandler.getMessage("no-permission"));
            return true;
        }

        System.out.println(player.getWorld());
        FileConfiguration ArenaConfig = arenaHandler.getArenaConfig(player.getWorld());
        String worldName = ArenaConfig.getString("region.world");

        if (worldName == null) {
            sender.sendMessage(langHandler.getMessage("chestrefill.no-arena"));
            return true;
        }

        chestRefillHandler.refillChests(player.getWorld());

        return true;
    }
}
