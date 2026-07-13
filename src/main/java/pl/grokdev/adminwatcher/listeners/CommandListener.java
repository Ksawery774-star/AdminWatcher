package pl.grokdev.adminwatcher.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.grokdev.adminwatcher.AdminWatcher;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

import java.util.List;

/**
 * Słucha komend graczy i loguje te, które nas interesują.
 * Proste i skuteczne.
 */
public class CommandListener implements Listener {

    private final AdminWatcher plugin;
    private final LogManager logManager;
    private final ConfigManager config;

    public CommandListener(AdminWatcher plugin, LogManager logManager, ConfigManager config) {
        this.plugin = plugin;
        this.logManager = logManager;
        this.config = config;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("adminwatcher.bypass")) return;

        String msg = e.getMessage().toLowerCase().trim();
        String cmdName = msg.split(" ")[0].replace("/", "");

        if (!config.getMonitoredCommands().contains(cmdName) && 
            !config.getMonitoredCommands().contains("minecraft:" + cmdName)) {
            return;
        }

        // Logujemy komendę
        String location = p.getWorld().getName() + " " + p.getBlockX() + "," + p.getBlockY() + "," + p.getBlockZ();
        String logMsg = p.getName() + " wykonał: " + e.getMessage() + " | " + location;
        logManager.log("COMMAND", logMsg);

        // Sprawdzamy czy to give i czy był niedawno w creative
        if (config.isSuspiciousEnabled() && isGiveCommand(cmdName)) {
            logManager.checkSuspiciousGive(p, e.getMessage());
        }
    }

    private boolean isGiveCommand(String cmd) {
        return cmd.equals("give") || cmd.equals("minecraft:give") || cmd.equals("i") || cmd.equals("item");
    }
}