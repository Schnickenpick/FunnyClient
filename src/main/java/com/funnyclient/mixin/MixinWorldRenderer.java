package com.funnyclient.mixin;

import com.funnyclient.FunnyClient;
import com.funnyclient.events.RenderEvent;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    
    @Inject(method = "renderLayer", at = @At("RETURN"))
    private void onRenderLayer(CallbackInfo ci) {
        if (FunnyClient.INSTANCE != null) {
            MatrixStack matrices = new MatrixStack();
            FunnyClient.INSTANCE.getEventManager().post(new RenderEvent(matrices, 0));
        }
    }
}