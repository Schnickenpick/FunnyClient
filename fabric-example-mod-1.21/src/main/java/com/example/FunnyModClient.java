package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
//import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;



public class FunnyModClient implements ClientModInitializer {
    private static KeyBinding funnyKey;

    @Override
    public void onInitializeClient() {
        /*
        // Inside your onInitializeClient() method:
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Regular features
            Scaffold.tick(client);
            
            
            // Hand of God trigger
            if (HandOfGod.handofgodEnabled) {
                HandOfGod.run(client);
            } 
        }); */

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 1. Fundamental safety check
            if (client.player == null || client.world == null) return;

            // 2. Wrap EVERYTHING in a safety net
            try {
                // Run Scaffold
                Scaffold.tick(client);
                
                // Run Nuker
                Nuker.tick(client);

            } catch (Exception e) {
                // This prints the error to your Prism Launcher log 
                // so we can see EXACTLY why it stopped.
                e.printStackTrace();
            }
        });
        // Register the K key
        funnyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.funnymod.open", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_K, 
            "category.funnymod"
        ));

        // Watch for the key press every frame
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (funnyKey.wasPressed()) {
                client.setScreen(new FunnyScreen());
            }
        });
    }
}