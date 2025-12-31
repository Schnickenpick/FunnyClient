package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlock.class)
public class JesusMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        
        // 1. SAFETY CHECK: Check if the game is actually ready
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.player == null) {
            return;
        }
        
        // 2. TOGGLE CHECK: Only run if Jesus is turned on
        if (!FunnyScreen.jesusEnabled) {
            return;
        }

        // 3. LOGIC: Make the water solid if the player is standing on it
        if (context instanceof EntityShapeContext entityContext) {
            Entity entity = entityContext.getEntity();
            
            if (entity != null && entity.isPlayer()) {
                // If the player's feet are above the water surface
                if (entity.getY() > (double) pos.getY() + 0.9) {
                    cir.setReturnValue(VoxelShapes.fullCube());
                }
            }
        }
    }
}