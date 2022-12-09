package com.hecode.abtogit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

//import java.util.Timer;
//import java.util.TimerTask;

public class PluginMain extends JavaPlugin {
	// 自动备份定时器对象，以方便在随时随地开始停止任务
	public class timer extends BukkitRunnable {
		@Override
		public void run() {
			if(isProcessing == false) {
				say("§3[ABToGit]§r 自动备份已开始");
				StartBackup();
			} else {
				say("§3[ABToGit]§r 备份已在运行。")
			}
			
		}
	}
	public boolean isProcessing = false;

	public void say(String s) {
		// 用于在控制台输出提示
		CommandSender sender = Bukkit.getConsoleSender();
		sender.sendMessage(s);
	}

	public void StartBackup() {
		// 标记正在运行
		isProcessing = true;
		// Minecraft 保存所有内容。
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		// 运行 Git 提交、推送
		Process process = Runtime.getRuntime().exec("git add . & git commit -m 自动备份 & git push");
		process.waitfor();
		say("§3[ABToGit]§r 备份已完成");
		// 标记不在运行
		isProcessing = false;
	}

	public void onEnable() {
		say("§3AutoBackupToGit, by HECODE, v1.0.0");
		// 读取配置文件
		long delay = getConfig().getint("BackupFirstDelay");
		long period = getConfig().getint("BackupCyclePeriod") * 1000;
		// 设置定时
		timer.runTaskTimer(this, delay * 1L, period * 1L);
		//timer.scheduleAtFixedRate(task, delay, period);
		say("§3[ABToGit]§r 插件已加载！");
	}

	public void onDisable() {
		// 取消事件
		timer.cancel();
		say("§3[ABToGit]§r 插件已卸载！");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if(label.equalsIgnoreCase("backuptogit") && sender.hasPermisson("abtogit.command.backuptogit")) {
			if(isProcessing == false) {
				sender.sendMessage("[ABToGit] 手动备份已开始");
				say("§3[ABToGit]§r 手动备份已开始");
				StartBackup();
			} else {
				sender.sendMessage("[ABToGit] 备份已在运行。")
			}
			
			return true;
		} else {
			return false;
		}
	}
}