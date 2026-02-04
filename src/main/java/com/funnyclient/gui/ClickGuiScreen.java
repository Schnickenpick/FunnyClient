package com.funnyclient.gui;

import com.funnyclient.FunnyClient;
import com.funnyclient.modules.Module;
import com.funnyclient.modules.Module.Category;
import com.funnyclient.settings.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private final List<CategoryPanel> panels = new ArrayList<>();
    private static final int PANEL_WIDTH = 120;
    private static final int PANEL_SPACING = 10;
    
    public ClickGuiScreen() {
        super(Text.literal("FunnyClient"));
        
        int x = 20;
        for (Category category : Category.values()) {
            panels.add(new CategoryPanel(category, x, 20));
            x += PANEL_WIDTH + PANEL_SPACING;
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // By leaving this empty, we remove both the background tint and the blur shader
        // If you want a slight dark tint WITHOUT blur, uncomment the line below:
        // context.fill(0, 0, this.width, this.height, 0x44000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        
        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY);
        }
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
    
    private static class CategoryPanel {
        private final Category category;
        private int x, y;
        private boolean dragging;
        private int dragX, dragY;
        private boolean expanded = true;
        
        private static final int HEADER_HEIGHT = 16;
        private static final int MODULE_HEIGHT = 14;
        
        public CategoryPanel(Category category, int x, int y) {
            this.category = category;
            this.x = x;
            this.y = y;
        }
        
        public void render(DrawContext context, int mouseX, int mouseY) {
            List<Module> modules = FunnyClient.INSTANCE.getModuleManager()
                .getModulesByCategory(category);
            
            int panelHeight = HEADER_HEIGHT;
            if (expanded) {
                panelHeight += modules.size() * MODULE_HEIGHT;
                
                for (Module module : modules) {
                    if (module.isExpanded()) {
                        panelHeight += module.getSettings().size() * 12;
                    }
                }
            }
            
            // Panel background
            context.fill(x, y, x + PANEL_WIDTH, y + panelHeight, 0xD0000000);
            
            // Header
            context.fill(x, y, x + PANEL_WIDTH, y + HEADER_HEIGHT, 
                isMouseOver(mouseX, mouseY, x, y, PANEL_WIDTH, HEADER_HEIGHT) ? 
                0xFF404040 : 0xFF202020);
            
            // Category name
            context.drawText(FunnyClient.mc.textRenderer, category.getName(), 
                x + 4, y + 4, 0xFFFFFF, true);
            
            if (!expanded) return;
            
            int moduleY = y + HEADER_HEIGHT;
            for (Module module : modules) {
                boolean hovered = isMouseOver(mouseX, mouseY, x, moduleY, PANEL_WIDTH, MODULE_HEIGHT);
                
                int bgColor = module.isEnabled() ? 0xFF505050 : 
                             hovered ? 0xFF303030 : 0xFF252525;
                context.fill(x, moduleY, x + PANEL_WIDTH, moduleY + MODULE_HEIGHT, bgColor);
                
                int textColor = module.isEnabled() ? 0x00FF00 : 0xFFFFFF;
                context.drawText(FunnyClient.mc.textRenderer, module.getName(), 
                    x + 4, moduleY + 3, textColor, true);
                
                moduleY += MODULE_HEIGHT;
                
                if (module.isExpanded()) {
                    for (Setting<?> setting : module.getSettings()) {
                        context.fill(x + 2, moduleY, x + PANEL_WIDTH - 2, moduleY + 12, 0xFF1A1A1A);
                        
                        if (setting instanceof BoolSetting) {
                            BoolSetting bool = (BoolSetting) setting;
                            int color = bool.getValue() ? 0x00FF00 : 0xFF0000;
                            context.drawText(FunnyClient.mc.textRenderer, 
                                setting.getName() + ": " + bool.getValue(), 
                                x + 6, moduleY + 2, color, false);
                        } 
                        else if (setting instanceof DoubleSetting) {
                            DoubleSetting doub = (DoubleSetting) setting;
                            context.drawText(FunnyClient.mc.textRenderer, 
                                setting.getName() + ": " + String.format("%.1f", doub.getValue()), 
                                x + 6, moduleY + 2, 0xAAAAAA, false);
                        }
                        else if (setting instanceof ModeSetting) {
                            ModeSetting mode = (ModeSetting) setting;
                            context.drawText(FunnyClient.mc.textRenderer, 
                                setting.getName() + ": " + mode.getValue(), 
                                x + 6, moduleY + 2, 0x00FFFF, false);
                        }
                        
                        moduleY += 12;
                    }
                }
            }
        }
        
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isMouseOver(mouseX, mouseY, x, y, PANEL_WIDTH, HEADER_HEIGHT)) {
                if (button == 0) {
                    dragging = true;
                    dragX = (int) (mouseX - x);
                    dragY = (int) (mouseY - y);
                } else if (button == 1) {
                    expanded = !expanded;
                }
                return true;
            }
            
            if (!expanded) return false;
            
            List<Module> modules = FunnyClient.INSTANCE.getModuleManager()
                .getModulesByCategory(category);
            
            int moduleY = y + HEADER_HEIGHT;
            for (Module module : modules) {
                if (isMouseOver(mouseX, mouseY, x, moduleY, PANEL_WIDTH, MODULE_HEIGHT)) {
                    if (button == 0) {
                        module.toggle();
                    } else if (button == 1) {
                        module.setExpanded(!module.isExpanded());
                    }
                    return true;
                }
                
                moduleY += MODULE_HEIGHT;
                
                if (module.isExpanded()) {
                    for (Setting<?> setting : module.getSettings()) {
                        if (isMouseOver(mouseX, mouseY, x + 2, moduleY, PANEL_WIDTH - 4, 12)) {
                            if (button == 0) {
                                if (setting instanceof BoolSetting) {
                                    ((BoolSetting) setting).toggle();
                                } else if (setting instanceof ModeSetting) {
                                    ((ModeSetting) setting).cycle();
                                }
                            }
                            return true;
                        }
                        moduleY += 12;
                    }
                }
            }
            
            return false;
        }
        
        public void mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0) {
                dragging = false;
            }
        }
        
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            if (dragging) {
                x = (int) (mouseX - dragX);
                y = (int) (mouseY - dragY);
                return true;
            }
            return false;
        }
        
        private boolean isMouseOver(double mouseX, double mouseY, int x, int y, int width, int height) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }
}