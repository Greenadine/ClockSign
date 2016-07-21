package com.greenadine.clocksign.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.greenadine.clocksign.ClockSaver;
import com.greenadine.clocksign.ClockSign;
import com.greenadine.clocksign.Lang;
import com.greenadine.clocksign.Updater;
import com.greenadine.clocksign.Updater.UpdateResult;
import com.greenadine.clocksign.configuration.SpaceConfig;

public class ClockCommandExecutor implements CommandExecutor {
	private ClockSign m_plugin;
	private String versionID = ClockSign.getVersionID();

	public ClockCommandExecutor(ClockSign plugin) {
		m_plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NOARGS.toString()
								.replaceAll("%versionid%", versionID).replaceAll("%label%", label)));
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("save")) {
					try {
						m_plugin.clocks("save");
					} catch (Exception e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_SAVECLOCKS_FAIL.toString()));
						m_plugin.log.warning(
								"[ClockSign] Failed to save clocks to file! Look for error(s) below! Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
						e.printStackTrace();
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
							+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_SAVECLOCKS_SUCCESS.toString()));
					return true;
				} else if (args[0].equalsIgnoreCase("load")) {
					try {
						m_plugin.clocks("load");
					} catch (Exception e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_LOADCLOCKS_FAIL.toString()));
						m_plugin.log.warning(
								"[ClockSign] Failed to load clocks from file! Look for error(s) below! Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
						e.printStackTrace();
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
							+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_LOADCLOCKS_SUCCESS.toString()));
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					try {
						SpaceConfig.loadConfigs();
						m_plugin.loadLang(m_plugin.getConfig().getString("settings.language") + ".yml");
					} catch (Exception e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_RELOAD_FAIL.toString()));
						m_plugin.log.warning(
								"[ClockSign] Failed to reload configuration & messages.yml! Look for error(s) below! Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
						e.printStackTrace();
						return true;
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
							+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_RELOAD_SUCCESS.toString()));
					return true;
				} else if (args[0].equalsIgnoreCase("update")) {
					if (m_plugin.getConfig().getBoolean("updater.enable") == true) {
						Updater updater = new Updater(m_plugin, 89467, m_plugin.file, Updater.UpdateType.DEFAULT,
								m_plugin.getConfig().getBoolean("settings.logUpdater"));
						updater.getResult();
						if (updater.getResult().equals(UpdateResult.SUCCESS)) {
							sender.sendMessage(
									ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor
											.translateAlternateColorCodes('&', Lang.COMMAND_UPDATE_SUCCESS.toString()));
						} else if (updater.getResult().equals(UpdateResult.NO_UPDATE)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_NO_UPDATE.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_DOWNLOAD)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_DOWNLOAD.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_NOVERSION)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_NOVERSION.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_DBO)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_DBO.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_APIKEY)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_APIKEY.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_BADID)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_BADID.toString()));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_UNKNOWN.toString()));
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_UPDATE_DISABLED.toString()));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(
							ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_HELP_HEADER.toString()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_AVAILABLE_COMMANDS.toString()));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_SAVE_DESC.toString().replaceAll("%label%", label)));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_LOAD_DESC.toString().replaceAll("%label%", label)));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_UPDATE_DESC.toString().replaceAll("%label%", label)));
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_RELOAD_DESC.toString().replaceAll("%label%", label)));
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
							+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_UNKNOWN_SUBCOMMAND.toString()
									.replaceAll("%subcmd%", args[0]).replaceAll("%label%", label)));
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_TOO_MANY_ARGUMENTS.toString().replaceAll("%label%", label)));
				return true;
			}

		} else {
			Player p = (Player) sender;
			Location ploc = p.getLocation();
			if (!hasPerm(p, "clocksign.commands.main")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NO_PERMISSION.toString()));
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NOARGS.toString()
								.replaceAll("%versionid%", versionID).replaceAll("%label%", label)));
				return true;
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("save")) {
					if (hasPerm(p, "clocksign.commands.save") == false) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NO_PERMISSION.toString()));
						return true;
					} else {
						try {
							ClockSaver.write(m_plugin.saveFile, m_plugin.clocks);
						} catch (Exception e) {
							playExplode(p, ploc);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_SAVECLOCKS_FAIL.toString()));
							m_plugin.log.warning(
									"[ClockSign] Failed to save clocks! Check if clocks.cvs exists. Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
							return true;
						}
						playLevelUp(p, ploc);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor
								.translateAlternateColorCodes('&', Lang.COMMAND_SAVECLOCKS_SUCCESS.toString()));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("load")) {
					if (hasPerm(p, "clocksign.commands.load") == false) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NO_PERMISSION.toString()));
						return true;
					} else {
						try {
							m_plugin.clocks = ClockSaver.read(m_plugin.saveFile, m_plugin.getServer());
						} catch (Exception e) {
							playExplode(p, ploc);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_LOADCLOCKS_FAIL.toString()));
							m_plugin.log.warning(
									"[ClockSign] Failed to reload clocks! Check if clocks.cvs exists. Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
							e.printStackTrace();
							return true;
						}
						playLevelUp(p, ploc);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor
								.translateAlternateColorCodes('&', Lang.COMMAND_LOADCLOCKS_SUCCESS.toString()));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (hasPerm(p, "clocksign.commands.reload") == false) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NO_PERMISSION.toString()));
						return true;
					} else {
						try {
							SpaceConfig.loadConfigs();
							m_plugin.loadLang(m_plugin.getConfig().getString("settings.language") + ".yml");
						} catch (Exception e) {
							playExplode(p, ploc);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_RELOAD_FAIL.toString()));
							m_plugin.log.warning(
									"[ClockSign] Failed to reload configuration & messages.yml! Look for error(s) below! Post a ticket on the plugin's BukkitDev page if you got any errors that you cannot fix!");
							e.printStackTrace();
							return true;
						}
						playLevelUp(p, ploc);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_RELOAD_SUCCESS.toString()));
						return true;
					}
				} else if (args[0].equalsIgnoreCase("update")) {
					if (hasPerm(p, "clocksign.commands.update") == false) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_NO_PERMISSION.toString()));
						return true;
					}
					if (m_plugin.getConfig().getBoolean("updater.enable") == true) {
						Updater updater = new Updater(m_plugin, 89467, m_plugin.file, Updater.UpdateType.DEFAULT,
								m_plugin.getConfig().getBoolean("settings.logUpdater"));
						updater.getResult();
						if (updater.getResult().equals(UpdateResult.SUCCESS)) {
							sender.sendMessage(
									ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString()) + ChatColor
											.translateAlternateColorCodes('&', Lang.COMMAND_UPDATE_SUCCESS.toString()));
						} else if (updater.getResult().equals(UpdateResult.NO_UPDATE)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_NO_UPDATE.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_DOWNLOAD)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_DOWNLOAD.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_NOVERSION)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_NOVERSION.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_DBO)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_DBO.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_APIKEY)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_APIKEY.toString()));
						} else if (updater.getResult().equals(UpdateResult.FAIL_BADID)) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_BADID.toString()));
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
									+ ChatColor.translateAlternateColorCodes('&',
											Lang.COMMAND_UPDATE_FAIL_UNKNOWN.toString()));
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
								+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_UPDATE_DISABLED.toString()));
						return true;
					}

				} else if (args[0].equalsIgnoreCase("help")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_HELP_HEADER.toString()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_AVAILABLE_COMMANDS.toString()));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_SAVE_DESC.toString().replaceAll("%label%", label)));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_LOAD_DESC.toString().replaceAll("%label%", label)));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_UPDATE_DESC.toString().replaceAll("%label%", label)));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							Lang.COMMAND_HELP_RELOAD_DESC.toString().replaceAll("%label%", label)));
					return true;
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
							+ ChatColor.translateAlternateColorCodes('&', Lang.COMMAND_UNKNOWN_SUBCOMMAND.toString()
									.replaceAll("%subcmd%", args[0]).replaceAll("%label%", label)));
					return true;
				}
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.PREFIX.toString())
						+ ChatColor.translateAlternateColorCodes('&',
								Lang.COMMAND_TOO_MANY_ARGUMENTS.toString().replaceAll("%label%", label)));
			}
		}
		return false;
	}

	private void playLevelUp(Player player, Location location) {
		if (m_plugin.getConfig().getBoolean("enableSoundEffects") == true) {
			player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
		} else {
			// Do nothing
		}
	}

	private void playExplode(Player player, Location location) {
		if (m_plugin.getConfig().getBoolean("enableSoundEffects") == true) {
			player.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 10, 1);
		} else {
			// Do nothing
		}
	}

	private boolean hasPerm(Player player, String permission) {
		if (player.hasPermission(permission)) {
			return true;
		} else {
			return false;
		}
	}
}
