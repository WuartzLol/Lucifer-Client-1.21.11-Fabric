package com.lucifer.client.setting;

public final class BoolSetting extends Setting<Boolean> {
    public BoolSetting(String name, String description, boolean initial) {
        super(name, description, initial, value -> value != null);
    }
    public void toggle() { set(!get()); }
}
