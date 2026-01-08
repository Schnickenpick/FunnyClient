package com.funnyclient.modules.render;

import com.funnyclient.FunnyClient;
import com.funnyclient.events.RenderEvent;
import com.funnyclient.modules.Module;
import com.funnyclient.settings.BoolSetting;
import com.funnyclient.settings.DoubleSetting;
import com.funnyclient.utils.RenderUtils;
import com.funnyclient.utils.WorldUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;

public class ESP extends Module {
    private final BoolSetting players = new BoolSetting("Players", true);
    private final BoolSetting mobs = new BoolSetting("Mobs", false);
    private final DoubleSetting range = new DoubleSetting("Range", 64.0, 16.0, 256.0, 16.0);
    
    public ESP() {
        super("ESP", "See entities through walls", Category.RENDER);
        getSettings().add(players);
        getSettings().add(mobs);
        getSettings().add(range);
        
        // Register render event
        FunnyClient.INSTANCE.getEventManager().register(RenderEvent.class, this::onRender);
    }
    
    private void onRender(RenderEvent event) {
        if (!isEnabled() || mc.player == null || mc.world == null) return;
        
        if (players.getValue()) {
            for (PlayerEntity player : WorldUtils.getPlayers()) {
                double distance = mc.player.distanceTo(player);
                if (distance > range.getValue()) continue;
                
                // Different colors for different players
                RenderUtils.drawEntityBox(event.getMatrices(), player, 1.0f, 0.0f, 0.0f, 1.0f);
            }
        }
        
        if (mobs.getValue()) {
            for (LivingEntity mob : WorldUtils.getMobs(range.getValue())) {
                if (mob instanceof Monster) {
                    RenderUtils.drawEntityBox(event.getMatrices(), mob, 1.0f, 0.5f, 0.0f, 1.0f);
                }
            }
        }
    }
}
