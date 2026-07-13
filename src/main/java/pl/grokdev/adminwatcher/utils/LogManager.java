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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LogManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final File logFile;
    private final LinkedList<String> recentLogs = new LinkedList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        this.logFile = new File(dataFolder, "admin-logs.log");
    }

    public void log(String type, String message, Player player) {
        String timestamp = LocalDateTime.now().format(formatter);
        String fullLog = String.format("[%s] [%s] %s", timestamp, type, message);

        // Zapisujemy do pliku – żeby nawet po restarcie było co czytać
        writeToFile(fullLog);

        // Wyrzucamy na konsolę z czerwonym kolorem, żeby od razu rzucało się w oczy
        Bukkit.getConsoleSender().sendMessage("\u00a7c[ADMIN-WATCH] " + fullLog);

        // Dodajemy do pamięci – /adminlogs działa od razu
        addToRecent(fullLog);

        // Jeśli Discord włączony – wysyłamy
        if (configManager.isDiscordEnabled() && !configManager.getDiscordWebhook().isEmpty()) {
            DiscordWebhook.send(configManager.getDiscordWebhook(), 
                "**AdminWatcher** - ktoś coś kombinował:\n" + fullLog);
        }
    }

    public void logSuspicious(Player player, String reason) {
        // Tu dajemy znać, że coś śmierdzi
        String msg = String.format("Podejrzana akcja: %s | Gracz: %s", reason, player.getName());
        log("SUSPICIOUS", msg, player);
    }

    public void checkRecentCreativeGive(Player player) {
        // Na razie prosta wersja – jeśli chcesz pełne okno czasowe daj znać
    }

    private void addToRecent(String log) {
        recentLogs.addFirst(log);
        if (recentLogs.size() > configManager.getMaxLogsInMemory()) {
            recentLogs.removeLast();
        }
    }

    public List<String> getRecentLogs(int amount) {
        List<String> result = new ArrayList<>();
        int count = Math.min(amount, recentLogs.size());
        for (int i = 0; i < count; i++) {
            result.add(recentLogs.get(i));
        }
        return result;
    }

    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            plugin.getLogger().warning("Nie udało się zapisać logu do pliku: " + e.getMessage());
        }
    }

    public void saveAll() {
        // Na razie nic specjalnego, ale zostawiamy na przyszłość
    }
}