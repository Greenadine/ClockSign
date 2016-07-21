package com.greenadine.clocksign;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
	PREFIX("prefix", "&9[ClockSign]"),
	COMMAND_NOARGS("command-no-args", "&fClockSign plugin version %versionid% made by Kevinzuman22. Type &7/%label% help &fFor a list of commands."),
	COMMAND_SAVECLOCKS_FAIL("command-saveclocks-fail", "&cFailed to save clocks to file. See console for information."),
	COMMAND_SAVECLOCKS_SUCCESS("command-saveclocks-success", "&fSuccessfuly saved clocks to file!"),
	COMMAND_LOADCLOCKS_FAIL("command-loadclocks-fail", "&cFailed to load clocks from file! See console for information."),
	COMMAND_LOADCLOCKS_SUCCESS("command-loadclocks-success", "&fSucessfuly loaded clocks from file!"),
	COMMAND_RELOAD_FAIL("command-reload-fail", "&fFailed to reload plugin! See console for information."),
	COMMAND_RELOAD_SUCCESS("command-reload-success", "&fSuccessfuly reloaded plugin!"),
	COMMAND_UPDATE_SUCCESS("command-update-success", "&fAn update has been found. Restart or reload the server to update the plugin."),
	COMMAND_UPDATE_NO_UPDATE("command-update-no-update", "&fNo update was found."),
	COMMAND_UPDATE_FAIL_DOWNLOAD("command-update-fail-download", "&cCould not download new update! Check for internet connection."),
	COMMAND_UPDATE_FAIL_NOVERSION("command-update-fail-noversion", "&cUnable to detect newest version on BukkitDev. Report this to the plugin developer."),
	COMMAND_UPDATE_FAIL_DBO("command-update-fail-dbo", "&cUnable to reach BukkitDev. No internet connection/DevBukkit website is offline."),
	COMMAND_UPDATE_FAIL_APIKEY("command-update-fail-apikey", "&cInvalid apikey defined in config. Check apikey for typo's etc.."),
	COMMAND_UPDATE_FAIL_BADID("command-update-fail-badid", "&cInvalid resource ID. Report this to the plugin developer."),
	COMMAND_UPDATE_FAIL_UNKNOWN("command-update-fail-unknown", "&cUnknown error. Report this to the plugin developer."),
	COMMAND_UPDATE_DISABLED("command-update-disabled", "&eUpdater has been disabled. Enable it in the plugin's config file."),
	COMMAND_HELP_HEADER("command-help-header", "&9&m-->&r &fClockSign Help &9&m<--"),
	COMMAND_HELP_AVAILABLE_COMMANDS("command-help-available-commands", "&fAvailable commands:"),
	COMMAND_HELP_SAVE_DESC("command-help-save-desc", "&7/%label% save - &fSave all clocks to file."),
	COMMAND_HELP_LOAD_DESC("command-help-load-desc", "&7/%label% load - &fLoad all clocks from file."),
	COMMAND_HELP_UPDATE_DESC("command-help-update-desc", "&7/%label% update - &fCheck for updates, and install them."),
	COMMAND_HELP_RELOAD_DESC("command-help-reload-desc", "&7/%label% reload - &fReload plugin & clocks."),
	COMMAND_UNKNOWN_SUBCOMMAND("command-unknown-subcommand", "&c'%subcmd%' is not a recognized subcommand! Use &6/%label% help &cfor a list of subcommands."),
	COMMAND_TOO_MANY_ARGUMENTS("command-too-many-arguments", "&cToo many arguments. Usage: &6/%label% [subcommand]&c."),
	COMMAND_NO_PERMISSION("command-no-permission", "&cYou have no permission to preform this action."),
	CLOCK_BREAK_NO_PERMISSION("clock-break-no-permission", "&cNo permission to break clocks!"),
	CLOCK_BREAK_REMOVED("clock-break-removed", "&fClock removed!"),
	CLOCK_BREAK_SNEAK("clock-break-sneak", "&fSneak to remove clocks."),
	LOG_REMOVE_CONSOLE("log-remove-console", "Removing %clocktype% with label '%clocklabel%' in world '%world%'."),
	LOG_REMOVE_OPERATOR("log-remove-operator", "&eRemoving %clocktype% with label '%clocklabel%' in world '%world%'."),
	LOG_INVALID_CONSOLE("log-invalid-clock-console", "Removed invalid clock with label '%clocklabel%' in world '%world%'."),
	LOG_INVALID_OPERATOR("log-invalid-clock-operator", "&eRemoved invalid clock with label '%clocklabel%' in world '%world%'."),
	LOG_DENY_PLACE_CONSOLE("log-deny-place-console", "Denied %clocktype% creation for %player% in world '%world%' for reason: No permission."),
	LOG_DENY_PLACE_OPERATOR("log-deny-place-operator", "&eDenied %clocktype% creation for %player% in world '%world%' for reason: &cNo permission&r."),
	LOG_DENY_PLACE_PLAYER("log-deny-place-player", "&cYou are not allowed to create a %clocktype%!"),
	LOG_CREATE_CONSOLE("log-create-console", "Player %player% created a %clocktype% in world %world%."),
	LOG_CREATE_OPERATOR("log-create-operator", "&ePlayer %player% created a %clocktype% in world %world%."),
	LOG_CREATE_PLAYER("log-create-player", "&fCreated a new %clocktype%!"),
	PLAYER_JOIN_HEADER("player-join-header","&9&m----------->&r &fClockSign &9&m<-----------"),
	PLAYER_JOIN_VERSION("player-joinnotification-version", "&fVersion: &6%versionid%&f."),
	PLAYER_JOIN_MCVERSION("player-joinnotification-mcversion", "&fFor Minecraft: &6%mcversionid%&f."),
	PLAYER_JOIN_CBVERSION("player-joinnotification-cbversion", "&fCraftbukkit: &6%cbversionid%&f.");

	private String path;
	private String def;
	private static YamlConfiguration LANG;

	/**
	 * Lang enum constructor.
	 * 
	 * @param path
	 *            The string path.
	 * @param start
	 *            The default string.
	 **/

	Lang(String path, String start) {
		this.path = path;
		this.def = start;
	}

	/**
	 * Set the {@code YamlConfiguration} to use.
	 * 
	 * @param config
	 *            The config to set.
	 **/

	public static void setFile(YamlConfiguration config) {
		LANG = config;
	}

	@Override
	public String toString() {
		if (this == PREFIX)
			return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " ";
		return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
	}

	/**
	 * Get the default value of the path.
	 * 
	 * @return The default value of the path.
	 **/

	public String getDefault() {
		return this.def;
	}

	/**
	 * Get the path to the string.
	 * 
	 * @return The path to the string.
	 **/

	public String getPath() {
		return this.path;
	}
}
