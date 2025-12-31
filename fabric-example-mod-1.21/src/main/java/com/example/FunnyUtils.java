package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class FunnyUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // 1. Target Finder: Finds the closest LivingEntity (Mob or Player)
    public static LivingEntity getClosestEntity(double range) {
        if (mc.world == null || mc.player == null) return null;
        LivingEntity closest = null;
        double closestDist = range;

        for (Entity e : mc.world.getEntities()) {
            if (e instanceof LivingEntity entity && e != mc.player && e.isAlive()) {
                double dist = mc.player.distanceTo(e);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = entity;
                }
            }
        }
        return closest;
    }

    // 2. Hotbar Helper: Finds an item in slots 0-8
    public static int findItemInHotbar(Item item) {
        if (mc.player == null) return -1;
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    // 3. Simple Raycast Check: Can the player see this spot?
    public static boolean canSeePos(Vec3d pos) {
        if (mc.world == null || mc.player == null) return false;
        
        RaycastContext context = new RaycastContext(
            mc.player.getEyePos(), 
            pos,
            RaycastContext.ShapeType.COLLIDER,
            RaycastContext.FluidHandling.NONE,
            mc.player
        );

        return mc.world.raycast(context).getType() == HitResult.Type.MISS;
    }
}