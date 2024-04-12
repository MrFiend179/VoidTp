package mc.flubel.voidtp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class VoidTp extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("---------------------------------------------");
        getLogger().info("-                                           -");
        getLogger().info("-           VoidTP has started              -");
        getLogger().info("-             Made by Fiend                 -");
        getLogger().info("-                                           -");
        getLogger().info("---------------------------------------------");

        // Register the event listener
        getServer().getPluginManager().registerEvents(this, this);

        // Load config
        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("---------------------------------------------");
        getLogger().info("-                                           -");
        getLogger().info("-           VoidTP has stopped              -");
        getLogger().info("-             Made by Fiend                 -");
        getLogger().info("-                                           -");
        getLogger().info("---------------------------------------------");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Command to set spawn location
        if (label.equalsIgnoreCase("voidtp") && args.length > 0 && args[0].equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getLocation();
                config.set("spawnLocation", location.serialize());
                saveConfig();
                player.sendMessage("Spawn location set to your current location.");
            } else {
                sender.sendMessage("You must be a player to use this command!");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        // Check if the player falls into the void in the specified world
        if (event.getTo().getY() < 0 && world.getName().equals(getConfig().getString("spawnLocation.world"))) {
            Location spawnLocation = Location.deserialize(getConfig().getConfigurationSection("spawnLocation").getValues(false));
            player.teleport(spawnLocation); // Teleport player to spawn
        }
    }

    private void loadConfig() {
        // Create config file if it doesn't exist
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        // Load config
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void saveConfig() {
        // Save config
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().warning("Error saving config.yml!");
        }
    }

}
