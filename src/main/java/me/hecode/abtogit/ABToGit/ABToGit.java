package me.hecode.abtogit.ABToGit;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ABToGit extends JavaPlugin {
	// 自动备份定时器对象，以方便在随时随地开始停止任务
	public class BackupTimer extends BukkitRunnable {
		@Override
		public void run() {
			if(isProcessing == false) {
				say("§3[ABToGit]§r 自动备份已开始");
				StartBackup();
			} else {
				say("§3[ABToGit]§r 备份已在运行。");
			}
			
		}
	}
	public boolean isProcessing = false;
	public BackupTimer timer;

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
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("git add . & git commit -m 自动备份 & git push");
			p.waitFor();
			say("§3[ABToGit]§r 备份已完成");
		} catch(Exception e) {
			say("§3[ABToGit]§r 备份时发生错误！");
		}
		
		// 标记不在运行
		isProcessing = false;
	}
	
	@Override
	public void onEnable() {
		say("§3AutoBackupToGit, by HECODE, v1.0.0");
		// 读取配置文件
		long delay = /*getConfig().getInt("BackupFirstDelay")*/300;
		long period = /*getConfig().getInt("BackupCyclePeriod")*/3600;
		// 设置定时
		timer.runTaskTimer(this, delay * 1000L, period * 1000L);
		//timer.scheduleAtFixedRate(task, delay, period);
		// 设置指令
		Bukkit.getPluginCommand("backuptogit").setExecutor(this);
		//say("§3[ABToGit]§r 插件已加载！");
	}
	
	@Override
	public void onDisable() {
		// 取消事件
		timer.cancel();
		//say("§3[ABToGit]§r 插件已卸载！");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if(label.equalsIgnoreCase("backuptogit") && sender.hasPermission("abtogit.command.backuptogit")) {
			if(isProcessing == false) {
				sender.sendMessage("[ABToGit] 手动备份已开始");
				say("§3[ABToGit]§r 手动备份已开始");
				StartBackup();
			} else {
				sender.sendMessage("[ABToGit] 备份已在运行。");
			}
			
			return true;
		} else {
			return false;
		}
	}
}