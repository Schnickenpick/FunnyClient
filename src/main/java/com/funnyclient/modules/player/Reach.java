package com.funnyclient.modules.player;

import com.funnyclient.modules.Module;
import com.funnyclient.settings.DoubleSetting;

public class Reach extends Module {
    private final DoubleSetting reach = new DoubleSetting("Reach", 4.5, 3.0, 6.0, 0.1);
    
    public Reach() {
        super("Reach", "Extend your reach distance", Category.PLAYER);
        getSettings().add(reach);
    }
    
    public double getReach() {
        return isEnabled() ? reach.getValue() : 3.0;
    }
}
