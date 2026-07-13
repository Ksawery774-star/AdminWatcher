package pl.grokdev.adminwatcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.grokdev.adminwatcher.AdminWatcher;
import pl.grokdev.adminwatcher.utils.ConfigManager;
import pl.grokdev.adminwatcher.utils.LogManager;

public class AdminWatcherCommand implements CommandExecutor {

    private final AdminWatcher plugin;
    private final ConfigManager configManager;
    private final LogManager logManager;

    public AdminWatcherCommand(AdminWatcher plugin, ConfigManager configManager, LogManager logManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.logManager = logManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("\u00a7eUżyj: /adminwatcher reload");
            return true;
        }

        if (!sender.hasPermission("adminwatcher.admin")) {
            sender.sendMessage("\u00a7cNie masz uprawnień do tej komendy!");
            return true;
        }

        configManager.reload();
        sender.sendMessage("\u00a7a[AdminWatcher] Config przeładowany pomyślnie!");
        plugin.getLogger().info("Config przeładowany przez " + sender.getName());
        return true;
    }
}