package com.funnyclient;

import com.funnyclient.modules.ModuleManager;
import com.funnyclient.events.EventManager;
import net.fabricmc.api.ClientModInitializer; // Requirement for "client" entrypoint
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunnyClient implements ClientModInitializer {
    public static final String MOD_ID = "funnyclient";
    public static final String NAME = "FunnyClient";
    public static final String VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    
    public static FunnyClient INSTANCE;
    public static MinecraftClient mc;
    
    private ModuleManager moduleManager;
    private EventManager eventManager;
    
    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        // Correct way to initialize MC instance during client startup
        mc = MinecraftClient.getInstance();
        
        LOGGER.info("Initializing {} v{} for Minecraft 1.21.4", NAME, VERSION);
        
        // Initialize managers
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        
        LOGGER.info("{} initialization complete!", NAME);
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
}