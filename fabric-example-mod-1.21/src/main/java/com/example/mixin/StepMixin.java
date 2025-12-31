package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class StepMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void applyStep(CallbackInfo ci) {
        // 'this' refers to the PlayerEntity we are mixing into
        PlayerEntity player = (PlayerEntity) (Object) this;
        
        // 1. Safety Check: If the game hasn't finished loading the player's 
        // attributes (like health/speed), stop here so we don't crash.
        if (player.getAttributes() == null) return;
        
        // 2. Grab the Step Height attribute from the player
        var stepAttribute = player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
        
        if (stepAttribute != null) {
            // 3. Instead of ON/OFF, we use the exact value from our slider in FunnyScreen
            double targetValue = FunnyScreen.stepHeightValue;
            
            // 4. Only update if the value actually changed. 
            // Setting attributes every single tick is laggy, so we check first.
            if (stepAttribute.getBaseValue() != targetValue) {
                stepAttribute.setBaseValue(targetValue);
            }
        }
    }
}