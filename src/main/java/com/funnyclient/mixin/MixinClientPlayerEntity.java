package com.funnyclient.mixin;

import com.funnyclient.FunnyClient;
import com.funnyclient.events.TickEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onPreTick(CallbackInfo ci) {
        if (FunnyClient.INSTANCE != null) {
            FunnyClient.INSTANCE.getEventManager().post(new TickEvent.Pre());
            FunnyClient.INSTANCE.getModuleManager().onTick();
        }
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void onPostTick(CallbackInfo ci) {
        if (FunnyClient.INSTANCE != null) {
            FunnyClient.INSTANCE.getEventManager().post(new TickEvent.Post());
        }
    }
}