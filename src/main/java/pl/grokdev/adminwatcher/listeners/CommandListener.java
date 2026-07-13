package pl.grokdev.adminwatcher.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.grokdev.adminwatcher.AdminWatcher;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

import java.util.List;

public class CommandListener implements Listener {

    private final AdminWatcher plugin;
    private final LogManager logManager;
    private final ConfigManager configManager;

    public CommandListener(AdminWatcher plugin, LogManager logManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.logManager = logManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("adminwatcher.bypass")) return;

        String message = event.getMessage().toLowerCase().trim();
        String cmd = message.split(" ")[0].replace("/", "");

        List<String> monitored = configManager.getMonitoredCommands();
        boolean shouldLog = false;

        for (String monitoredCmd : monitored) {
            if (cmd.equals(monitoredCmd.toLowerCase())) {
                shouldLog = true;
                break;
            }
        }

        if (shouldLog) {
            String logMessage = String.format("%s użył komendy: %s | Lok: %s %d,%d,%d",
                    player.getName(),
                    event.getMessage(),
                    player.getWorld().getName(),
                    player.getBlockX(), player.getBlockY(), player.getBlockZ());

            logManager.log("COMMAND", logMessage, player);

            // Sprawdzenie podejrzanej aktywności (creative + give)
            if (configManager.isSuspiciousEnabled() && 
                (cmd.equals("give") || cmd.equals("minecraft:give") || cmd.equals("i") || cmd.equals("item"))) {
                if (player.getGameMode() == org.bukkit.GameMode.CREATIVE) {
                    logManager.logSuspicious(player, "Gracz w creative użył /give");
                }
            }
        }
    }
}