package com.ezyro.emanhmc.duppatch;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Main extends JavaPlugin implements Listener {
    private ArrayList<Player> players;
    private ProtocolManager protclmgr;
    public Main() {}
    
    public void onEnable() {
        players = new ArrayList();
        
        if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            protclmgr = ProtocolLibrary.getProtocolManager();
            
            protclmgr.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.AUTO_RECIPE }) {
                public void onPacketReceiving(PacketEvent e) {
                    Player p = e.getPlayer();
                    if (!players.contains(p))
                    players.add(p);
                }
            });
        } else {
            getLogger().warning("ProtocolLib doesn't exist! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Bukkit.getPluginManager().registerEvents(this, this);
    }
@EventHandler(ignoreCancelled=true)
public void onItemDrop(PlayerPickupItemEvent e) {
    Player p = e.getPlayer();
    if (players.contains(p))
    e.setCancelled(true);
}
@EventHandler(ignoreCancelled=true)
public void onInventoryClose(InventoryCloseEvent e) {
    HumanEntity he = e.getPlayer();
    if ((he instanceof Player)) {
        Player p = (Player) he;
        if (players.contains(p)) {
            players.remove(p);
        }
    }
}
}
