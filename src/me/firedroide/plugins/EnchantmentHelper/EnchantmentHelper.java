package me.firedroide.plugins.EnchantmentHelper;

import java.util.HashMap;
import java.util.logging.Logger;

import me.firedroide.plugins.EnchantmentHelper.util.ConfigReader;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentHelper extends JavaPlugin {
	
	EnchantmentListener enchantListener;
	HashMap<String, Runnable> threads;
	HashMap<String, Integer[]> levels;
	Logger logger;
	ConfigReader cr;
	LevelCommandExecutor lcl;
	
	Integer delay;
	Boolean messages;
	Boolean reset;
	Integer attempts;
	
	public void onEnable() {
		logger = this.getLogger();
		enchantListener = new EnchantmentListener(this);
		getServer().getPluginManager().registerEvents(enchantListener, this);
		threads = new HashMap<String, Runnable>();
		levels = new HashMap<String, Integer[]>();
		
		lcl = new LevelCommandExecutor(this);
		this.getCommand("enchantinglevel").setExecutor(lcl);
		
		cr = new ConfigReader(this);
		readConfig();
	}
	
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		threads.clear();
		levels.clear();
	}
	
	public void readConfig() {
		if (this.getConfig().getKeys(true).size() == 0) {
			this.saveResource("config.yml", false);
			logger.info("Default config created!");
			this.reloadConfig();
		}
		this.getConfig().setDefaults(new MemoryConfiguration());
		
		delay = cr.getInteger("main", "EnchantingDelay", 400);
		messages = cr.getBoolean("main", "ConsoleMessages", false);
		reset = cr.getBoolean("main", "ResetAfterEnchant", true);
		attempts = cr.getInteger("main", "MaximumEnchantmentAttempts", 1000);
		
		if (cr.gotErrors()) this.saveConfig();
	}
	
	public HashMap<String, Runnable> getThreads() {
		return threads;
	}
	
	public Runnable getThread(String player) {
		return threads.get(player);
	}
	
	public HashMap<String, Integer[]> getLevels() {
		return levels;
	}
	
	public Integer getMinLevel(String player) {
		return levels.get(player)[0];
	}
	
	public Integer getMaxLevel(String player) {
		if (levels.get(player).length == 2) {
			return levels.get(player)[1];
		} else {
			return levels.get(player)[0];
		}
	}
	
	public Integer getDelay() {
		return delay;
	}
	
	public Boolean getSendMessages() {
		return messages;
	}
	
	public Boolean getReset() {
		return reset;
	}
	
	public Integer getMaxAttempts() {
		return attempts;
	}
	
}
