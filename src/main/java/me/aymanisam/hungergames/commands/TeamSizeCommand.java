package me.aymanisam.hungergames.commands;

import me.aymanisam.hungergames.HungerGames;
import me.aymanisam.hungergames.handlers.LangHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeamSizeCommand implements CommandExecutor {
    private final HungerGames plugin;
    private final LangHandler langHandler;

    public TeamSizeCommand(HungerGames plugin) {
        this.plugin = plugin;
        this.langHandler = new LangHandler(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!(player.hasPermission("hungergames.teamsize"))) {
                sender.sendMessage(langHandler.getMessage("no-permission"));
                return true;
            }
            langHandler.getLangConfig(player);
        }

        if (args.length != 1) {
            sender.sendMessage(langHandler.getMessage("game.team-usage"));
            return true;
        }

        int newSize = Integer.parseInt(args[0]);

        plugin.reloadConfig();
        plugin.getConfig().set("players-per-team", newSize);
        plugin.saveConfig();

        sender.sendMessage(langHandler.getMessage("game.team-size") + newSize);

        return true;
    }
}
