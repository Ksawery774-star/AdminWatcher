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
            String logMessage = String.format("%s zmienił gamemode na CREATIVE | Lok: %s %d,%d,%d",
                    player.getName(),
                    player.getWorld().getName(),
                    player.getBlockX(), player.getBlockY(), player.getBlockZ());

            logManager.log("GAMEMODE", logMessage, player);

            // Opcjonalnie: sprawdzenie czy gracz był niedawno w creative i dał sobie rzeczy
            if (configManager.isSuspiciousEnabled()) {
                logManager.checkRecentCreativeGive(player);
            }
        }
    }
}