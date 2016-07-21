package com.greenadine.clocksign;

import org.bukkit.configuration.file.YamlConfiguration;

public class PluginConfigurator {
	public static void readConfig(String filename, ClockSign plugin) {
		try {
			YamlConfiguration config = new YamlConfiguration();
			config.load(filename);
			ClockSign.defaultTimeFormat = config.getString("defaultTimeFormat", ClockSign.defaultTimeFormat);
			
		} catch (Exception e) {
			plugin.log.warning("[ClockSign] Could not read config file!");
			plugin.log.warning(e.getMessage());
		}
	}
}
