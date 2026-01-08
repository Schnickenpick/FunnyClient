package com.funnyclient.modules.player;

import com.funnyclient.modules.Module;
import com.funnyclient.settings.BoolSetting;
import com.funnyclient.settings.DoubleSetting;
import com.funnyclient.utils.BlockUtils;
import com.funnyclient.utils.RotationUtils;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class Nuker extends Module {
    private final DoubleSetting range = new DoubleSetting("Range", 4.5, 1.0, 6.0, 0.5);
    private final BoolSetting rotate = new BoolSetting("Rotate", true);
    private final BoolSetting instant = new BoolSetting("Instant", false);
    
    public Nuker() {
        super("Nuker", "Automatically breaks blocks around you", Category.PLAYER);
        getSettings().add(range);
        getSettings().add(rotate);
        getSettings().add(instant);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        
        List<BlockPos> blocks = BlockUtils.getBlocksInReach(range.getValue());
        
        for (BlockPos pos : blocks) {
            if (!BlockUtils.canBeClicked(pos)) continue;
            if (BlockUtils.isBlockType(pos, Blocks.BEDROCK)) continue;
            
            if (rotate.getValue()) {
                RotationUtils.rotateTo(pos);
            }
            
            Direction side = BlockUtils.getPlaceableSide(pos);
            
            if (instant.getValue()) {
                // Packet nuker
                mc.player.networkHandler.sendPacket(
                    new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK,
                        pos,
                        side
                    )
                );
                mc.player.networkHandler.sendPacket(
                    new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK,
                        pos,
                        side
                    )
                );
            } else {
                // Normal nuker
                mc.interactionManager.updateBlockBreakingProgress(pos, side);
            }
            
            break; // Only break one block per tick
        }
    }
}
