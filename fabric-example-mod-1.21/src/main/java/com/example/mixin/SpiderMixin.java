package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class SpiderMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void doSpider(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (FunnyScreen.spiderEnabled && player.horizontalCollision) {
            var vel = player.getVelocity();
            // Set upward velocity
            player.setVelocity(vel.x, 0.2, vel.z);
        }
    }
}