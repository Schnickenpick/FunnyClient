package com.example;

import com.example.mixin.IMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import java.util.Comparator;
import java.util.stream.StreamSupport;

public class CrystalAura {
    public static boolean crystalAuraEnabled = false;
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void tick() {
        if (!crystalAuraEnabled || mc.player == null || mc.world == null) return;

        // 1. Find Target (Mobs/Players)
        LivingEntity target = FunnyUtils.getClosestEntity(10.0);
        if (target == null) return;

        // 2. Break Logic: Kill crystals instantly
        EndCrystalEntity crystal = findNearestCrystal(6.0);
        if (crystal != null) {
            attackCrystal(crystal);
        } 

        // 3. Auto-Switch and Place Logic
        int crystalSlot = FunnyUtils.findItemInHotbar(Items.END_CRYSTAL);
        if (crystalSlot != -1) {
            mc.player.getInventory().selectedSlot = crystalSlot;
            
            BlockPos placePos = findObsidian(target);
            if (placePos != null) {
                placeCrystal(placePos);
            }
        }
    }

    private static void attackCrystal(EndCrystalEntity crystal) {
        mc.interactionManager.attackEntity(mc.player, crystal);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private static void placeCrystal(BlockPos pos) {
        // Uses the Mixin Accessor to bypass the 4-tick delay
        ((IMinecraftClient) mc).setItemUseCooldown(0);

        BlockHitResult hit = new BlockHitResult(
            new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5),
            Direction.UP, pos, false
        );

        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private static BlockPos findObsidian(LivingEntity target) {
        BlockPos center = target.getBlockPos();
        int r = 4; 

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (mc.world.getBlockState(pos).isOf(net.minecraft.block.Blocks.OBSIDIAN)) {
                        if (mc.world.getBlockState(pos.up()).isAir()) {
                            return pos;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static EndCrystalEntity findNearestCrystal(double range) {
        return StreamSupport.stream(mc.world.getEntities().spliterator(), false)
            .filter(e -> e instanceof EndCrystalEntity)
            .map(e -> (EndCrystalEntity) e)
            .filter(e -> mc.player.distanceTo(e) <= range)
            .min(Comparator.comparingDouble(c -> mc.player.distanceTo(c)))
            .orElse(null);
    }
}