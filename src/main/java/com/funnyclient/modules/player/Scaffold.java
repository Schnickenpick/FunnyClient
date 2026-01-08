package com.funnyclient.modules.player;

import com.funnyclient.modules.Module;
import com.funnyclient.settings.BoolSetting;
import com.funnyclient.settings.DoubleSetting;
import com.funnyclient.utils.BlockUtils;
import com.funnyclient.utils.InventoryUtils;
import com.funnyclient.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {
    private final BoolSetting rotate = new BoolSetting("Rotate", true);
    private final BoolSetting autoSwitch = new BoolSetting("Auto Switch", true);
    private final DoubleSetting delay = new DoubleSetting("Delay", 0.0, 0.0, 5.0, 0.1);
    
    private int tickCounter = 0;
    
    public Scaffold() {
        super("Scaffold", "Automatically places blocks under you", Category.PLAYER);
        getSettings().add(rotate);
        getSettings().add(autoSwitch);
        getSettings().add(delay);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        
        // Delay system
        if (delay.getValue() > 0) {
            tickCounter++;
            if (tickCounter < delay.getValue() * 20) return;
            tickCounter = 0;
        }
        
        // Get position below player
        BlockPos belowPos = mc.player.getBlockPos().down();
        
        // Check if we need to place a block
        if (!BlockUtils.isAir(belowPos)) return;
        
        // Find a block to place
        int blockSlot = findBlockSlot();
        if (blockSlot == -1) return;
        
        int oldSlot = InventoryUtils.getHotbarSlot();
        
        // Switch to block if needed
        if (autoSwitch.getValue() && blockSlot != oldSlot) {
            InventoryUtils.switchToSlot(blockSlot);
        }
        
        // Find a placeable neighbor
        BlockPos placePos = null;
        Direction placeSide = null;
        
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = belowPos.offset(dir);
            if (!BlockUtils.isAir(neighbor)) {
                placePos = neighbor;
                placeSide = dir.getOpposite();
                break;
            }
        }
        
        if (placePos == null) return;
        
        // Rotate if needed
        if (rotate.getValue()) {
            RotationUtils.rotateTo(Vec3d.ofCenter(placePos));
        }
        
        // Place the block
        BlockHitResult hitResult = new BlockHitResult(
            Vec3d.ofCenter(placePos),
            placeSide,
            placePos,
            false
        );
        
        mc.interactionManager.interactBlock(
            mc.player,
            Hand.MAIN_HAND,
            hitResult
        );
        
        mc.player.swingHand(Hand.MAIN_HAND);
        
        // Switch back
        if (autoSwitch.getValue() && blockSlot != oldSlot) {
            InventoryUtils.switchToSlot(oldSlot);
        }
    }
    
    private int findBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = InventoryUtils.getStackInSlot(i);
            if (stack.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                // Make sure it's a solid block
                if (block.getDefaultState().isFullCube(mc.world, BlockPos.ORIGIN)) {
                    return i;
                }
            }
        }
        return -1;
    }
}
