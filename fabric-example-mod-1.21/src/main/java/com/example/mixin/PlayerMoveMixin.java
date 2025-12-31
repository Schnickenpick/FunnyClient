package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerMoveMixin {

    /**
     * This "Injects" our code into the getMovementSpeed method.
     * "At RETURN" means we wait for Minecraft to calculate the speed, 
     * then we grab that value and change it before the game uses it.
     */
    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    private void multiplySpeed(CallbackInfoReturnable<Float> cir) {
        // 1. Get the original speed (usually 0.1 for walking)
        float originalSpeed = cir.getReturnValue();
        
        // 2. Multiply it by our GUI slider value
        // We cast to (float) because Minecraft physics use floats, not doubles
        float newSpeed = originalSpeed * (float) FunnyScreen.speedValue;
        
        // 3. Force the game to use our new faster speed
        cir.setReturnValue(newSpeed);
    }
}