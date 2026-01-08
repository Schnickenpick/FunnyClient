package com.funnyclient.modules.render;

import com.funnyclient.modules.Module;

public class Fullbright extends Module {
    private double oldGamma;
    
    public Fullbright() {
        super("Fullbright", "See in the dark", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        if (mc.options == null) return;
        oldGamma = mc.options.getGamma().getValue();
        mc.options.getGamma().setValue(16.0);
    }
    
    @Override
    public void onDisable() {
        if (mc.options == null) return;
        mc.options.getGamma().setValue(oldGamma);
    }
}
