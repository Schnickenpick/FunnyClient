package com.funnyclient.events;

import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent {
    private final MatrixStack matrices;
    private final float tickDelta;
    
    public RenderEvent(MatrixStack matrices, float tickDelta) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
    }
    
    public MatrixStack getMatrices() {
        return matrices;
    }
    
    public float getTickDelta() {
        return tickDelta;
    }
}
