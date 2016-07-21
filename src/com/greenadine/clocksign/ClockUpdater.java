package com.greenadine.clocksign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.greenadine.clocksign.clocks.Clock;
import com.greenadine.clocksign.clocks.GameClock;
import com.greenadine.clocksign.clocks.PlayerTimeClock;
import com.greenadine.clocksign.clocks.RealClock;

public class ClockUpdater implements Runnable {
	private ClockSign m_plugin;
	
	public ClockUpdater(ClockSign plugin) {
		m_plugin = plugin;
	}
	
	public void run() {
		for (int i = 0; i < m_plugin.clocks.size(); i++) {
			Clock clock = m_plugin.clocks.get(i);
			if (clock.isLoaded()) {
				if (clock.isValid()) {
					clock.update();
				} else {
					if(clock instanceof GameClock) {
						logRemoveToConsole(i, "gameclock", clock);
						logRemoveToOperators(i, "gameclock", clock);
						
						removeClock(i);
						
					} else if(clock instanceof RealClock) {
						logRemoveToConsole(i, "realclock", clock);
						logRemoveToOperators(i, "realclock", clock);
						
						removeClock(i);
						
					} else if(clock instanceof PlayerTimeClock) {
						logRemoveToConsole(i, "playerclock", clock);
						logRemoveToOperators(i, "playerclock", clock);
						
						removeClock(i);
						
					} else {
						logRemoveToConsole(i, "invalid clock", clock);
						logRemoveToOperators(i, "invalid clock", clock);
						
						removeClock(i);
					}
				}
			}
		}
	}
	
	public void removeClock(int i) {
		m_plugin.clocks.remove(i);
		i -= 1;
	}
	
	public void logRemoveToConsole(int i, String clocktype, Clock cblock) {
		Clock clock = m_plugin.clocks.get(i);
		
		if(m_plugin.getConfig().getBoolean("logRemoveToConsole") == true) {
			m_plugin.log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor.translateAlternateColorCodes('&', Lang.LOG_REMOVE_CONSOLE.toString().replaceAll("%clocktype%", clocktype).replaceAll("%clocklabel%", clock.getLabel()).replaceAll("%world%", cblock.getBlock().getWorld().getName())));
		} else {
			//Do nothing.
		}
	}
	
	public void logRemoveToOperators(int i, String clocktype, Clock cblock) {
		Clock clock = m_plugin.clocks.get(i);
		
		if(m_plugin.getConfig().getBoolean("logRemoveToOperators") == true) {
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				if(player.isOp()) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor.translateAlternateColorCodes('&', Lang.LOG_REMOVE_OPERATOR.toString().replaceAll("%clocktype%", clocktype).replaceAll("%clocklabel%", clock.getLabel()).replaceAll("%world%", cblock.getBlock().getWorld().getName())));
				}
			}
		} else {
			//Do nothing.
		}
	}
}
