package me.firedroide.plugins.EnchantmentHelper;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class EnchantmentListener implements Listener {
	
	EnchantmentHelper plugin;
	
	public EnchantmentListener(EnchantmentHelper plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPrepareItemEnchantEvent(PrepareItemEnchantEvent e) {
		if (e.getEnchanter() != null) {
			
			if (!plugin.getLevels().containsKey(e.getEnchanter().getName())) return;
			
			if (!plugin.getThreads().containsKey(e.getEnchanter().getName()) && plugin.getLevels().containsKey(e.getEnchanter().getName())) {
				EnchantmentGiver thr = new EnchantmentGiver(e, plugin,
						plugin.getMinLevel(e.getEnchanter().getName()),
						plugin.getMaxLevel(e.getEnchanter().getName()));
				plugin.getThreads().put(e.getEnchanter().getName(), thr);
				thr.start();
			}
		}
	}
	
	
}
