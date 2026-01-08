package com.funnyclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static class Rotation {
        public final float yaw;
        public final float pitch;
        
        public Rotation(float yaw, float pitch) {
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }
    
    public static Rotation getRotationsTo(Vec3d target) {
        if (mc.player == null) return new Rotation(0, 0);
        
        Vec3d eyePos = mc.player.getEyePos();
        
        double diffX = target.x - eyePos.x;
        double diffY = target.y - eyePos.y;
        double diffZ = target.z - eyePos.z;
        
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        
        return new Rotation(yaw, MathHelper.clamp(pitch, -90, 90));
    }
    
    public static Rotation getRotationsTo(Entity entity) {
        return getRotationsTo(entity.getPos().add(0, entity.getEyeHeight(entity.getPose()) / 2, 0));
    }
    
    public static Rotation getRotationsTo(BlockPos pos) {
        return getRotationsTo(Vec3d.ofCenter(pos));
    }
    
    public static void rotate(Rotation rotation) {
        if (mc.player != null) {
            mc.player.setYaw(rotation.yaw);
            mc.player.setPitch(rotation.pitch);
        }
    }
    
    public static void rotateTo(Vec3d target) {
        rotate(getRotationsTo(target));
    }
    
    public static void rotateTo(Entity entity) {
        rotate(getRotationsTo(entity));
    }
    
    public static void rotateTo(BlockPos pos) {
        rotate(getRotationsTo(pos));
    }
    
    public static double getAngleTo(Entity entity) {
        Rotation needed = getRotationsTo(entity);
        if (mc.player == null) return 0;
        
        float yawDiff = Math.abs(needed.yaw - mc.player.getYaw());
        float pitchDiff = Math.abs(needed.pitch - mc.player.getPitch());
        
        return Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);
    }
}