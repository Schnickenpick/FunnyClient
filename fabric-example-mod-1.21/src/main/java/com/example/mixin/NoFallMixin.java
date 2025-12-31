package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class NoFallMixin {

    @Inject(method = "sendMovementPackets", at = @At("HEAD"))
    private void onSendMovementPackets(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        if (FunnyScreen.noFallEnabled) {
            // Meteor Logic: Only trigger if we are actually falling with some speed
            // and NOT currently using an Elytra (gliding)
            if (player.getVelocity().y < -0.5 && !player.isGliding()) {
                
                // We send a tiny packet that tells the server "I am on the ground right now"
                // This resets the server-side fall distance counter.
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, player.horizontalCollision));
            }
        }
    }
}