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
 * Słucha komend i loguje te ważne.
 * Próbuje też parsować /give.
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

        String msg = e.getMessage().trim();
        String lower = msg.toLowerCase();
        String cmdName = lower.split(" ")[0].replace("/", "");

        if (!config.getMonitoredCommands().contains(cmdName)) {
            return;
        }

        String location = p.getWorld().getName() + " " + p.getBlockX() + "," + p.getBlockY() + "," + p.getBlockZ();
        String logMsg = p.getName() + " >> " + msg + " | " + location;
        logManager.log("COMMAND", logMsg);

        // Specjalne przetwarzanie give i gamemode
        if (cmdName.contains("give") || cmdName.contains("item")) {
            logManager.handleGiveCommand(p, msg);
        } else if (cmdName.contains("gamemode") || cmdName.contains("gm")) {
            logManager.handleGamemodeCommand(p, msg);
        }
    }
}