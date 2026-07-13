package pl.grokdev.adminwatcher.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import pl.grokdev.adminwatcher.AdminWatcher;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

public class GameModeListener implements Listener {

    private final AdminWatcher plugin;
    private final LogManager logManager;
    private final ConfigManager configManager;

    public GameModeListener(AdminWatcher plugin, LogManager logManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.logManager = logManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (!configManager.isLogCreative()) return;

        Player player = event.getPlayer();
        if (player.hasPermission("adminwatcher.bypass")) return;

        if (event.getNewGameMode() == GameMode.CREATIVE) {
            // Klasyczna sytuacja – admin wchodzi w gm1 żeby "coś przetestować"
            String logMessage = String.format("%s wszedł w CREATIVE | Świat: %s | x:%d y:%d z:%d",
                    player.getName(),
                    player.getWorld().getName(),
                    player.getBlockX(), player.getBlockY(), player.getBlockZ());

            logManager.log("GAMEMODE", logMessage, player);

            if (configManager.isSuspiciousEnabled()) {
                logManager.checkRecentCreativeGive(player);
            }
        }
    }
}