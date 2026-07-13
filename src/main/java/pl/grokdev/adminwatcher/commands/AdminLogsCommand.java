package pl.grokdev.adminwatcher.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.grokdev.adminwatcher.utils.LogManager;

public class AdminLogsCommand implements CommandExecutor {

    private final LogManager logManager;

    public AdminLogsCommand(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int amount = 10;
        if (args.length > 0) {
            try {
                amount = Integer.parseInt(args[0]);
                if (amount < 1) amount = 10;
                if (amount > 50) amount = 50;
            } catch (NumberFormatException ignored) {}
        }

        sender.sendMessage("\u00a7e=== Ostatnie " + amount + " logów administracji ===");
        for (String log : logManager.getRecentLogs(amount)) {
            sender.sendMessage(log);
        }
        sender.sendMessage("\u00a77Pełne logi w pliku: plugins/AdminWatcher/admin-logs.log");
        return true;
    }
}