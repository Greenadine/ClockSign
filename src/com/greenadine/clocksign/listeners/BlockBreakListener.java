package com.greenadine.clocksign.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.permissions.Permission;

import com.greenadine.clocksign.ClockSign;
import com.greenadine.clocksign.Lang;

public class BlockBreakListener implements Listener {
	
	private ClockSign m_plugin;
	
	public BlockBreakListener(ClockSign m_plugin) {
		this.m_plugin = m_plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		
		if(b.getType() == Material.SIGN || b.getType() == Material.WALL_SIGN) {
			Sign s = (Sign) b;
			if(m_plugin.clocks.contains(s)) {
				if(!p.hasPermission(new Permission("clocksign.clocks.break"))) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor.translateAlternateColorCodes('&', Lang.CLOCK_BREAK_NO_PERMISSION.toString()));
				} else {
					if(p.isSneaking()) {
						e.setCancelled(false);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor.translateAlternateColorCodes('&', Lang.CLOCK_BREAK_REMOVED.toString()));
					} else {
						e.setCancelled(true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor.translateAlternateColorCodes('&', Lang.CLOCK_BREAK_SNEAK.toString()));
					}
				}
			} else {
				//Do nothing.
			}
		}
	}
	
}
