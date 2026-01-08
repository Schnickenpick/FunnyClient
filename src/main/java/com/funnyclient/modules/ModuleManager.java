package com.funnyclient.modules;

import com.funnyclient.modules.combat.CrystalAura;
import com.funnyclient.modules.movement.Flight;
import com.funnyclient.modules.player.Nuker;
import com.funnyclient.modules.player.Reach;
import com.funnyclient.modules.player.Scaffold;
import com.funnyclient.modules.render.ESP;
import com.funnyclient.modules.render.Fullbright;
import com.funnyclient.modules.render.Tracers;
import com.funnyclient.FunnyClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        // Combat modules
        register(new CrystalAura());
        
        // Movement modules
        register(new Flight());
        
        // Player modules
        register(new Nuker());
        register(new Reach());
        register(new Scaffold());
        
        // Render modules
        register(new ESP());
        register(new Fullbright());
        register(new Tracers());
        
        FunnyClient.LOGGER.info("Registered {} modules", modules.size());
    }
    
    private void register(Module module) {
        modules.add(module);
    }
    
    public void onTick() {
        modules.stream()
            .filter(Module::isEnabled)
            .forEach(Module::onTick);
    }
    
    public Module getModule(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public List<Module> getModulesByCategory(Module.Category category) {
        return modules.stream()
            .filter(m -> m.getCategory() == category)
            .collect(Collectors.toList());
    }
    
    public <T extends Module> T getModule(Class<T> moduleClass) {
        return modules.stream()
            .filter(m -> m.getClass() == moduleClass)
            .map(moduleClass::cast)
            .findFirst()
            .orElse(null);
    }
}
