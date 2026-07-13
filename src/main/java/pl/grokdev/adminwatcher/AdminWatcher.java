package pl.grokdev.adminwatcher;

import org.bukkit.plugin.java.JavaPlugin;
import pl.grokdev.adminwatcher.commands.AdminLogsCommand;
import pl.grokdev.adminwatcher.commands.AdminWatcherCommand;
import pl.grokdev.adminwatcher.listeners.CommandListener;
import pl.grokdev.adminwatcher.listeners.GameModeListener;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

/**
 * Główna klasa pluginu.
 * Prosta, czytelna, bez kombinowania.
 */
public class AdminWatcher extends JavaPlugin {

    private ConfigManager configManager;
    private LogManager logManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        logManager = new LogManager(this, configManager);

        // Rejestrujemy listenery
        getServer().getPluginManager().registerEvents(
                new CommandListener(this, logManager, configManager), this);
        getServer().getPluginManager().registerEvents(
                new GameModeListener(logManager, configManager), this);

        // Komendy
        getCommand("adminwatcher").setExecutor(new AdminWatcherCommand(configManager));
        getCommand("adminlogs").setExecutor(new AdminLogsCommand(logManager));

        getLogger().info("[AdminWatcher] v2.2 uruchomiony. Pilnuje adminów.");
    }

    @Override
    public void onDisable() {
        if (logManager != null) {
            logManager.flush();
        }
        getLogger().info("[AdminWatcher] Wyłączony.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}