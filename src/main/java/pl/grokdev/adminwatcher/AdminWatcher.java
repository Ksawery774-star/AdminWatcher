package pl.grokdev.adminwatcher;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminWatcher extends JavaPlugin implements Listener {

    private File logFile;
    private List<String> monitoredCommands;
    private boolean logCreative;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        logFile = new File(getDataFolder(), "admin-logs.log");
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("\u00a7a[AdminWatcher] Plugin włączony! Monitoring administracji aktywny.");
    }

    private void loadConfig() {
        monitoredCommands = getConfig().getStringList("monitored-commands");
        logCreative = getConfig().getBoolean("log-creative", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("adminwatcher")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("adminwatcher.admin")) {
                    sender.sendMessage("\u00a7cNie masz uprawnień!");
                    return true;
                }
                reloadConfig();
                loadConfig();
                sender.sendMessage("\u00a7a[AdminWatcher] Config przeładowany!");
                return true;
            }
            sender.sendMessage("\u00a7eUżyj: /adminwatcher reload");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        if (!logCreative) return;

        Player p = e.getPlayer();
        if (p.hasPermission("adminwatcher.bypass")) return;

        if (e.getNewGameMode() == GameMode.CREATIVE) {
            String msg = String.format("[%s] %s (%s) zmienił GAMEMODE na CREATIVE | Świat: %s | Lok: %d,%d,%d",
                    LocalDateTime.now().format(FORMATTER),
                    p.getName(),
                    p.getUniqueId(),
                    p.getWorld().getName(),
                    p.getBlockX(), p.getBlockY(), p.getBlockZ());

            logToFile(msg);
            Bukkit.getConsoleSender().sendMessage("\u00a7c[ADMIN WATCH] " + msg);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("adminwatcher.bypass")) return;

        String fullCmd = e.getMessage().toLowerCase().trim();
        String cmdName = fullCmd.split(" ")[0].replace("/", "");

        boolean shouldLog = false;
        for (String monitored : monitoredCommands) {
            if (cmdName.equals(monitored.toLowerCase()) || fullCmd.startsWith("/" + monitored.toLowerCase())) {
                shouldLog = true;
                break;
            }
        }

        if (shouldLog) {
            String msg = String.format("[%s] %s (%s) użył: %s | Świat: %s | Lok: %d,%d,%d",
                    LocalDateTime.now().format(FORMATTER),
                    p.getName(),
                    p.getUniqueId(),
                    e.getMessage(),
                    p.getWorld().getName(),
                    p.getBlockX(), p.getBlockY(), p.getBlockZ());

            logToFile(msg);
            Bukkit.getConsoleSender().sendMessage("\u00a7c[ADMIN WATCH] " + msg);
        }
    }

    private void logToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException ex) {
            getLogger().warning("Nie udało się zapisać do pliku logów: " + ex.getMessage());
        }
    }
}