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
        // Ładujemy config i logi – standardowa sprawa
        this.configManager = new ConfigManager(this);
        this.logManager = new LogManager(this, configManager);

        // Rejestrujemy listenery – tu się dzieje magia pilnowania
        getServer().getPluginManager().registerEvents(new CommandListener(this, logManager, configManager), this);
        getServer().getPluginManager().registerEvents(new GameModeListener(this, logManager, configManager), this);

        // Komendy – żeby właściciel mógł szybko sprawdzić co się dzieje
        getCommand("adminwatcher").setExecutor(new AdminWatcherCommand(this, configManager, logManager));
        getCommand("adminlogs").setExecutor(new AdminLogsCommand(logManager));

        // Wiadomość startowa – żeby było widać, że działa
        getLogger().info("\u00a7a[AdminWatcher] v2.1 odpalił się pomyślnie! Pilnuję adminów jak trzeba 🔥");
        getLogger().info("\u00a7e[AdminWatcher] Wpisz /adminlogs żeby zobaczyć co admini ostatnio wyczyniali.");
    }

    @Override
    public void onDisable() {
        if (logManager != null) {
            logManager.saveAll();
        }
        getLogger().info("\u00a7c[AdminWatcher] Plugin się wyłącza... do następnego razu!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}