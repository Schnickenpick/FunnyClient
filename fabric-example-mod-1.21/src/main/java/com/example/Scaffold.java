package com.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold {
    public static boolean scaffoldEnabled = false;

    public static void tick(MinecraftClient client) {
        if (!scaffoldEnabled || client.player == null || client.world == null) return;

        // DEBUG: This will make your arm twitch constantly when enabled
        client.player.swingHand(Hand.MAIN_HAND);

        // Use the helper to find a slot
        int blockSlot = findBlockInHotbar();

        // If no blocks, don't do anything
        if (blockSlot == -1) return;

        BlockPos pos = client.player.getBlockPos().down();

        if (client.world.getBlockState(pos).isAir()) {
            // Save old slot to swap back later
            int oldSlot = client.player.getInventory().selectedSlot;
            
            // Swap to the block
            client.player.getInventory().selectedSlot = blockSlot;

            // Place logic
            BlockHitResult hitResult = new BlockHitResult(
                new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
                Direction.UP, pos, false
            );

            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
            client.player.swingHand(Hand.MAIN_HAND);

            // Swap back
            client.player.getInventory().selectedSlot = oldSlot;
        }
    }

    // --- PUT THE HELPER HERE ---
    private static int findBlockInHotbar() {
        MinecraftClient mc = MinecraftClient.getInstance();
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getStack(i);
            // Check if it's not empty AND if the item is a block
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                return i;
            }
        }
        return -1; // Return -1 if no blocks are found
    }
}