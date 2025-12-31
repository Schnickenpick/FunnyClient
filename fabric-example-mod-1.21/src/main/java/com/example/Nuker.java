package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Nuker {
    public static boolean nukerEnabled = false;
    private static int timer = 0;

    public static void tick(MinecraftClient client) {
        if (!nukerEnabled || client.player == null || client.world == null) return;

        // Delay logic (Meteor uses this to prevent kicks)
        if (timer > 0) {
            timer--;
            return;
        }

        int range = 4; // Same as Meteor's default
        BlockPos playerPos = client.player.getBlockPos();

        // The "Cube" search algorithm
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos targetPos = playerPos.add(x, y, z);

                    // Skip air and bedrock
                    if (client.world.getBlockState(targetPos).isAir() || 
                        client.world.getBlockState(targetPos).getHardness(client.world, targetPos) < 0) continue;

                    // This is Meteor's "Packet Mine" logic translated to Vanilla
                    // START_DESTROY_BLOCK + STOP_DESTROY_BLOCK = Insta-mine (if server allows)
                    client.player.networkHandler.sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, targetPos, Direction.UP));
                    
                    client.player.networkHandler.sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, targetPos, Direction.UP));

                    // Swing hand so it looks real
                    client.player.swingHand(Hand.MAIN_HAND);
                    
                    timer = 1; // 1 tick delay between blocks to be safe
                    return; // Break one block per tick
                }
            }
        }
    }
}