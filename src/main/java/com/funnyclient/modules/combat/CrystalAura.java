package com.funnyclient.modules.combat;

import com.funnyclient.modules.Module;
import com.funnyclient.settings.BoolSetting;
import com.funnyclient.settings.DoubleSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class CrystalAura extends Module {
    private final DoubleSetting range = new DoubleSetting("Range", 5.0, 1.0, 6.0, 0.1);
    private final DoubleSetting placeRange = new DoubleSetting("Place Range", 4.5, 1.0, 6.0, 0.1);
    private final BoolSetting autoSwitch = new BoolSetting("Auto Switch", true);
    private final BoolSetting rotate = new BoolSetting("Rotate", true);
    private final DoubleSetting minDamage = new DoubleSetting("Min Damage", 6.0, 0.0, 20.0, 0.5);
    private final DoubleSetting maxSelfDamage = new DoubleSetting("Max Self Damage", 8.0, 0.0, 20.0, 0.5);
    
    public CrystalAura() {
        super("Crystal Aura", "Automatically places and breaks end crystals", Category.COMBAT);
        getSettings().add(range);
        getSettings().add(placeRange);
        getSettings().add(autoSwitch);
        getSettings().add(rotate);
        getSettings().add(minDamage);
        getSettings().add(maxSelfDamage);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        
        // Break existing crystals
        List<EndCrystalEntity> crystals = getCrystalsInRange();
        for (EndCrystalEntity crystal : crystals) {
            if (shouldAttack(crystal)) {
                attackCrystal(crystal);
                return;
            }
        }
        
        // Place new crystals
        PlayerEntity target = getTarget();
        if (target != null) {
            BlockPos placePos = findBestPlacePos(target);
            if (placePos != null) {
                placeCrystal(placePos);
            }
        }
    }
    
    private List<EndCrystalEntity> getCrystalsInRange() {
        List<EndCrystalEntity> crystals = new ArrayList<>();
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof EndCrystalEntity) {
                double distance = mc.player.squaredDistanceTo(entity);
                if (distance <= range.getValue() * range.getValue()) {
                    crystals.add((EndCrystalEntity) entity);
                }
            }
        }
        return crystals;
    }
    
    private boolean shouldAttack(EndCrystalEntity crystal) {
        PlayerEntity target = getTarget();
        if (target == null) return true;
        
        double targetDamage = calculateDamage(crystal.getPos(), target);
        double selfDamage = calculateDamage(crystal.getPos(), mc.player);
        
        return targetDamage >= minDamage.getValue() && selfDamage <= maxSelfDamage.getValue();
    }
    
    private void attackCrystal(EndCrystalEntity crystal) {
        if (rotate.getValue()) {
            rotateTo(crystal.getPos());
        }
        
        mc.player.networkHandler.sendPacket(
            PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking())
        );
        mc.player.swingHand(Hand.MAIN_HAND);
    }
    
    private BlockPos findBestPlacePos(PlayerEntity target) {
        BlockPos bestPos = null;
        double bestDamage = 0;
        
        BlockPos playerPos = mc.player.getBlockPos();
        
        for (int x = -5; x <= 5; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -5; z <= 5; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    
                    if (!canPlaceCrystal(pos)) continue;
                    
                    double distance = mc.player.squaredDistanceTo(
                        pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5
                    );
                    
                    if (distance > placeRange.getValue() * placeRange.getValue()) continue;
                    
                    Vec3d crystalPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                    double targetDamage = calculateDamage(crystalPos, target);
                    double selfDamage = calculateDamage(crystalPos, mc.player);
                    
                    if (targetDamage >= minDamage.getValue() && 
                        selfDamage <= maxSelfDamage.getValue() &&
                        targetDamage > bestDamage) {
                        bestDamage = targetDamage;
                        bestPos = pos;
                    }
                }
            }
        }
        
        return bestPos;
    }
    
    private boolean canPlaceCrystal(BlockPos pos) {
        if (!mc.world.getBlockState(pos).getBlock().equals(net.minecraft.block.Blocks.OBSIDIAN) &&
            !mc.world.getBlockState(pos).getBlock().equals(net.minecraft.block.Blocks.BEDROCK)) {
            return false;
        }
        
        BlockPos above = pos.up();
        BlockPos above2 = pos.up(2);
        
        if (!mc.world.getBlockState(above).isAir() || !mc.world.getBlockState(above2).isAir()) {
            return false;
        }
        
        Box box = new Box(above);
        return mc.world.getOtherEntities(null, box).isEmpty();
    }
    
    private void placeCrystal(BlockPos pos) {
        int crystalSlot = findCrystalSlot();
        if (crystalSlot == -1) return;
        
        int oldSlot = mc.player.getInventory().selectedSlot;
        
        if (autoSwitch.getValue() && crystalSlot != oldSlot) {
            mc.player.getInventory().selectedSlot = crystalSlot;
        }
        
        if (rotate.getValue()) {
            Vec3d vec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            rotateTo(vec);
        }
        
        BlockHitResult hitResult = new BlockHitResult(
            new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5),
            Direction.UP,
            pos,
            false
        );
        
        mc.interactionManager.interactBlock(
            mc.player,
            Hand.MAIN_HAND,
            hitResult
        );
        
        mc.player.swingHand(Hand.MAIN_HAND);
        
        if (autoSwitch.getValue() && crystalSlot != oldSlot) {
            mc.player.getInventory().selectedSlot = oldSlot;
        }
    }
    
    private int findCrystalSlot() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }
    
    private PlayerEntity getTarget() {
        PlayerEntity target = null;
        double closestDistance = Double.MAX_VALUE;
        
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (player.isDead() || player.getHealth() <= 0) continue;
            
            double distance = mc.player.distanceTo(player);
            if (distance < range.getValue() && distance < closestDistance) {
                closestDistance = distance;
                target = player;
            }
        }
        
        return target;
    }
    
    private double calculateDamage(Vec3d crystalPos, PlayerEntity target) {
        double distance = target.getPos().distanceTo(crystalPos);
        if (distance > 12) return 0;
        
        double exposure = 1.0;
        double impact = (1.0 - distance / 12.0) * exposure;
        double damage = (impact * impact + impact) * 7.0 * 12.0 + 1.0;
        
        return damage;
    }
    
    private void rotateTo(Vec3d vec) {
        double diffX = vec.x - mc.player.getX();
        double diffY = vec.y - (mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()));
        double diffZ = vec.z - mc.player.getZ();
        
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        
        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }
}