package com.example.mixin;

import com.example.FunnyScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientPlayerEntity.class)
public class AutoTotemMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        // Only run if the hack is enabled and we aren't in a menu
        if (!FunnyScreen.autoTotemEnabled || player.currentScreenHandler != player.playerScreenHandler) {
            return;
        }

        // Check if offhand is already a totem
        if (player.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return;
        }

        // Search inventory for a totem
        int totemSlot = findTotemSlot(player);

        if (totemSlot != -1) {
            // 1.21.4 Inventory Packet Logic
            // Slot 45 is the Offhand slot
            // We "Click" the totem slot, then "Click" the offhand slot to swap them
            
            // Pick up the totem
            player.networkHandler.sendPacket(new ClickSlotC2SPacket(
                player.currentScreenHandler.syncId,
                player.currentScreenHandler.getRevision(),
                totemSlot,
                0,
                SlotActionType.PICKUP,
                player.getInventory().getStack(totemSlot).copy(),
                new it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap<>()
            ));

            // Drop it into the offhand (Slot 45)
            player.networkHandler.sendPacket(new ClickSlotC2SPacket(
                player.currentScreenHandler.syncId,
                player.currentScreenHandler.getRevision(),
                45,
                0,
                SlotActionType.PICKUP,
                player.getOffHandStack().copy(),
                new it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap<>()
            ));
        }
    }

    private int findTotemSlot(ClientPlayerEntity player) {
        // Search main inventory (0-35)
        // In ScreenHandler indexing:
        // Slots 0-8 are hotbar (which are 36-44 in container)
        // Slots 9-35 are main inventory
        for (int i = 9; i <= 44; i++) {
            if (player.getInventory().getStack(i < 36 ? i : i - 36).isOf(Items.TOTEM_OF_UNDYING)) {
                return i;
            }
        }
        return -1;
    }
}