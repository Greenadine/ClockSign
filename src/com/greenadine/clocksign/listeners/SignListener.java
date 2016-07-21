package com.greenadine.clocksign.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.greenadine.clocksign.ClockSign;
import com.greenadine.clocksign.Lang;
import com.greenadine.clocksign.clocks.GameClock;
import com.greenadine.clocksign.clocks.PlayerTimeClock;
import com.greenadine.clocksign.clocks.RealClock;

public class SignListener implements Listener {
	protected ClockSign m_plugin;
	
	public SignListener(ClockSign plugin) {
		this.m_plugin = plugin;
	}

	@EventHandler
	public void blockEvent(SignChangeEvent event) {
		String[] lines = event.getLines();
		Player p = (Player) event.getPlayer();
		World world = event.getBlock().getWorld();
		
		if (lines[0].equals("[gameclock]")) {
			if(!p.hasPermission("clocksign.clocks.game")) {
				msgDenyToPlayer(p, "gameclock");
				notifyDenyToConsole(p, "gameclock", world);
				notifyDenyToOperators(p, "gameclock", world);
				
				event.getBlock().breakNaturally();
			} else {
				GameClock clock = new GameClock(event.getBlock());
				clock.setLabel(lines[1]);
				
				clock.setFormat(lines[3]);
				
				m_plugin.addClock(clock);
				
				msgCreateToPlayer(p, "gameclock");
				notifyCreateToConsole(p, "gameclock", world);
				notifyCreateToOperators(p, "gameclock", world);
			}
			
		} else if (lines[0].equals("[realclock]")) {
			if(!p.hasPermission("clocksign.clocks.real")) {
				msgDenyToPlayer(p, "realclock");
				notifyDenyToConsole(p, "realclock", world);
				notifyDenyToOperators(p, "realclock", world);
				
				event.getBlock().breakNaturally();
			} else {
				RealClock clock = new RealClock(event.getBlock());
				clock.setLabel(lines[1]);
				clock.setTimeZone(lines[2]);
				clock.setFormat(lines[3]);

				m_plugin.addClock(clock);
				
				msgCreateToPlayer(p, "realclock");
				notifyCreateToConsole(p, "realclock", world);
				notifyCreateToOperators(p, "realclock", world);
			}
			
		} else if (lines[0].equals("[playerclock]")) {
			if(!p.hasPermission("clocksign.clocks.player")) {
				msgDenyToPlayer(p, "playerclock");
				notifyDenyToConsole(p, "playerclock", world);
				notifyDenyToOperators(p, "playerclock", world);
				
				event.getBlock().breakNaturally();
			} else {
				PlayerTimeClock clock = new PlayerTimeClock(event.getBlock());
				clock.setLabel(lines[1]);
				
				clock.setFormat(lines[3]);

				m_plugin.addClock(clock);
				
				msgCreateToPlayer(p, "playerclock");
				notifyCreateToConsole(p, "playerclock", world);
				notifyCreateToOperators(p, "playerclock", world);
			}
			
		} else {
			//Do nothing.
		}
	}
	
	public void notifyDenyToConsole(Player p, String clocktype, World world) {
		if(m_plugin.getConfig().getBoolean("notifyDenyToConsole") == true) {
			m_plugin.log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) 
					+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_DENY_PLACE_CONSOLE.toString().replaceAll("%clocktype%", clocktype).replaceAll("%player%", p.getName()).replaceAll("%world%", world.getName())));
			
		} else {
			//Do not notify.
		}
	}
	
	public void notifyDenyToOperators(Player p, String clocktype, World world) {
		if(m_plugin.getConfig().getBoolean("notifyDenyToOperator") == true) {
			for (Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.isOp()) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())  
							+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_DENY_PLACE_OPERATOR.toString().replaceAll("%clocktype%", clocktype).replaceAll("%player%", p.getName()).replaceAll("%world%", world.getName())));
				}
			}
		} else {
			//Do not notify.
		}
	}
	
	
	public void notifyCreateToConsole(Player p, String clocktype, World world) {
		if(m_plugin.getConfig().getBoolean("notifyCreateToConsole") == true) {
			m_plugin.log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) 
					+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_CREATE_CONSOLE.toString().replaceAll("%clocktype%", clocktype).replaceAll("%player%", p.getName()).replaceAll("%world%", world.getName())));
		} else {
			//Do not notify.
		}
	}
	
	public void notifyCreateToOperators(Player p, String clocktype, World world) {
		if(m_plugin.getConfig().getBoolean("notifyCreateToOperators") == true) {
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				if(player.isOp()) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) 
							+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_CREATE_OPERATOR.toString().replaceAll("%clocktype%", clocktype).replaceAll("%player%", p.getName()).replaceAll("%world%", world.getName())));
				} else {
					//Do not notify.
				}
			}
		}
	}
	
	public void msgCreateToPlayer(Player p, String clocktype) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) 
				+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_CREATE_PLAYER.toString().replaceAll("%clocktype%", ("" + clocktype.charAt(0)).toUpperCase() + clocktype.substring(1))));
	}
	
	public void msgDenyToPlayer(Player p, String clocktype) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) 
				+ ChatColor.translateAlternateColorCodes('&', Lang.LOG_DENY_PLACE_PLAYER.toString().replaceAll("%clocktype%", clocktype)));
		
	}
	
}
