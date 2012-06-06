package me.firedroide.plugins.EnchantmentHelper;

import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class EnchantmentGiver extends Thread {
	
	PrepareItemEnchantEvent e;
	EnchantmentHelper plugin;
	Boolean killed;
	Integer min;
	Integer max;
	
	public EnchantmentGiver(PrepareItemEnchantEvent event, EnchantmentHelper pl, Integer MinimumLevel, Integer MaximumLevel) {
		e = event;
		plugin = pl;
		killed = false;
		min = MinimumLevel;
		max = MaximumLevel;
	}
	
	@Override
	public void run() {
		if (e.getItem().getTypeId() == 0 || e.getView().getItem(0).getTypeId() == 0) kill();
		
		if (plugin.getSendMessages()) plugin.getLogger().info(e.getEnchanter().getName() + " started enchanting.");
		
		Integer j = plugin.getMaxAttempts();
		Boolean lvl = false;
		int[] lvls = e.getExpLevelCostsOffered();
		while (!(lvl || killed)) {
			j -= 1;
			if (j <= 0) {
				break;
			}
			for (int i = 0; i <= 2 ; i++) {
				if (lvls[i] >= min && lvls[i] <= max) {
					lvl = true;
					break;
				}
			}
			
			try {
				Thread.sleep(plugin.getDelay());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if (!lvl) {
				if (e.getView().getItem(0).equals(e.getItem())) {
					e.getView().setItem(0, e.getItem());
				} else {
					break;
				}
			}
		}
		
		if (lvl && plugin.getReset()) plugin.getLevels().remove(e.getEnchanter().getName());
		if (plugin.getSendMessages()) plugin.getLogger().info(e.getEnchanter().getName() + " finished enchanting");		
		kill();
	}
	
	public void kill() {
		killed = true;
		plugin.getThreads().remove(e.getEnchanter().getName());
	}
}
