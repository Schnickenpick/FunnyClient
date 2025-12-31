package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

@Mixin(ClientPlayerEntity.class)
public class KillAuraMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        // 1. Safety Checks
        if (!FunnyScreen.killAuraEnabled || !player.isAlive()) return;

        // 2. Cooldown Check (Wait for sword to be ready)
        if (player.getAttackCooldownProgress(0) < 1.0f) return;

        // 3. Find Target
        net.minecraft.util.math.Box box = player.getBoundingBox().expand(FunnyScreen.killAuraRange);
        
        Entity target = player.getWorld().getOtherEntities(player, box, e -> e instanceof LivingEntity && e.isAlive())
            .stream()
            .filter(e -> player.canSee(e)) 
            .min(Comparator.comparingDouble(player::distanceTo))
            .orElse(null);

        // 4. Attack Logic
        if (target != null) {
            // --- AUTO CRIT SPOOF ---
            if (FunnyScreen.autoCritEnabled && player.isOnGround()) {
                // We tell the server we are slightly in the air so the hit counts as a Critical
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY() + 0.0625, player.getZ(), false, false));
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX(), player.getY(), player.getZ(), false, false));
            }

            // --- THE ATTACK ---
            // In 1.21.4, we send the attack packet directly to the network handler
            player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, player.isSneaking()));
            
            // Swing arm and reset cooldown
            player.swingHand(Hand.MAIN_HAND);
            player.resetLastAttackedTicks();

            // LOGGING: Check your Prism Launcher console to see if this triggers!
            System.out.println("[FunnyMod] Attacking: " + target.getType().getName().getString());
        }
    }
}