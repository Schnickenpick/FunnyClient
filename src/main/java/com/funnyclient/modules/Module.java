package com.funnyclient.modules;

import com.funnyclient.settings.Setting;
import com.funnyclient.FunnyClient;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected final MinecraftClient mc = FunnyClient.mc;
    
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>(); // Fixed generic
    
    private boolean enabled = false;
    private boolean expanded = false;
    private int key = GLFW.GLFW_KEY_UNKNOWN;
    
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }
    
    public void toggle() {
        setEnabled(!enabled);
    }
    
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }
    
    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public boolean isExpanded() { return expanded; }
    public int getKey() { return key; }
    public List<Setting<?>> getSettings() { return settings; } // Fixed generic
    
    public void setKey(int key) { this.key = key; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
    
    public enum Category {
        COMBAT("Combat"), MOVEMENT("Movement"), RENDER("Render"), PLAYER("Player"), MISC("Misc");
        private final String name;
        Category(String name) { this.name = name; }
        public String getName() { return name; }
    }
}