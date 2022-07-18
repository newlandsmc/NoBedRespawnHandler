package com.semivanilla.nobedrespawnhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

public final class NoBedRespawnHandler extends JavaPlugin implements Listener {
    private File DATA_FOLDER = new File(getDataFolder(), "data/");

    private Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public void onEnable() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        if (!this.DATA_FOLDER.exists())
            this.DATA_FOLDER.mkdir();
        getCommand("setspawnlocationinternal").setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("respawnhandler.setspawnlocationinternal")) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Usage: /setspawnlocationinternal <player>");
            return true;
        }
        String playerName = args[0];
        sender.sendMessage("Setting spawn location for player " + playerName + " to " + sender.getName());
        Player p = getServer().getPlayer(playerName);
        if (p == null) {
            sender.sendMessage("Player " + playerName + " not found!");
            return true;
        }
        p.setBedSpawnLocation(p.getLocation());
        File file = new File(this.DATA_FOLDER, p.getUniqueId() + ".json");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            PrintStream ps = new PrintStream(file);
            ps.print(this.gson.toJson(new StoredLocation(p.getLocation())));
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sender.sendMessage("Spawn location set for player " + playerName + "!");
        return true;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer().getBedSpawnLocation() == null) {
            getLogger().info("Player " + event.getPlayer().getName() + " has no bed!");
            File file = new File(this.DATA_FOLDER, event.getPlayer().getUniqueId() + ".json");
            if (!file.exists()) {
                getLogger().info("Player " + event.getPlayer().getName() + " has no data file, allowing vanilla mc to handle respawn");
                return;
            }
            getLogger().info("Player " + event.getPlayer().getName() + " has data file, loading data");
            try {
                String json = new String(Files.readAllBytes(file.toPath()));
                StoredLocation location = (StoredLocation)this.gson.fromJson(json, StoredLocation.class);
                Location l = location.getLocation();
                getLogger().info("Setting respawn location to " + l);
                event.setRespawnLocation(l);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
