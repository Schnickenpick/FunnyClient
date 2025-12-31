package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

@Mixin(ClientPlayerEntity.class)
public class CrystalAuraMixin {
    private int delay = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (!FunnyScreen.crystalAuraEnabled || player == null) return;

        if (delay > 0) {
            delay--;
            return;
        }

        // 1. Find Target
        Entity target = player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(10.0), 
            e -> e instanceof LivingEntity && e.isAlive()).stream()
            .min(Comparator.comparingDouble(player::distanceTo)).orElse(null);

        if (target == null) return;

        // 2. Scan for a valid spot (Obsidian/Bedrock) near the target
        BlockPos bestPos = null;
        BlockPos targetPos = target.getBlockPos();
        
        // Check offsets: Down (feet), North, South, East, West
        BlockPos[] checkOffsets = {
            targetPos.down(), 
            targetPos.north().down(), 
            targetPos.south().down(), 
            targetPos.east().down(), 
            targetPos.west().down()
        };

        for (BlockPos pos : checkOffsets) {
            if (player.getWorld().getBlockState(pos).isOf(net.minecraft.block.Blocks.OBSIDIAN) 
                || player.getWorld().getBlockState(pos).isOf(net.minecraft.block.Blocks.BEDROCK)) {
                
                // Ensure the space above is AIR so we can actually place
                if (player.getWorld().getBlockState(pos.up()).isAir()) {
                    bestPos = pos;
                    break;
                }
            }
        }

        // If we didn't find any obsidian/bedrock near them, we can't do anything.
        if (bestPos == null) return;


        // 3. EXECUTE ATTACK OR PLACE

        // Check for an existing crystal at that spot
        BlockPos finalBestPos = bestPos;
        Entity existingCrystal = player.getWorld().getOtherEntities(player, new net.minecraft.util.math.Box(bestPos.up()).expand(0.5), 
            e -> e instanceof EndCrystalEntity).stream().findFirst().orElse(null);

        if (existingCrystal != null) {
            // --- ATTACK (POP) ---
            player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(existingCrystal, player.isSneaking()));
            player.swingHand(Hand.MAIN_HAND);
            delay = 1; // Super fast delay for popping
        } else {
            // --- PLACE ---
            int crystalSlot = findItem(Items.END_CRYSTAL);
            if (crystalSlot != -1) {
                switchTo(crystalSlot);
                // Click the TOP of the block
                Vec3d hitVec = new Vec3d(finalBestPos.getX() + 0.5, finalBestPos.getY() + 1.0, finalBestPos.getZ() + 0.5);
                BlockHitResult hit = new BlockHitResult(hitVec, Direction.UP, finalBestPos, false);
                
                player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hit, 0));
                player.swingHand(Hand.MAIN_HAND);
                
                // Set delay based on your slider (20 / speed)
                delay = (int) (20 / FunnyScreen.crystalAuraSpeed);
            }
        }
    }

    // --- HELPERS ---
    
    private int findItem(net.minecraft.item.Item item) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }

    private void switchTo(int slot) {
        MinecraftClient.getInstance().player.getInventory().selectedSlot = slot;
    }
}