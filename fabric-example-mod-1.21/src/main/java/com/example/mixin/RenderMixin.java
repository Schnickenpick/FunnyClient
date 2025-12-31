package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class RenderMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(
        @Coerce Object allocator, 
        RenderTickCounter tickCounter,
        boolean renderBlockOutline,
        Camera camera,
        GameRenderer gameRenderer,
        Matrix4f projectionMatrix,
        Matrix4f modelViewMatrix,
        CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        float delta = tickCounter.getTickDelta(false);
        Vec3d camPos = camera.getPos();
        
        // We use a MatrixStack to ensure the rotation is handled correctly
        MatrixStack matrices = new MatrixStack();
        // This is the "Magic": it aligns our drawing with the camera's actual view
        matrices.multiplyPositionMatrix(modelViewMatrix);

        VertexConsumerProvider.Immediate consumers = client.getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer lines = consumers.getBuffer(RenderLayer.getLines());

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof LivingEntity) || entity == client.player || entity.isRemoved()) continue;

            // 100 Block Range Limit
            if (entity.squaredDistanceTo(client.player) > 10000) continue;

            // Calculate smooth world position
            double x = (entity.prevX + (entity.getX() - entity.prevX) * delta) - camPos.x;
            double y = (entity.prevY + (entity.getY() - entity.prevY) * delta) - camPos.y;
            double z = (entity.prevZ + (entity.getZ() - entity.prevZ) * delta) - camPos.z;

            // TRACERS
            if (FunnyScreen.tracersEnabled) {
                float h = entity.getHeight() / 2.0f;
                // Start: matrix (center of screen), End: world position
                lines.vertex(matrices.peek().getPositionMatrix(), 0, 0, 0).color(255, 255, 255, 255).normal(0, 1, 0);
                lines.vertex(matrices.peek().getPositionMatrix(), (float)x, (float)y + h, (float)z).color(255, 255, 255, 255).normal(0, 1, 0);
            }

            // ESP
            if (FunnyScreen.espEnabled) {
                drawBox(matrices, lines, x, y, z, entity.getWidth() / 2, entity.getHeight());
            }
        }
        consumers.draw(RenderLayer.getLines());
    }

    private void drawBox(MatrixStack matrices, VertexConsumer buffer, double x, double y, double z, double w, double h) {
        float x1 = (float)x - (float)w; float y1 = (float)y; float z1 = (float)z - (float)w;
        float x2 = (float)x + (float)w; float y2 = (float)y + (float)h; float z2 = (float)z + (float)w;
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        // Bottom
        line(buffer, positionMatrix, x1, y1, z1, x2, y1, z1);
        line(buffer, positionMatrix, x2, y1, z1, x2, y1, z2);
        line(buffer, positionMatrix, x2, y1, z2, x1, y1, z2);
        line(buffer, positionMatrix, x1, y1, z2, x1, y1, z1);
        // Top
        line(buffer, positionMatrix, x1, y2, z1, x2, y2, z1);
        line(buffer, positionMatrix, x2, y2, z1, x2, y2, z2);
        line(buffer, positionMatrix, x2, y2, z2, x1, y2, z2);
        line(buffer, positionMatrix, x1, y2, z2, x1, y2, z1);
        // Pillars
        line(buffer, positionMatrix, x1, y1, z1, x1, y2, z1);
        line(buffer, positionMatrix, x2, y1, z1, x2, y2, z1);
        line(buffer, positionMatrix, x2, y1, z2, x2, y2, z2);
        line(buffer, positionMatrix, x1, y1, z2, x1, y2, z2);
    }

    private void line(VertexConsumer buffer, Matrix4f matrix, float x1, float y1, float z1, float x2, float y2, float z2) {
        buffer.vertex(matrix, x1, y1, z1).color(255, 0, 0, 255).normal(0, 1, 0);
        buffer.vertex(matrix, x2, y2, z2).color(255, 0, 0, 255).normal(0, 1, 0);
    }
}