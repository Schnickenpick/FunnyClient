package com.funnyclient.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class RenderUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static void drawBox(MatrixStack matrices, Box box, float r, float g, float b, float a) {
        matrices.push();
        
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);
        
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;
        
        // Bottom
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        
        // Top
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
        
        // Verticals
        buffer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
        buffer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
        buffer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
        
        RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
        RenderSystem.lineWidth(2.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrices.pop();
    }
    
    public static void drawLine(MatrixStack matrices, Vec3d start, Vec3d end, float r, float g, float b, float a) {
        matrices.push();
        
        // Use the camera's interpolated position to prevent "lagging" lines
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);
        
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        
        RenderSystem.setShader(ShaderProgramKeys.RENDERTYPE_LINES);
        RenderSystem.lineWidth(2.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        
        buffer.vertex(matrix, (float) start.x, (float) start.y, (float) start.z).color(r, g, b, a);
        buffer.vertex(matrix, (float) end.x, (float) end.y, (float) end.z).color(r, g, b, a);
        
        BufferRenderer.drawWithGlobalProgram(buffer.end());
        
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        matrices.pop();
    }
    
    
    public static void drawEntityBox(MatrixStack matrices, Entity entity, float r, float g, float b, float a) {
        drawBox(matrices, entity.getBoundingBox(), r, g, b, a);
    }
    
    public static void drawBlockBox(MatrixStack matrices, BlockPos pos, float r, float g, float b, float a) {
        drawBox(matrices, new Box(pos), r, g, b, a);
    }
}