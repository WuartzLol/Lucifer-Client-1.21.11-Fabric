package com.lucifer.client.module;

import com.lucifer.client.setting.Setting;
import java.util.ArrayList;
import java.util.List;

public class ClientModule {
    private final String name, description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;

    public ClientModule(String name, String description, Category category) {
        this.name = name; this.description = description; this.category = category;
    }
    public String name() { return name; }
    public String description() { return description; }
    public Category category() { return category; }
    public boolean enabled() { return enabled; }
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }
    public void toggle() { setEnabled(!enabled); }
    public List<Setting<?>> settings() { return List.copyOf(settings); }
    public <T extends Setting<?>> T add(T setting) { settings.add(setting); return setting; }
    protected void onEnable() { }
    protected void onDisable() { }
}
