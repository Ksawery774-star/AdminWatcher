package pl.grokdev.adminwatcher.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

/**
 * Loguje zmiany gamemode.
 * Szczególnie interesuje nas creative.
 */
public class GameModeListener implements Listener {

    private final LogManager logManager;
    private final ConfigManager config;

    public GameModeListener(LogManager logManager, ConfigManager config) {
        this.logManager = logManager;
        this.config = config;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        if (!config.isLogCreative()) return;

        Player p = e.getPlayer();
        if (p.hasPermission("adminwatcher.bypass")) return;

        if (e.getNewGameMode() == GameMode.CREATIVE) {
            String loc = p.getWorld().getName() + " " + p.getBlockX() + "," + p.getBlockY() + "," + p.getBlockZ();
            logManager.log("GAMEMODE", p.getName() + " wszedł w CREATIVE | " + loc);
            
            // Zapisujemy czas wejścia w creative – potrzebne do wykrywania podejrzanej aktywności
            logManager.recordCreativeEntry(p);
        }
    }
}