package com.lucifer.client;

import com.lucifer.client.gui.LuciferClickGui;
import com.lucifer.client.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class LuciferClient implements ClientModInitializer {
    public static final String MOD_ID = "lucifer";
    public static final ModuleManager MODULES = new ModuleManager();
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        MODULES.registerDefaults();
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lucifer.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.lucifer.client"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new LuciferClickGui());
                else if (client.currentScreen instanceof LuciferClickGui) client.setScreen(null);
            }
        });
    }
}
