package me.firedroide.plugins.EnchantmentHelper;

import java.util.HashMap;
import java.util.logging.Logger;

import me.firedroide.plugins.EnchantmentHelper.util.ConfigReader;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentHelper extends JavaPlugin {
	
	EnchantmentListener EnchantListener;
	HashMap<String, Thread> Threads;
	HashMap<String, Integer[]> Levels;
	Logger logger;
	ConfigReader cr;
	LevelCommandExecutor lcl;
	
	Integer delay;
	Boolean messages;
	Boolean reset;
	Integer attempts;
	
	public void onEnable() {
		logger = this.getLogger();
		EnchantListener = new EnchantmentListener(this);
		getServer().getPluginManager().registerEvents(EnchantListener, this);
		Threads = new HashMap<String, Thread>();
		Levels = new HashMap<String, Integer[]>();
		
		lcl = new LevelCommandExecutor(this);
		this.getCommand("enchantinglevel").setExecutor(lcl);
		
		cr = new ConfigReader(this);
		readConfig();
	}
	
	public void onDisable() {
		for (String key : Threads.keySet()) {
			Threads.get(key);
			Threads.remove(key);
		}
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
	
	public HashMap<String, Thread> getThreads() {
		return Threads;
	}
	
	public Thread getThread(String player) {
		return Threads.get(player);
	}
	
	public HashMap<String, Integer[]> getLevels() {
		return Levels;
	}
	
	public Integer getMinLevel(String player) {
		return Levels.get(player)[0];
	}
	
	public Integer getMaxLevel(String player) {
		if (Levels.get(player).length == 2) {
			return Levels.get(player)[1];
		} else {
			return Levels.get(player)[0];
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
