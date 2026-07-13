package pl.grokdev.adminwatcher.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Obsługa config.yml.
 * Prosta i czytelna.
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration cfg;

    private boolean logCreative;
    private Set<String> monitoredCommands;
    private boolean discordEnabled;
    private String discordWebhook;
    private boolean suspiciousEnabled;
    private int creativeGiveWindow;
    private int maxLogsMemory;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        cfg = plugin.getConfig();

        logCreative = cfg.getBoolean("log-creative", true);

        List<String> cmds = cfg.getStringList("monitored-commands");
        monitoredCommands = new HashSet<>(cmds);

        discordEnabled = cfg.getBoolean("discord.enabled", false);
        discordWebhook = cfg.getString("discord.webhook-url", "");

        suspiciousEnabled = cfg.getBoolean("suspicious.enabled", true);
        creativeGiveWindow = cfg.getInt("suspicious.creative-give-window", 45);
        maxLogsMemory = cfg.getInt("max-logs-memory", 300);
    }

    public boolean isLogCreative() { return logCreative; }
    public Set<String> getMonitoredCommands() { return monitoredCommands; }
    public boolean isDiscordEnabled() { return discordEnabled; }
    public String getDiscordWebhook() { return discordWebhook; }
    public boolean isSuspiciousEnabled() { return suspiciousEnabled; }
    public int getCreativeGiveWindow() { return creativeGiveWindow; }
    public int getMaxLogsMemory() { return maxLogsMemory; }
}