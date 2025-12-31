package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MovementMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
    ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
    
    // Safety check: if the game isn't fully in a world yet, STOP.
    if (player == null || player.getWorld() == null || player.input == null) return;

    // This prevents the 'Dead Code' warning while actually stopping the crash.
    // We check if the player has a world and an input system initialized.
    if (player.getWorld() == null || player.input == null) return;

    // 1. AUTOSPRINT
    if (FunnyScreen.sprintEnabled) {
        if (player.input.movementForward > 0 && !player.isSneaking() && !player.horizontalCollision) {
            player.setSprinting(true);
        }
    }

    // 2. JESUS (Movement)
    if (FunnyScreen.jesusEnabled) {
        if (player.isTouchingWater() || player.isInLava()) {
            Vec3d vel = player.getVelocity();
            player.setVelocity(vel.x, 0.1, vel.z);
            player.setOnGround(true);
        }
    }
}}