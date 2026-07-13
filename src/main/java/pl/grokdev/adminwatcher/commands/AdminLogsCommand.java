package pl.grokdev.adminwatcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.grokdev.adminwatcher.utils.LogManager;

public class AdminLogsCommand implements CommandExecutor {

    private final LogManager logs;

    public AdminLogsCommand(LogManager logs) {
        this.logs = logs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int count = 10;
        if (args.length > 0) {
            try {
                count = Math.min(Math.max(Integer.parseInt(args[0]), 1), 50);
            } catch (NumberFormatException ignored) {}
        }

        sender.sendMessage("\u00a7e=== Ostatnie " + count + " logów ===");
        for (String line : logs.getRecent(count)) {
            sender.sendMessage(line);
        }
        return true;
    }
}