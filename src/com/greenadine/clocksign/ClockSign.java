package com.greenadine.clocksign;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.greenadine.clocksign.Updater.UpdateResult;
import com.greenadine.clocksign.clocks.Clock;
import com.greenadine.clocksign.clocks.GameClock;
import com.greenadine.clocksign.clocks.PlayerTimeClock;
import com.greenadine.clocksign.clocks.RealClock;
import com.greenadine.clocksign.commands.ClockCommandExecutor;
import com.greenadine.clocksign.configuration.SpaceConfig;
import com.greenadine.clocksign.configuration.SpaceConfig.ConfigFile;
import com.greenadine.clocksign.listeners.PlayerJoinListener;
import com.greenadine.clocksign.listeners.SignListener;

public class ClockSign extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public ArrayList<Clock> clocks = new ArrayList<Clock>(); // A list of all
																// placed clocks
	public String saveFile = "plugins/ClockSign/clocks.csv";
	
	public static String defaultTimeFormat = "HH:mm";

	public File file = this.getFile();

	private static String name = "ClockSign";
	private static String versionID = "1.8";
	private static String mcID = "1.10.2";
	private static String craftbukkitID = "CB 1.10.2-R0.1-SNAPSHOT";
	
	public static YamlConfiguration LANG;
	public static File LANG_FILE;



	private static Plugin plugin;

	public static Plugin getPlugin() {
		return plugin;
	}

	@SuppressWarnings("deprecation")
	public void onEnable() {
		//////
		// Load language file
		File lang = new File(getDataFolder(), "lang" + File.separator + getConfig().getString("settings.language") + ".yml");
		File defaultLang = new File(getDataFolder(), "lang" + File.separator + "en_US.yml");
		if(!lang.exists()) {
			log.severe("[ClockSign] Language file '" + getConfig().getString("settings.languageFile") + "' doesn't exist. Attempting to use default 'en_US.yml' language file...");
			if(defaultLang.exists()) {
				log.severe("[ClockSign] Default language file not found! Plugin cannot be enabled if language file doesn't exist. Please re-download the plugin to restore the language file.");
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			} else {
				log.info("[ClockSign] Default language file found. Continuing plugin startup...");
				loadLang("en_US.yml");
			}
		} else {
			log.info("[ClockSign] Using locale '" + getConfig().getString("settings.language") + "'."); 
			loadLang(getConfig().getString("settings.language") + ".yml");
		}
		
		//////
		// Configuration file
		loadConfiguration();
		
		//////
		// Read configuration file
		PluginConfigurator.readConfig("plugins/ClockSign/config.yml", this);	

		//////
		// Set up the plug-in directory if needed.
		File save = new File(saveFile);

		if (!save.exists()) {
			if (!save.getParentFile().exists()) {
				if (save.getParentFile().mkdirs()) {
					log.info("[ClockSign] Settings directory not found, creating...");
					log.info("[ClockSign] Created settings directory!");
				} else {
					log.info("[ClockSign] Settings directory not found, creating...");
					log.info("[ClockSign] Failed to create settings directory!");
				}
			} else {
				log.info(
						"[ClockSign] File will be created when '/clocksign save' is issued or at server shutdown.");
			}
		} else {
			clocks = ClockSaver.read(saveFile, getServer());
			log.info("[ClockSign] Successfully loaded clocks.csv!");
		}

		//////
		// Setup commands
		getCommand("clocksign").setExecutor(new ClockCommandExecutor(this));

		//////
		// Attach the listeners
		SignListener signListener = new SignListener(this);
		getServer().getPluginManager().registerEvents(signListener, this);

		PlayerJoinListener joinListener = new PlayerJoinListener(this);
		getServer().getPluginManager().registerEvents(joinListener, this);

		// 0 is the initial delay and 16.6 is interval.
		getServer().getScheduler().scheduleAsyncRepeatingTask(this, new ClockUpdater(this), 0, (long) 16.6);

		consoleMessage(ChatColor.GREEN + "Enabled " + name + " plugin version " + ChatColor.RED + getVersionID()
				+ ChatColor.GREEN + " for CraftBukkit version " + ChatColor.RED + craftbukkitID + ChatColor.GREEN
				+ "!");

		//////
		// Check for updates
		if(getConfig().getBoolean("updater.enable") == true) {
			Updater updater = new Updater(this, 89467, file, Updater.UpdateType.DEFAULT,
					getConfig().getBoolean("updater.logConsole"));
			updater.getResult();
			if (updater.getResult().equals(UpdateResult.SUCCESS)) {
				log.info(
						ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor
								.translateAlternateColorCodes('&', Lang.COMMAND_UPDATE_SUCCESS.toString()));
			} else if (updater.getResult().equals(UpdateResult.NO_UPDATE)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_NO_UPDATE.toString()));
			} else if (updater.getResult().equals(UpdateResult.FAIL_DOWNLOAD)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_DOWNLOAD.toString()));
			} else if (updater.getResult().equals(UpdateResult.FAIL_NOVERSION)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_NOVERSION.toString()));
			} else if (updater.getResult().equals(UpdateResult.FAIL_DBO)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_DBO.toString()));
			} else if (updater.getResult().equals(UpdateResult.FAIL_APIKEY)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_APIKEY.toString()));
			} else if (updater.getResult().equals(UpdateResult.FAIL_BADID)) {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_BADID.toString()));
			} else {
				log.info(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_UPDATE_FAIL_UNKNOWN.toString()));
			}
		} else {
			//Do nothing: don't check for any updates.
		}
		
	}

	public void onDisable() {
		//////
		// Save all clocks.
		log.info("[ClockSign] Saving all clocks to file...");
		try {
			ClockSaver.write(saveFile, clocks);
		} catch (Exception e) {
			log.info("[ClockSign] Couldn't write clocks to file! ");
			e.printStackTrace();
		}
		log.info("[ClockSign] Saved all clocks to file!");
		log.info("[ClockSign] ClockSign v" + getVersionID() + " disabled!");
	}

	public void addClock(RealClock clock) {
		clocks.add(clock);
	}

	public void addClock(GameClock clock) {
		clocks.add(clock);
	}

	public void addClock(PlayerTimeClock clock) {
		clocks.add(clock);
	}

	//////
	// Load/Setup config.yml.
	public void loadConfiguration() {
		FileConfiguration config = getConfig();
		config.options().header(
				"Settings for the ClockSign plugin. For information about the configuration, please visit the plugin's BukkitDev page: http://dev.bukkit.org/bukkit-plugins/clocksign.");
		config.addDefault("settings.language", "en_US");
		config.addDefault("settings.defaultTimeFormat", "HH:mm");
		config.addDefault("settings.notifyDenyToConsole", Boolean.valueOf(true));
		config.addDefault("settings.notifyDenyToOperators", Boolean.valueOf(true));
		config.addDefault("settings.notifyRemoveToConsole", Boolean.valueOf(true));
		config.addDefault("settings.notifyRemoveToOperators", Boolean.valueOf(true));
		config.addDefault("settings.enableSoundEffects", Boolean.valueOf(true));
		config.addDefault("settings.operatorJoinMessage", Boolean.valueOf(true));
		config.addDefault("updater.enable", Boolean.valueOf(false));
		config.addDefault("updater.logConsole", Boolean.valueOf(true));
		config.options().copyDefaults(true);
		config.options().copyHeader(true);
		saveConfig();
	}

	public void clocks(String type) {
		if (type.equals("save")) {
			try {
				ClockSaver.write(saveFile, clocks);
			} catch (Exception e) {
				log.warning("Could not save clocks to file!");
			}
		} else if (type.equals("load")) {
			try {
				ClockSaver.read(saveFile, getServer());
			} catch (Exception e) {
				log.warning("Could not load clocks from file!");
			}
		} else {
			// Do nothing
		}
	}

	public void loadConfigurationFiles() {
		try {
			SpaceConfig.loadConfig(ConfigFile.CONFIG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void consoleMessage(String message) {
		this.getServer().getConsoleSender().sendMessage(message);
	}

	//Get info methods
	public static String getPluginName() {
		return name;
	}
	
	public static String getVersionID() {
		return versionID;
	}	

	public static String getMinecraftID() {
		return mcID;
	}
	
	public static String getCraftbukkitID() {
		return craftbukkitID;
	}
	
	//Lang methods
	@SuppressWarnings("deprecation")
	public void loadLang(String file) {
		File lang = new File(getDataFolder(), "lang" + File.separator + file);
		
		if (!lang.exists()) {
			try {
				getDataFolder().mkdir();
				lang.createNewFile();
				InputStream defConfigStream = this.getResource("en_US.yml");
				if (defConfigStream != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
					defConfig.save(lang);
					Lang.setFile(defConfig);
					return;
				}
			} catch(IOException e) {
				e.printStackTrace(); // So they notice
				log.severe("[ClockSign] Couldn't create language file.");
				log.severe("[ClockSign] This is a fatal error. Now disabling");
				this.setEnabled(false); // Without it loaded, we can't send them messages
			}
		}
		
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for(Lang item:Lang.values()) {
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		
		Lang.setFile(conf);
		LANG = conf;
		LANG_FILE = lang;
		try {
			conf.save(getLangFile());
		} catch(IOException e) {
			log.log(Level.WARNING, "ClockSign: Failed to save " + getConfig().getString("settings.language") + ".yml.");
			log.log(Level.WARNING, "ClockSign: Report this stack trace to the plugin developer.");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the lang.yml config.
	 * @return The lang.yml config.
	*/
	
	public YamlConfiguration getLang() {
		return LANG;
	}
	
	/**
	 * Get the lang.yml file.
	 * @return The lang.yml file.
	*/
	
	public File getLangFile() {
		return LANG_FILE;
	}
}
