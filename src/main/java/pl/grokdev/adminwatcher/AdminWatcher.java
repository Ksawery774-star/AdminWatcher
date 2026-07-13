package pl.grokdev.adminwatcher;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.grokdev.adminwatcher.commands.AdminWatcherCommand;
import pl.grokdev.adminwatcher.commands.AdminLogsCommand;
import pl.grokdev.adminwatcher.listeners.CommandListener;
import pl.grokdev.adminwatcher.listeners.GameModeListener;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

public class AdminWatcher extends JavaPlugin {

    private ConfigManager configManager;
    private LogManager logManager;

    @Override
    public void onEnable() {
        // Inicjalizacja
        this.configManager = new ConfigManager(this);
        this.logManager = new LogManager(this, configManager);

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new CommandListener(this, logManager, configManager), this);
        getServer().getPluginManager().registerEvents(new GameModeListener(this, logManager, configManager), this);

        // Rejestracja komend
        getCommand("adminwatcher").setExecutor(new AdminWatcherCommand(this, configManager, logManager));
        getCommand("adminlogs").setExecutor(new AdminLogsCommand(logManager));

        getLogger().info("\u00a7a[AdminWatcher] v2.0 włączony! Zaawansowane monitorowanie administracji aktywne.");
        getLogger().info("\u00a7e[AdminWatcher] Użyj /adminlogs aby zobaczyć ostatnie akcje adminów.");
    }

    @Override
    public void onDisable() {
        if (logManager != null) {
            logManager.saveAll();
        }
        getLogger().info("\u00a7c[AdminWatcher] Plugin wyłączony.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}