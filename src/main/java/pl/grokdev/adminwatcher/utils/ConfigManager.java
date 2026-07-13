package pl.grokdev.adminwatcher.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    private boolean logCreative;
    private List<String> monitoredCommands;
    private boolean discordEnabled;
    private String discordWebhook;
    private boolean suspiciousEnabled;
    private int creativeGiveWindow;
    private int maxLogsInMemory;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        this.logCreative = config.getBoolean("log-creative", true);
        this.monitoredCommands = config.getStringList("monitored-commands");
        this.discordEnabled = config.getBoolean("discord.enabled", false);
        this.discordWebhook = config.getString("discord.webhook-url", "");
        this.suspiciousEnabled = config.getBoolean("suspicious-activity.enabled", true);
        this.creativeGiveWindow = config.getInt("suspicious-activity.creative-give-window-seconds", 30);
        this.maxLogsInMemory = config.getInt("max-logs-in-memory", 500);
    }

    public boolean isLogCreative() { return logCreative; }
    public List<String> getMonitoredCommands() { return monitoredCommands; }
    public boolean isDiscordEnabled() { return discordEnabled; }
    public String getDiscordWebhook() { return discordWebhook; }
    public boolean isSuspiciousEnabled() { return suspiciousEnabled; }
    public int getCreativeGiveWindow() { return creativeGiveWindow; }
    public int getMaxLogsInMemory() { return maxLogsInMemory; }
}