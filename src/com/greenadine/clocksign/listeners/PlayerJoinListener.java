package com.greenadine.clocksign.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.greenadine.clocksign.ClockSign;
import com.greenadine.clocksign.Lang;

public class PlayerJoinListener implements Listener {
	
	private ClockSign m_plugin;
	
	public PlayerJoinListener(ClockSign plugin) {
		this.m_plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.getPlayer().isOp()) {
			if(m_plugin.getConfig().getBoolean("settings.operatorJoinMessage") == true) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_HEADER.toString()));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_VERSION.toString().replaceAll("%versionid%", ClockSign.getVersionID())));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_MCVERSION.toString().replaceAll("%mcversionid%", ClockSign.getMinecraftID())));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_CBVERSION.toString().replaceAll("%cbversionid%", ClockSign.getCraftbukkitID())));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PLAYER_JOIN_HEADER.toString()));
			} else {
				//Do nothing.
			}
		} else {
			// Do nothing.
		}
	}
	
}
