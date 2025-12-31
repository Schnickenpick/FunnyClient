package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FlightMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void doFlight(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        if (player.getAbilities() == null) return;
        
        if (FunnyScreen.flyEnabled) {
            player.getAbilities().allowFlying = true;
            // Apply the fly speed slider value (Default is 0.05)
            player.getAbilities().setFlySpeed((float)(0.05 * FunnyScreen.flySpeedValue));
        } else if (!player.isCreative() && !player.isSpectator()) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
            player.getAbilities().setFlySpeed(0.05f); // Reset to default
        }
    }
}