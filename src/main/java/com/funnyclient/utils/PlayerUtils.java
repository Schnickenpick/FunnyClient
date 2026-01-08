package com.funnyclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static double distanceTo(Entity entity) {
        if (mc.player == null) return 0;
        return mc.player.distanceTo(entity);
    }
    
    public static double distanceTo(BlockPos pos) {
        if (mc.player == null) return 0;
        return Math.sqrt(mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    
    public static double distanceTo(Vec3d vec) {
        if (mc.player == null) return 0;
        return mc.player.getPos().distanceTo(vec);
    }
    
    public static List<PlayerEntity> getPlayersInRange(double range) {
        if (mc.world == null) return List.of();
        return mc.world.getPlayers().stream()
            .filter(p -> p != mc.player)
            .filter(p -> !p.isDead())
            .filter(p -> distanceTo(p) <= range)
            .collect(Collectors.toList());
    }
    
    public static PlayerEntity getClosestPlayer(double range) {
        return getPlayersInRange(range).stream()
            .min((p1, p2) -> Double.compare(distanceTo(p1), distanceTo(p2)))
            .orElse(null);
    }
    
    public static Vec3d getPlayerPos() {
        return mc.player != null ? mc.player.getPos() : Vec3d.ZERO;
    }
    
    public static Vec3d getEyePos() {
        return mc.player != null ? mc.player.getEyePos() : Vec3d.ZERO;
    }
    
    public static BlockPos getPlayerBlockPos() {
        return mc.player != null ? mc.player.getBlockPos() : BlockPos.ORIGIN;
    }
}