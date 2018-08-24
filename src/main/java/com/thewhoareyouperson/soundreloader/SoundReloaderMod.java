package com.thewhoareyouperson.soundreloader;

import java.lang.reflect.Field;

import org.lwjgl.input.Keyboard;

import com.thewhoareyouperson.soundreloader.command.SoundReload;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod(modid = "AsyncSoundReloader", version = "1.1-dev")
public class SoundReloaderMod {

	private KeyBinding soundBinding;
	private SoundManager cachedManager = null;
	private boolean nowReloading;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		this.soundBinding = new KeyBinding("Reload Default Sound", Keyboard.KEY_P, "key.categories.misc");
		ClientRegistry.registerKeyBinding(this.soundBinding);

		MinecraftForge.EVENT_BUS.register(this);
		ClientCommandHandler.instance.registerCommand(new SoundReload(this));
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(this.soundBinding.isPressed()) {
			this.reloadSound(false);
		}
	}

	public void reloadSound(boolean sync) {
		if(this.cachedManager == null) {
			SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
			SoundManager mngr = null;
			try {
				Field fi;
				try {
					fi = SoundHandler.class.getDeclaredField("field_147694_f");
				} catch (Exception e) {
					fi = SoundHandler.class.getDeclaredField("sndManager");
				}
				fi.setAccessible(true);
				mngr = (SoundManager) fi.get(handler);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.cachedManager = mngr;
		}

		if(this.cachedManager != null) {
			if(this.nowReloading) {
				return;
			}
			this.nowReloading = true;
			new Thread(() -> {
				this.sendMessage("&7Reloading sound system...");
				this.cachedManager.reloadSoundSystem();
				this.sendMessage("&aReloaded sound system!");
				this.nowReloading = false;
			}).start();
		} else {
			this.sendMessage("&cUnable to get sound manager");
		}
	}

	private void sendMessage(String message) {
		message = message.replaceAll("&", "\u00a7");
		message = message.replaceAll("§", "\u00a7");

		Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(message));
	}

}
