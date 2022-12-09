package com.hecode.abtogit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Backup extends JavaPlugin {
	public void say(String s) {
		CommandSender sender = bukkit.getConsoleSender();
		sender.sendMessage(s);
	}

	@Override
	public void onEnable() {
		say("[AutoBackup] 插件已加载！")
	}

	@Override
	public void onDisable() {
		say("[AutoBackup] 插件已卸载！")
	}
}