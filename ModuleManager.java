package com.lucifer.client.module;

import com.lucifer.client.setting.BoolSetting;
import com.lucifer.client.setting.NumberSetting;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Registry for independent, client-side information and presentation modules. */
public final class ModuleManager {
    private final List<ClientModule> modules = new ArrayList<>();

    public void registerDefaults() {
        // COMBAT: informational tools; no automated attacks or server manipulation.
        register(new ClientModule("Combat HUD", "Shows configurable combat information widgets.", Category.COMBAT)
                {{ add(new BoolSetting("Show target panel", "Display a target information panel when available.", true));
                   add(new NumberSetting("Scale", "Widget scale multiplier.", 1.0, 0.5, 1.8, 0.1)); }});
        register(new ClientModule("Crystal Reference", "Displays configurable CPvP reference ranges.", Category.COMBAT)
                {{ add(new NumberSetting("Reference range", "A visual reference only; does not change reach.", 4.0, 1.0, 8.0, 0.25));
                   add(new BoolSetting("Show reference", "Show the range reference in the HUD.", true)); }});
        register(new ClientModule("Equipment Status", "Configuration for armor and totem status widgets.", Category.COMBAT)
                {{ add(new BoolSetting("Compact mode", "Use a compact status layout.", false)); }});

        // MOVEMENT: local diagnostics only.
        register(new ClientModule("Sprint Status", "Shows the current local sprint state.", Category.MOVEMENT)
                {{ add(new BoolSetting("Show icon", "Display a small status icon.", true)); }});
        register(new ClientModule("Motion Monitor", "Configures local movement diagnostics.", Category.MOVEMENT)
                {{ add(new NumberSetting("Refresh rate", "UI refresh interval in ticks.", 2.0, 1.0, 10.0, 1.0)); }});
        register(new ClientModule("Direction HUD", "Configures heading and coordinate display.", Category.MOVEMENT)
                {{ add(new BoolSetting("Show coordinates", "Display local coordinates in the HUD.", true)); }});

        // RENDER: client-side visual preferences.
        register(new ClientModule("Crosshair Studio", "Customizes crosshair presentation settings.", Category.RENDER)
                {{ add(new BoolSetting("Dynamic pulse", "Enable a subtle visual pulse preference.", true));
                   add(new NumberSetting("Gap", "Crosshair center gap in pixels.", 3.0, 0.0, 10.0, 1.0)); }});
        register(new ClientModule("Overlay Tuning", "Configures optional entity-information overlay limits.", Category.RENDER)
                {{ add(new NumberSetting("Overlay distance", "Maximum distance for supported overlays.", 48.0, 8.0, 128.0, 4.0));
                   add(new NumberSetting("Opacity", "Overlay opacity setting.", 0.75, 0.1, 1.0, 0.05)); }});
        register(new ClientModule("Dropped Item Labels", "Configures dropped-item label preferences.", Category.RENDER)
                {{ add(new BoolSetting("Show distance", "Include distance in supported labels.", true)); }});

        // HUD.
        register(new ClientModule("Array List", "Lists enabled Lucifer modules on the HUD.", Category.HUD)
                {{ add(new BoolSetting("Show descriptions", "Include short descriptions.", false)); }});
        register(new ClientModule("Watermark", "Displays Lucifer Client branding preferences.", Category.HUD)
                {{ add(new BoolSetting("Show version", "Include the client version label.", true)); }});
        register(new ClientModule("Performance HUD", "Configures FPS and memory diagnostic display.", Category.HUD)
                {{ add(new BoolSetting("Show frame time", "Include frame-time diagnostics.", true)); }});

        // UTILITY.
        register(new ClientModule("Notifications", "Configures non-intrusive local status notifications.", Category.UTILITY)
                {{ add(new BoolSetting("Enable notifications", "Allow supported status notifications.", true));
                   add(new NumberSetting("Display seconds", "Notification lifetime in seconds.", 3.0, 1.0, 10.0, 0.5)); }});
        register(new ClientModule("Session Notes", "Enables preferences for local session notes.", Category.UTILITY)
                {{ add(new BoolSetting("Timestamp notes", "Include a local timestamp in notes.", true)); }});
        register(new ClientModule("Debug Overlay", "Configures local diagnostic information.", Category.UTILITY)
                {{ add(new BoolSetting("Verbose", "Include extra diagnostic labels.", false)); }});

        // PERFORMANCE: controls for this mod's own optional work only.
        register(new ClientModule("Overlay Budget", "Sets a conservative budget for optional overlay work.", Category.PERFORMANCE)
                {{ add(new NumberSetting("Update interval", "Target update interval in ticks.", 2.0, 1.0, 20.0, 1.0));
                   add(new NumberSetting("Entity cap", "Maximum entities considered by supported overlays.", 32.0, 4.0, 128.0, 4.0)); }});
        register(new ClientModule("Reduced Animation", "Configures lightweight interface animation behavior.", Category.PERFORMANCE)
                {{ add(new BoolSetting("Reduce animations", "Prefer reduced motion in the interface.", false)); }});
        register(new ClientModule("Diagnostics", "Configures optional performance diagnostics.", Category.PERFORMANCE)
                {{ add(new BoolSetting("Show allocation hints", "Show basic diagnostic hints where available.", false)); }});
    }

    public void register(ClientModule module) {
        if (find(module.name()).isPresent()) throw new IllegalArgumentException("Duplicate module: " + module.name());
        modules.add(module);
    }
    public List<ClientModule> all() { return List.copyOf(modules); }
    public List<ClientModule> in(Category category) { return modules.stream().filter(m -> m.category() == category).toList(); }
    public Optional<ClientModule> find(String name) { return modules.stream().filter(m -> m.name().equalsIgnoreCase(name)).findFirst(); }
}
