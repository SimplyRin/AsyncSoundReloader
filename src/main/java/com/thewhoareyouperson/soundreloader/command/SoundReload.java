package com.thewhoareyouperson.soundreloader.command;

import com.thewhoareyouperson.soundreloader.SoundReloaderMod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * Created by SimplyRin on 2018/08/24.
 */
public class SoundReload extends CommandBase {

	private SoundReloaderMod instance;

	public SoundReload(SoundReloaderMod instance) {
		this.instance = instance;
	}

	@Override
	public String getCommandName() {
		return "soundreload";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("sync")) {
				this.instance.reloadSound(true);
				return;
			}
		}
		this.instance.reloadSound(false);
	}

}
