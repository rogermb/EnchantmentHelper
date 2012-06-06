package me.firedroide.plugins.EnchantmentHelper;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommandExecutor implements CommandExecutor {
	
	EnchantmentHelper plugin;
	
	public LevelCommandExecutor(EnchantmentHelper plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only Players can use this command.");
			return true;
		}
		
		if (!sender.hasPermission("enchantmenthelper.use")) {
			sender.sendMessage("§4You're not allowed to use this command.");
			return true;
		}
		
		if (args.length == 0) {
			if (!RemoveLevel(sender)) {
				sender.sendMessage("§3Usage: §o/" + label + " LevelNumber§r§3.");
				sender.sendMessage("§3Or:     §o/" + label + " MinimumLevel MaximumLevel§r§3.");
				sender.sendMessage("§3Use    §o/" + label + "§r§3 to reset your echantment request.");
			}
			return true;
		} else if (args.length == 1) {
			try {
				Integer l = Integer.valueOf(args[0]);
				if (l > 0 && l <= 50) {
					Integer[] i = {l};
					SetLevel(sender, i);
				} else {
					RemoveLevel(sender);
				}
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else if (args.length == 2) {
			try {
				Integer m = Math.min(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
				Integer n = Math.max(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
				if ((m > 0 && m <= 50) && (n > 0 && n <= 50) && (m != n)) {
					Integer[] i = {m, n};
					SetLevel(sender, i);
					return true;
				} else {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	public boolean RemoveLevel(CommandSender sender) {
		if (plugin.getLevels().containsKey(sender.getName())) {
			plugin.getLevels().remove(sender.getName());
			sender.sendMessage("§3Your enchantment request has been reset.");
			return true;
		} else {
			return false;
		}
	}
	
	public void SetLevel(CommandSender sender, Integer[] level) {
		if (plugin.getLevels().containsKey(sender.getName())) {
			plugin.getLevels().remove(sender.getName());
			plugin.getLevels().put(((Player) sender).getName(), level);
			if (level.length == 1) {
				sender.sendMessage("§3Your requested level has been updated to exactly " + level[0]);
			} else {
				sender.sendMessage("§3Your requested level has been updated to between " + level[0] + " and " + level[1]);
			}
		} else {
			plugin.getLevels().put(((Player) sender).getName(), level);
			if (level.length == 1) {
				sender.sendMessage("§3Your requested level was set to exactly " + level[0]);
			} else {
				sender.sendMessage("§3Your requested level was set to between " + level[0] + " and " + level[1]);
			}
		}
	}
	
}
