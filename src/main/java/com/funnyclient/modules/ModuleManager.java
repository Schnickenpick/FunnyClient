package com.funnyclient.modules;

import com.funnyclient.modules.combat.CrystalAura;
import com.funnyclient.modules.movement.Flight;
import com.funnyclient.FunnyClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    
    public ModuleManager() {
        // Register modules
        register(new Flight());
        register(new CrystalAura());
        
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
}