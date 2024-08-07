package me.aymanisam.hungergames.listeners;

import me.aymanisam.hungergames.HungerGames;
import me.aymanisam.hungergames.handlers.LangHandler;
import me.aymanisam.hungergames.handlers.SetSpawnHandler;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements Listener {
    private final LangHandler langHandler;
    private final SetSpawnHandler setSpawnHandler;

    public SignClickListener(HungerGames plugin, LangHandler langHandler, SetSpawnHandler setSpawnHandler) {
        this.langHandler = langHandler;
        this.setSpawnHandler = setSpawnHandler;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ;

            Block block = event.getClickedBlock();
            assert block != null;

            if (block.getState() instanceof Sign sign) {
                if (sign.getLine(0).equalsIgnoreCase("[Join]")) {
                    if (HungerGames.gameStarted) {
                        player.sendMessage(langHandler.getMessage(player, "startgame.started"));
                        return;
                    }

                    if (HungerGames.gameStarting) {
                        player.sendMessage(langHandler.getMessage(player, "startgame.starting"));
                        return;
                    }

                    if (setSpawnHandler.spawnPointMap.containsValue(player)) {
                        player.sendMessage(langHandler.getMessage(player, "game.already-joined"));
                        return;
                    }

                    setSpawnHandler.teleportPlayerToSpawnpoint(player);
                }
            }
        }
    }
}
