package com.funnyclient.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static boolean canPlace(BlockPos pos) {
        if (mc.world == null) return false;
        return mc.world.getBlockState(pos).isReplaceable();
    }
    
    public static boolean isAir(BlockPos pos) {
        if (mc.world == null) return false;
        return mc.world.getBlockState(pos).isAir();
    }
    
    public static boolean isBlockType(BlockPos pos, Block block) {
        if (mc.world == null) return false;
        return mc.world.getBlockState(pos).getBlock() == block;
    }
    
    public static BlockState getBlockState(BlockPos pos) {
        if (mc.world == null) return Blocks.AIR.getDefaultState();
        return mc.world.getBlockState(pos);
    }
    
    public static List<BlockPos> getSphere(BlockPos center, double radius) {
        List<BlockPos> positions = new ArrayList<>();
        int r = (int) Math.ceil(radius);
        
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.add(x, y, z);
                    if (center.isWithinDistance(pos, radius)) {
                        positions.add(pos);
                    }
                }
            }
        }
        
        return positions;
    }
    
    public static List<BlockPos> getBlocksInReach(double reach) {
        if (mc.player == null) return List.of();
        return getSphere(mc.player.getBlockPos(), reach);
    }
    
    public static boolean canBeClicked(BlockPos pos) {
        if (mc.world == null) return false;
        BlockState state = mc.world.getBlockState(pos);
        return !state.isAir() && state.getHardness(mc.world, pos) >= 0;
    }
    
    public static Direction getPlaceableSide(BlockPos pos) {
        for (Direction side : Direction.values()) {
            BlockPos neighbor = pos.offset(side);
            if (!isAir(neighbor)) {
                return side.getOpposite();
            }
        }
        return Direction.UP;
    }
    
    public static boolean hasSolidNeighbor(BlockPos pos) {
        for (Direction dir : Direction.values()) {
            if (!isAir(pos.offset(dir))) {
                return true;
            }
        }
        return false;
    }
}