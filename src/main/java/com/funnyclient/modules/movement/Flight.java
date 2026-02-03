package com.funnyclient.modules.movement;

import com.funnyclient.modules.Module;
import com.funnyclient.settings.DoubleSetting;
import com.funnyclient.settings.ModeSetting;
//import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Flight extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Packet", "Creative");
    private final DoubleSetting speed = new DoubleSetting("Speed", 1.5, 0.1, 10.0, 0.1);
    private final DoubleSetting verticalSpeed = new DoubleSetting("Vertical Speed", 1.0, 0.1, 5.0, 0.1);
    
    public Flight() {
        super("Flight", "Allows you to fly", Category.MOVEMENT);
        getSettings().add(mode);
        getSettings().add(speed);
        getSettings().add(verticalSpeed);
    }
    
    @Override
    public void onEnable() {
        if (mc.player == null) return;
        
        if (mode.getValue().equals("Creative")) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
        }
    }
    
    @Override
    public void onDisable() {
        if (mc.player == null) return;
        
        if (mode.getValue().equals("Creative")) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().allowFlying = false;
        }
    }
    
    @Override
    public void onTick() {
        if (mc.player == null) return;
        
        switch (mode.getValue()) {
            case "Vanilla":
                vanillaFly();
                break;
            //case "Packet":
            //    packetFly();
            //    break;
            case "Creative":
                // Creative mode handles itself
                break;
        }
    }
    
    private void vanillaFly() {
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) (speed.getValue() / 20.0));
        
        mc.player.setVelocity(0, 0, 0);
        
        // Forward/backward
        if (mc.options.forwardKey.isPressed()) {
            mc.player.addVelocity(
                -Math.sin(Math.toRadians(mc.player.getYaw())) * speed.getValue() / 20,
                0,
                Math.cos(Math.toRadians(mc.player.getYaw())) * speed.getValue() / 20
            );
        }
        if (mc.options.backKey.isPressed()) {
            mc.player.addVelocity(
                Math.sin(Math.toRadians(mc.player.getYaw())) * speed.getValue() / 20,
                0,
                -Math.cos(Math.toRadians(mc.player.getYaw())) * speed.getValue() / 20
            );
        }
        
        // Left/right
        if (mc.options.leftKey.isPressed()) {
            mc.player.addVelocity(
                -Math.sin(Math.toRadians(mc.player.getYaw() - 90)) * speed.getValue() / 20,
                0,
                Math.cos(Math.toRadians(mc.player.getYaw() - 90)) * speed.getValue() / 20
            );
        }
        if (mc.options.rightKey.isPressed()) {
            mc.player.addVelocity(
                -Math.sin(Math.toRadians(mc.player.getYaw() + 90)) * speed.getValue() / 20,
                0,
                Math.cos(Math.toRadians(mc.player.getYaw() + 90)) * speed.getValue() / 20
            );
        }
        
        // Up/down
        if (mc.options.jumpKey.isPressed()) {
            mc.player.addVelocity(0, verticalSpeed.getValue() / 20, 0);
        }
        if (mc.options.sneakKey.isPressed()) {
            mc.player.addVelocity(0, -verticalSpeed.getValue() / 20, 0);
        }
    }
    
    /* TODO: Fix
    private void packetFly() {
        mc.player.setVelocity(0, 0, 0);
        
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
        
        // Movement calculations
        if (mc.options.forwardKey.isPressed()) {
            x -= Math.sin(Math.toRadians(mc.player.getYaw())) * speed.getValue();
            z += Math.cos(Math.toRadians(mc.player.getYaw())) * speed.getValue();
        }
        if (mc.options.backKey.isPressed()) {
            x += Math.sin(Math.toRadians(mc.player.getYaw())) * speed.getValue();
            z -= Math.cos(Math.toRadians(mc.player.getYaw())) * speed.getValue();
        }
        if (mc.options.leftKey.isPressed()) {
            x -= Math.sin(Math.toRadians(mc.player.getYaw() - 90)) * speed.getValue();
            z += Math.cos(Math.toRadians(mc.player.getYaw() - 90)) * speed.getValue();
        }
        if (mc.options.rightKey.isPressed()) {
            x -= Math.sin(Math.toRadians(mc.player.getYaw() + 90)) * speed.getValue();
            z += Math.cos(Math.toRadians(mc.player.getYaw() + 90)) * speed.getValue();
        }
        if (mc.options.jumpKey.isPressed()) {
            y += verticalSpeed.getValue();
        }
        if (mc.options.sneakKey.isPressed()) {
            y -= verticalSpeed.getValue();
        }
        
        // Send position packet - FIXED for 1.21.4
        mc.player.networkHandler.sendPacket(
            PlayerMoveC2SPacket.Full(x, y, z, mc.player.getYaw(), mc.player.getPitch(), false)
        );
        
        mc.player.setPosition(x, y, z);
    } */
}