package pl.grokdev.adminwatcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.grokdev.adminwatcher.utils.ConfigManager;

/**
 * Prosta komenda reload.
 */
public class AdminWatcherCommand implements CommandExecutor {

    private final ConfigManager config;

    public AdminWatcherCommand(ConfigManager config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("\u00a7eUżyj: /adminwatcher reload");
            return true;
        }

        if (!sender.hasPermission("adminwatcher.admin")) {
            sender.sendMessage("\u00a7cBrak uprawnień.");
            return true;
        }

        config.reload();
        sender.sendMessage("\u00a7aConfig przeładowany.");
        return true;
    }
}