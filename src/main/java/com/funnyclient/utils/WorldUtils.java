package com.funnyclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.stream.Collectors;

public class WorldUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static List<Entity> getEntitiesInRange(double range) {
        if (mc.world == null || mc.player == null) return List.of();
        
        Box box = new Box(mc.player.getBlockPos()).expand(range);
        return mc.world.getOtherEntities(mc.player, box);
    }
    
    public static List<PlayerEntity> getPlayers() {
        if (mc.world == null) return List.of();
        return mc.world.getPlayers().stream()
            .filter(p -> p != mc.player)
            .collect(Collectors.toList());
    }
    
    public static List<LivingEntity> getMobs(double range) {
        return getEntitiesInRange(range).stream()
            .filter(e -> e instanceof Monster)
            .map(e -> (LivingEntity) e)
            .collect(Collectors.toList());
    }
    
    public static boolean isEntityInBox(Entity entity, BlockPos pos) {
        Box box = new Box(pos);
        return entity.getBoundingBox().intersects(box);
    }
    
    public static List<Entity> getEntitiesInBox(Box box) {
        if (mc.world == null) return List.of();
        return mc.world.getOtherEntities(null, box);
    }
    
    public static boolean canSeeEntity(Entity entity) {
        if (mc.player == null || mc.world == null) return false;
        Vec3d vec1 = mc.player.getEyePos();
        Vec3d vec2 = entity.getPos().add(0, entity.getEyeHeight(entity.getPose()) / 2, 0);
        return mc.world.raycast(new net.minecraft.world.RaycastContext(
            vec1,
            vec2,
            net.minecraft.world.RaycastContext.ShapeType.COLLIDER,
            net.minecraft.world.RaycastContext.FluidHandling.NONE,
            mc.player
        )).getType() == net.minecraft.util.hit.HitResult.Type.MISS;
    }
}