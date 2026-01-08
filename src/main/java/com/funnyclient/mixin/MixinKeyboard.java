package com.funnyclient.mixin;

import com.funnyclient.FunnyClient;
import com.funnyclient.gui.ClickGuiScreen;
import com.funnyclient.modules.Module;
import net.minecraft.client.Keyboard;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class MixinKeyboard {
    
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == GLFW.GLFW_PRESS && FunnyClient.INSTANCE != null) {
            // Open GUI with RIGHT_SHIFT
            if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                FunnyClient.mc.setScreen(new ClickGuiScreen());
                return;
            }
            
            // Check module keybinds
            for (Module module : FunnyClient.INSTANCE.getModuleManager().getModules()) {
                if (module.getKey() == key) {
                    module.toggle();
                }
            }
        }
    }
}
