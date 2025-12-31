package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class ReachMixin {
    @Inject(method = "getEntityInteractionRange", at = @At("HEAD"), cancellable = true)
    private void onGetReach(CallbackInfoReturnable<Double> cir) {
        if (FunnyScreen.reachEnabled) {
            // 6.0 is double the normal reach. Most servers will flag anything higher.
            cir.setReturnValue(6.0); 
        }
    }
}