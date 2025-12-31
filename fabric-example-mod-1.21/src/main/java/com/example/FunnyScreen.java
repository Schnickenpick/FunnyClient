
package com.example;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.component.SliderComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;


import org.jetbrains.annotations.NotNull;


public class FunnyScreen extends BaseOwoScreen<FlowLayout> {

    // Change 'private' to 'public' so the Mixin can see it!

    
    private final int neonGreen = 0xFF00FF00;

    public static boolean nukerEnabled = false; //Nuker
    public static boolean scaffoldEnabled = false; //scaffold
    public static boolean sprintEnabled = false; //auto sprint
    public static boolean jesusEnabled = false; //jesus (walk on water)
    public static boolean spiderEnabled = false; //spider
    public static boolean espEnabled =false;     //esp
    public static boolean tracersEnabled = false; //tracers
    public static boolean xrayEnabled = false; //xray
    public static boolean fullBrightEnabled = false; //fullbright
    public static boolean autoTotemEnabled = false; //autototem
    public static boolean velocityEnabled = false; //velocity
    public static boolean reachEnabled = false; //reach
    public static boolean autoCritEnabled = false; //criticals
    public static boolean killAuraEnabled = false; //killaura
    public static double killAuraRange = 3.5;
    public static boolean crystalAuraEnabled = false; //crystal aura
    public static double crystalAuraSpeed = 10.0; // Crystals per second
    public static boolean flyEnabled = false; //flight
    public static double flySpeedValue = 1.0; 
    public static double speedValue = 1.0; //speed
    public static boolean stepEnabled = false; //flashstep
    public static double stepHeightValue = 0.6;
    public static boolean noFallEnabled = false; //nofall

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.blur(5, 5))
            .horizontalAlignment(HorizontalAlignment.CENTER)
            .verticalAlignment(VerticalAlignment.CENTER);

        showMainMenu(rootComponent);
    }
    private void showMainMenu(FlowLayout root) {
        root.clearChildren();
        var menuBox = createBaseBox();
        
        menuBox.child(Components.label(Text.literal("FUNNY CLIENT")).color(Color.ofArgb(neonGreen)).margins(Insets.bottom(15)));
        
        // UPDATE THIS LINE:
        menuBox.child(createStyledButton("Combat", neonGreen, b -> showCombatMenu(root)));
        
        menuBox.child(createStyledButton("Movement", neonGreen, b -> showMovementMenu(root)));
        menuBox.child(createStyledButton("Visuals", neonGreen, b -> showVisualsMenu(root)));

        menuBox.child(Containers.verticalFlow(Sizing.fill(0), Sizing.expand(100)));
        menuBox.child(createStyledButton("Close", 0xFFFF0000, b -> this.close()));

        root.child(menuBox);
    }

        private void showVisualsMenu(FlowLayout root) {
        root.clearChildren();

        // 1. The Scrollable Content
        var content = Containers.verticalFlow(Sizing.fixed(180), Sizing.content());
        content.padding(Insets.of(10)).horizontalAlignment(HorizontalAlignment.CENTER);

        content.child(Components.label(Text.literal("COMBAT")).color(Color.ofArgb(neonGreen)).margins(Insets.bottom(10)));

        // --- Fullbright ---
        content.child(createStyledButton("Fullbright: " + (fullBrightEnabled ? "ON" : "OFF"), 
            fullBrightEnabled ? neonGreen : 0xFFFF0000, b -> {
                fullBrightEnabled = !fullBrightEnabled;
                if (fullBrightEnabled) {
                    // We force the value to 1000 (standard is 0.0 to 1.0)
                    client.options.getGamma().setValue(1000.0);
                } else {
                    // Reset to normal
                    client.options.getGamma().setValue(1.0);
                }
                showVisualsMenu(root);
            }));


        // --- Esp ---
        content.child(createStyledButton("ESP: " + (espEnabled ? "ON" : "OFF"), 
            espEnabled? neonGreen : 0xFFFF0000, b -> {
                espEnabled = !espEnabled;
                showVisualsMenu(root);
            }));

            // --- tracers ---
        content.child(createStyledButton("Tracers: " + (tracersEnabled ? "ON" : "OFF"), 
            tracersEnabled? neonGreen : 0xFFFF0000, b -> {
                tracersEnabled = !tracersEnabled;
                showVisualsMenu(root);
            }));


        // --- BACK BUTTON ---
        content.child(createStyledButton("Back", neonGreen, b -> showMainMenu(root)));

        // 2. The Scroll Window
        var scrollContainer = Containers.verticalScroll(Sizing.fixed(200), Sizing.fixed(250), content);
        scrollContainer.padding(Insets.of(2)).surface(Surface.flat(0x90000000).and(Surface.outline(neonGreen)));

        root.child(scrollContainer);
    }


    private void showCombatMenu(FlowLayout root) {
        root.clearChildren();

        // 1. The Scrollable Content
        var content = Containers.verticalFlow(Sizing.fixed(180), Sizing.content());
        content.padding(Insets.of(10)).horizontalAlignment(HorizontalAlignment.CENTER);

        content.child(Components.label(Text.literal("COMBAT")).color(Color.ofArgb(neonGreen)).margins(Insets.bottom(10)));

        // --- KILL AURA ---
        content.child(createStyledButton("KillAura: " + (killAuraEnabled ? "ON" : "OFF"), 
            killAuraEnabled ? neonGreen : 0xFFFF0000, b -> {
                killAuraEnabled = !killAuraEnabled;
                showCombatMenu(root);
            }));

        // Range Slider for KillAura
        var rangeLabel = Components.label(Text.literal("Reach: " + String.format("%.1f", killAuraRange)));
        content.child(rangeLabel);
        var rangeSlider = Components.slider(Sizing.fill(90));
        rangeSlider.value((killAuraRange - 3.0) / 3.0); 
        rangeSlider.onChanged().subscribe(val -> {
            killAuraRange = 3.0 + (val * 3.0);
            rangeLabel.text(Text.literal("Reach: " + String.format("%.1f", killAuraRange)));
        });
        content.child(rangeSlider);

        // --- Auto Totem ---
        content.child(createStyledButton("AutoTotem: " + (autoTotemEnabled ? "ON" : "OFF"), 
            autoTotemEnabled ? neonGreen : 0xFFFF0000, b -> {
                autoTotemEnabled = !autoTotemEnabled;
                showCombatMenu(root);
            }));

            // --- No knockback ---
        content.child(createStyledButton("Velocity: " + (velocityEnabled ? "ON" : "OFF"), 
            velocityEnabled ? neonGreen : 0xFFFF0000, b -> {
                velocityEnabled = !velocityEnabled;
                showCombatMenu(root);
            }));

        // --- Reach ---
        content.child(createStyledButton("Reach (6): " + (reachEnabled ? "ON" : "OFF"), 
            reachEnabled ? neonGreen : 0xFFFF0000, b -> {
                reachEnabled = !reachEnabled;
                showCombatMenu(root);
        }));


        // --- CRYSTAL AURA ---
        content.child(createStyledButton("CrystalAura: " + (crystalAuraEnabled ? "ON" : "OFF"), 
            crystalAuraEnabled ? neonGreen : 0xFFFF0000, b -> {
                crystalAuraEnabled = !crystalAuraEnabled;
                showCombatMenu(root);
        }));

        // Speed Slider for CrystalAura
        var caLabel = Components.label(Text.literal("CA Speed: " + (int)crystalAuraSpeed));
        content.child(caLabel);
        var caSlider = Components.slider(Sizing.fill(90));
        caSlider.value((crystalAuraSpeed - 1) / 19.0);
        caSlider.onChanged().subscribe(val -> {
            crystalAuraSpeed = 1.0 + (val * 19.0);
            caLabel.text(Text.literal("CA Speed: " + (int)crystalAuraSpeed));
        });
        content.child(caSlider);

        // --- AUTO CRIT (The missing button!) ---
        content.child(createStyledButton("Auto-Crit: " + (autoCritEnabled ? "ON" : "OFF"), 
            autoCritEnabled ? neonGreen : 0xFFFF0000, b -> {
                autoCritEnabled = !autoCritEnabled;
                showCombatMenu(root);
        }));

        // --- Nuker ---
        content.child(createStyledButton("Nuker (CREATIVE): " + (nukerEnabled ? "ON" : "OFF"), 
            nukerEnabled ? neonGreen : 0xFFFF0000, b -> {
                nukerEnabled = !nukerEnabled;
                showCombatMenu(root);
        }));

        // --- BACK BUTTON ---
        content.child(createStyledButton("Back", neonGreen, b -> showMainMenu(root)));

        // 2. The Scroll Window
        var scrollContainer = Containers.verticalScroll(Sizing.fixed(200), Sizing.fixed(250), content);
        scrollContainer.padding(Insets.of(2)).surface(Surface.flat(0x90000000).and(Surface.outline(neonGreen)));

        root.child(scrollContainer);
    }
   
    private void showMovementMenu(FlowLayout root) {
        root.clearChildren();

        // 1. Create the inner content box (this can be as tall as needed)
        // Sizing.content() allows it to expand based on the number of buttons
        var content = Containers.verticalFlow(Sizing.fixed(180), Sizing.content());
        content.padding(Insets.of(10)).horizontalAlignment(HorizontalAlignment.CENTER);

        // 2. Add all your components to 'content' instead of 'menuBox'
        content.child(Components.label(Text.literal("MOVEMENT")).color(Color.ofArgb(neonGreen)).margins(Insets.bottom(10)));
        
        // ... Add all your buttons/sliders here (Speed, Flight, Step, Spider, etc.) ...
        // [Add the rest of your movement buttons to 'content' here]
        content.child(Components.label(Text.literal("Speed Value:")).horizontalSizing(Sizing.fill(90)));

        // 1. Text Input Box
        TextBoxComponent speedInput = Components.textBox(Sizing.fill(90));
        speedInput.setText(String.format("%.2f", speedValue));
        speedInput.margins(Insets.vertical(5));
        content.child(speedInput);

        // 2. The Slider
        SliderComponent slider = Components.slider(Sizing.fill(90));
        slider.value((speedValue - 1) / 9.0);
        
        // Fix: In this version, onChanged uses the SliderComponent.OnChanged interface
        slider.onChanged().subscribe(value -> {
            speedValue = 1.0 + (value * 9.0);
            speedInput.setText(String.format("%.2f", speedValue));
        });
        
        content.child(slider);
        //Flight
        content.child(createStyledButton("Flight: " + (flyEnabled ? "ON" : "OFF"), 
            flyEnabled ? neonGreen : 0xFFFF0000, b -> {
            flyEnabled = !flyEnabled;
            showMovementMenu(root); // Refresh UI to update text/color
        }));

                // 1. Create the label first so we can update it
        var flySpeedLabel = Components.label(Text.literal("Fly Speed: " + String.format("%.1f", flySpeedValue)));
        content.child(flySpeedLabel);

        // 2. Create the slider
        var flySlider = Components.slider(Sizing.fill(90));

        // 3. Set the initial position (Reverse math: (Current - Min) / (Max - Min))
        flySlider.value((flySpeedValue - 1.0) / 9.0);

        // 4. Set the listener for live updates
        flySlider.onChanged().subscribe(val -> {
            // Math: MinValue + (SliderPercent * Range)
            flySpeedValue = 1.0 + (val * 9.0);
            
            // Update the label text LIVE as you drag
            flySpeedLabel.text(Text.literal("Fly Speed: " + String.format("%.1f", flySpeedValue)));
        });

        content.child(flySlider);

        //Flash step
        content.child(createStyledButton("Step: " + (stepEnabled ? "ON" : "OFF"), 
            stepEnabled ? neonGreen : 0xFFFF0000, b -> {
            stepEnabled = !stepEnabled;
            showMovementMenu(root); // Refresh UI to update text/color
        }));

        // --- STEP HEIGHT SLIDER (1 - 20) ---
        content.child(Components.label(Text.literal("Step Height: " + String.format("%.1f", stepHeightValue))).horizontalSizing(Sizing.fill(90)));
        SliderComponent stepSlider = Components.slider(Sizing.fill(90));
        stepSlider.value((stepHeightValue - 1) / 19.0); // Map 1-20 range
        stepSlider.onChanged().subscribe(value -> {
            stepHeightValue = 1.0 + (value * 19.0);
            // Note: We'll need to refresh a label or just let the slider handle the internal value
        });
        content.child(stepSlider);

                // --- SPIDER TOGGLE ---
        content.child(createStyledButton("Spider: " + (spiderEnabled ? "ON" : "OFF"), 
        spiderEnabled ? neonGreen : 0xFFFF0000, b -> {
        spiderEnabled = !spiderEnabled;
                showMovementMenu(root);
            }));

            // --- NOFALL TOGGLE ---
        content.child(createStyledButton("NoFall: " + (noFallEnabled ? "ON" : "OFF"), 
        noFallEnabled ? neonGreen : 0xFFFF0000, b -> {
        noFallEnabled = !noFallEnabled;
            showMovementMenu(root);
        }));
        // --- Jesus ---
        content.child(createStyledButton("Jesus: " + (jesusEnabled ? "ON" : "OFF"), 
            jesusEnabled ? neonGreen : 0xFFFF0000, b -> {
            jesusEnabled = !jesusEnabled;
            showMovementMenu(root); // Refresh UI to update text/color
        }));

        // -- scaffold
        content.child(createStyledButton("Scaffold: " + (scaffoldEnabled ? "ON" : "OFF"), 
            scaffoldEnabled ? neonGreen : 0xFFFF0000, b -> {
            scaffoldEnabled = !scaffoldEnabled;
            showMovementMenu(root); // Refresh UI to update text/color
        }));


        content.child(createStyledButton("Back", neonGreen, b -> showMainMenu(root)));

        // 3. Create the Scroll Container (the "Window")
        // We give it a fixed height so it doesn't go off-screen
        var scrollContainer = Containers.verticalScroll(
            Sizing.fixed(200), // Width
            Sizing.fixed(250), // Height (the size of the visible window)
            content            // The actual content to scroll
        );

        // 4. Style the container to look like your original box
        scrollContainer.padding(Insets.of(2))
                    .surface(Surface.flat(0x90000000).and(Surface.outline(neonGreen)));

        root.child(scrollContainer);
    }


        

    private FlowLayout createBaseBox() {
        var box = Containers.verticalFlow(Sizing.fixed(200), Sizing.fixed(280));
        box.padding(Insets.of(12))
           .surface(Surface.flat(0x90000000).and(Surface.outline(neonGreen)))
           .horizontalAlignment(HorizontalAlignment.CENTER);
        return box;
    }

    private ButtonComponent createStyledButton(String text, int color, java.util.function.Consumer<ButtonComponent> onPress) {
        ButtonComponent button = (ButtonComponent) Components.button(Text.literal(text), onPress);
        button.renderer(ButtonComponent.Renderer.flat(0x40000000, color, color));
        button.sizing(Sizing.fill(90), Sizing.fixed(25)).margins(Insets.vertical(3));
        return button;
    }
}