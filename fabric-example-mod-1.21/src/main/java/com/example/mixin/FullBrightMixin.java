package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightmapTextureManager.class)
public class FullBrightMixin {

    // This targets the method that calculates brightness for the lightmap
    // In 1.21.4, this is the most stable way to force max brightness
    @Inject(method = "getBrightness", at = @At("RETURN"), cancellable = true)
    private static void onGetBrightness(CallbackInfoReturnable<Float> cir) {
        if (FunnyScreen.fullBrightEnabled) {
            cir.setReturnValue(1.0f); // Sets brightness to 100%
        }
    }
}