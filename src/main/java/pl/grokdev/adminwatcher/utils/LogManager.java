package pl.grokdev.adminwatcher.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Zarządza logowaniem.
 * Trzyma ostatnie logi w pamięci + zapisuje do pliku.
 * Ma też prostą logikę wykrywania podejrzanej aktywności.
 */
public class LogManager {

    private final JavaPlugin plugin;
    private final ConfigManager config;
    private final File logFile;
    private final LinkedList<String> recentLogs = new LinkedList<>();
    private final Map<UUID, Long> lastCreativeTime = new HashMap<>();
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogManager(JavaPlugin plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;

        File folder = plugin.getDataFolder();
        if (!folder.exists()) folder.mkdirs();
        this.logFile = new File(folder, "admin-logs.log");
    }

    /**
     * Główna metoda logująca.
     */
    public void log(String type, String message) {
        String timestamp = LocalDateTime.now().format(timeFormat);
        String full = "[" + timestamp + "] [" + type + "] " + message;

        writeToFile(full);
        Bukkit.getConsoleSender().sendMessage("\u00a7c[AdminWatch] " + full);
        addToRecent(full);

        if (config.isDiscordEnabled() && !config.getDiscordWebhook().isEmpty()) {
            DiscordWebhook.send(config.getDiscordWebhook(), full);
        }
    }

    /**
     * Zapisuje czas wejścia gracza w creative.
     */
    public void recordCreativeEntry(Player player) {
        lastCreativeTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /**
     * Sprawdza czy gracz dał sobie rzeczy niedługo po wejściu w creative.
     */
    public void checkSuspiciousGive(Player player, String fullCommand) {
        if (!config.isSuspiciousEnabled()) return;

        Long lastCreative = lastCreativeTime.get(player.getUniqueId());
        if (lastCreative == null) return;

        long secondsAgo = (System.currentTimeMillis() - lastCreative) / 1000;
        if (secondsAgo <= config.getCreativeGiveWindow()) {
            String alert = player.getName() + " wszedł w creative i po " + secondsAgo + 
                    "s użył: " + fullCommand;
            log("SUSPICIOUS", alert);
        }
    }

    private void addToRecent(String line) {
        recentLogs.addFirst(line);
        while (recentLogs.size() > config.getMaxLogsMemory()) {
            recentLogs.removeLast();
        }
    }

    public List<String> getRecent(int amount) {
        List<String> result = new ArrayList<>();
        int limit = Math.min(amount, recentLogs.size());
        for (int i = 0; i < limit; i++) {
            result.add(recentLogs.get(i));
        }
        return result;
    }

    private void writeToFile(String line) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(logFile, true))) {
            w.write(line);
            w.newLine();
        } catch (IOException e) {
            plugin.getLogger().warning("Błąd zapisu logu: " + e.getMessage());
        }
    }

    public void flush() {
        // Na razie nic, ale zostawiamy miejsce na przyszłe flushowanie
    }
}