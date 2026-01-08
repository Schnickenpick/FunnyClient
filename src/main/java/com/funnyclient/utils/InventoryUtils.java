package com.funnyclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static int findItemSlot(Item item) {
        if (mc.player == null) return -1;
        
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findItemInInventory(Item item) {
        if (mc.player == null) return -1;
        
        for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean hasItem(Item item) {
        return findItemSlot(item) != -1;
    }
    
    public static void switchToSlot(int slot) {
        if (mc.player == null || slot < 0 || slot > 8) return;
        mc.player.getInventory().selectedSlot = slot;
    }
    
    public static int getHotbarSlot() {
        if (mc.player == null) return -1;
        return mc.player.getInventory().selectedSlot;
    }
    
    public static ItemStack getStackInSlot(int slot) {
        if (mc.player == null) return ItemStack.EMPTY;
        return mc.player.getInventory().getStack(slot);
    }
}