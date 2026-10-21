package com.lucifer.client.gui;

import com.lucifer.client.LuciferClient;
import com.lucifer.client.module.Category;
import com.lucifer.client.module.ClientModule;
import com.lucifer.client.setting.BoolSetting;
import com.lucifer.client.setting.NumberSetting;
import com.lucifer.client.setting.Setting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public final class LuciferClickGui extends Screen {
    private static final int BG = 0xF20B0B10;
    private static final int PANEL = 0xF218181F;
    private static final int CARD = 0xFF202029;
    private static final int CARD_HOVER = 0xFF2B2028;
    private static final int RED = 0xFFFF334F;
    private static final int RED_DARK = 0xFF9E142A;
    private static final int TEXT = 0xFFF4F0F3;
    private static final int MUTED = 0xFF9A939D;
    private Category selectedCategory = Category.COMBAT;
    private ClientModule selectedModule;
    private String search = "";
    private long openedAt = System.currentTimeMillis();
    private int panelX, panelY, panelW, panelH;
    private int listX, listY, listW;

    public LuciferClickGui() { super(Text.literal("Lucifer Client")); }

    @Override
    protected void init() {
        panelW = Math.min(860, width - 32);
        panelH = Math.min(520, height - 32);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        listX = panelX + 190;
        listY = panelY + 102;
        listW = panelW - 218;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(0, 0, width, height, 0x99030008);
        drawPanel(ctx, mouseX, mouseY);
        drawSidebar(ctx, mouseX, mouseY);
        drawHeader(ctx);
        if (selectedModule == null) drawModuleGrid(ctx, mouseX, mouseY);
        else drawSettings(ctx, mouseX, mouseY);
        super.render(ctx, mouseX, mouseY, delta);
    }

    private void drawPanel(DrawContext ctx, int mx, int my) {
        ctx.fill(panelX - 1, panelY - 1, panelX + panelW + 1, panelY + panelH + 1, 0xAA5E1020);
        ctx.fill(panelX, panelY, panelX + panelW, panelY + panelH, BG);
        ctx.fill(panelX, panelY, panelX + panelW, panelY + 3, RED);
        ctx.fill(panelX, panelY + 3, panelX + panelW, panelY + 4, RED_DARK);
        ctx.fill(panelX + 1, panelY + 1, panelX + panelW - 1, panelY + panelH - 1, 0x101FFFFFF);
        // Subtle animated crimson scan lines and particles: deterministic, allocation-free.
        long t = System.currentTimeMillis() - openedAt;
        int sweep = (int) ((t / 12L) % Math.max(1, panelW));
        ctx.fill(panelX + sweep, panelY + 4, Math.min(panelX + panelW, panelX + sweep + 2), panelY + panelH - 2, 0x12FF2447);
        for (int i = 0; i < 18; i++) {
            int px = panelX + 210 + (i * 97 % Math.max(1, panelW - 220));
            int py = panelY + 70 + (i * 61 % Math.max(1, panelH - 85));
            int alpha = 0x16 + (int) ((Math.sin((t / 500.0) + i) + 1) * 10);
            ctx.fill(px, py, px + 2, py + 2, (alpha << 24) | 0x00FF334F);
        }
        ctx.fill(panelX + 174, panelY + 64, panelX + 175, panelY + panelH - 18, 0xFF34242C);
    }

    private void drawHeader(DrawContext ctx) {
        ctx.fill(panelX + 22, panelY + 22, panelX + 52, panelY + 52, 0xFF35111B);
        ctx.fill(panelX + 22, panelY + 22, panelX + 25, panelY + 52, RED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("L"), panelX + 31, panelY + 27, TEXT);
        ctx.drawTextWithShadow(textRenderer, Text.literal("LUCIFER"), panelX + 62, panelY + 21, TEXT);
        ctx.drawTextWithShadow(textRenderer, Text.literal("CLIENT  /  1.21.11"), panelX + 62, panelY + 36, MUTED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("RIGHT SHIFT  •  CLOSE"), panelX + panelW - 148, panelY + 31, MUTED);
        ctx.fill(panelX + 190, panelY + 69, panelX + panelW - 18, panelY + 70, 0xFF34242C);
        ctx.drawTextWithShadow(textRenderer, Text.literal(selectedCategory.title().toUpperCase()), listX, panelY + 82, RED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Modular client interface"), listX + 92, panelY + 82, MUTED);
    }

    private void drawSidebar(DrawContext ctx, int mx, int my) {
        Category[] values = Category.values();
        int y = panelY + 94;
        for (int i = 0; i < values.length; i++) {
            int top = y + i * 37;
            boolean active = selectedCategory == values[i];
            boolean hover = mx >= panelX + 16 && mx <= panelX + 160 && my >= top && my <= top + 36;
            if (active) {
                ctx.fill(panelX + 16, top, panelX + 160, top + 36, 0xFF31141D);
                ctx.fill(panelX + 16, top, panelX + 19, top + 36, RED);
            } else if (hover) ctx.fill(panelX + 16, top, panelX + 160, top + 36, 0xFF211B22);
            ctx.drawTextWithShadow(textRenderer, Text.literal(values[i].title().toUpperCase()), panelX + 29, top + 13, active ? TEXT : MUTED);
            ctx.drawTextWithShadow(textRenderer, Text.literal(String.format("%02d", LuciferClient.MODULES.in(values[i]).size())), panelX + 133, top + 13, active ? RED : 0xFF655B66);
        }
        ctx.drawTextWithShadow(textRenderer, Text.literal("PROFILE"), panelX + 22, panelY + panelH - 58, MUTED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("CRIMSON  /  DEFAULT"), panelX + 22, panelY + panelH - 41, TEXT);
        ctx.drawTextWithShadow(textRenderer, Text.literal("BUILD 01"), panelX + 22, panelY + panelH - 24, RED);
    }

    private void drawModuleGrid(DrawContext ctx, int mx, int my) {
        List<ClientModule> modules = LuciferClient.MODULES.in(selectedCategory);
        int cols = listW > 420 ? 2 : 1;
        int gap = 10;
        int cardW = (listW - gap * (cols - 1)) / cols;
        int cardH = 76;
        int shown = 0;
        for (ClientModule module : modules) {
            if (!module.name().toLowerCase().contains(search.toLowerCase())) continue;
            int col = shown % cols;
            int row = shown / cols;
            int x = listX + col * (cardW + gap);
            int y = listY + row * (cardH + gap);
            boolean hover = mx >= x && mx <= x + cardW && my >= y && my <= y + cardH;
            ctx.fill(x, y, x + cardW, y + cardH, hover ? CARD_HOVER : CARD);
            ctx.fill(x, y, x + cardW, y + 1, module.enabled() ? RED : 0xFF38313A);
            ctx.drawTextWithShadow(textRenderer, Text.literal(module.name()), x + 12, y + 12, TEXT);
            ctx.drawTextWithShadow(textRenderer, Text.literal(module.description()), x + 12, y + 29, MUTED);
            int pillColor = module.enabled() ? 0xFF501522 : 0xFF34303A;
            ctx.fill(x + cardW - 52, y + 48, x + cardW - 12, y + 64, pillColor);
            ctx.drawTextWithShadow(textRenderer, Text.literal(module.enabled() ? "ON" : "OFF"), x + cardW - 42, y + 52, module.enabled() ? RED : MUTED);
            ctx.drawTextWithShadow(textRenderer, Text.literal("RMB  SETTINGS"), x + 12, y + 53, 0xFF7D727D);
            shown++;
        }
        if (shown == 0) ctx.drawTextWithShadow(textRenderer, Text.literal("No modules found in this category."), listX, listY + 8, MUTED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("LMB  toggle module     •     RMB  open settings"), listX, panelY + panelH - 28, MUTED);
    }

    private void drawSettings(DrawContext ctx, int mx, int my) {
        int x = listX, y = listY;
        ctx.drawTextWithShadow(textRenderer, Text.literal("‹  BACK TO MODULES"), x, y - 17, RED);
        ctx.drawTextWithShadow(textRenderer, Text.literal(selectedModule.name()), x, y + 8, TEXT);
        ctx.drawTextWithShadow(textRenderer, Text.literal(selectedModule.description()), x, y + 25, MUTED);
        ctx.drawTextWithShadow(textRenderer, Text.literal(selectedModule.enabled() ? "● ENABLED" : "○ DISABLED"), x, y + 47, selectedModule.enabled() ? RED : MUTED);
        int rowY = y + 78;
        for (Setting<?> setting : selectedModule.settings()) {
            ctx.fill(x, rowY, panelX + panelW - 20, rowY + 42, CARD);
            ctx.drawTextWithShadow(textRenderer, Text.literal(setting.name()), x + 12, rowY + 8, TEXT);
            ctx.drawTextWithShadow(textRenderer, Text.literal(setting.description()), x + 12, rowY + 23, MUTED);
            String value = setting instanceof BoolSetting b ? (b.get() ? "ON" : "OFF")
                    : setting instanceof NumberSetting n ? String.format("%.2f", n.get()) : String.valueOf(setting.get());
            ctx.drawTextWithShadow(textRenderer, Text.literal(value), panelX + panelW - 88, rowY + 14, RED);
            if (setting instanceof NumberSetting n) {
                int bx = panelX + panelW - 142;
                int by = rowY + 33;
                int bw = 110;
                ctx.fill(bx, by, bx + bw, by + 3, 0xFF45323B);
                int progress = (int) (bw * (n.get() - n.min()) / (n.max() - n.min()));
                ctx.fill(bx, by, bx + progress, by + 3, RED);
            }
            rowY += 50;
        }
        ctx.drawTextWithShadow(textRenderer, Text.literal("CLICK A SETTING TO CHANGE IT  •  SLIDERS: LEFT/RIGHT CLICK"), x, panelY + panelH - 28, MUTED);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX, my = (int) mouseY;
        if (selectedModule != null) {
            int backY = listY - 22;
            if (mx >= listX && mx <= listX + 150 && my >= backY && my <= backY + 20) {
                selectedModule = null; return true;
            }
            int rowY = listY + 78;
            for (Setting<?> setting : selectedModule.settings()) {
                if (my >= rowY && my <= rowY + 42 && mx >= listX && mx <= panelX + panelW - 20) {
                    if (setting instanceof BoolSetting bool) bool.toggle();
                    else if (setting instanceof NumberSetting number) number.nudge(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT ? -1 : 1);
                    return true;
                }
                rowY += 50;
            }
            if (my >= listY && my <= listY + 65 && mx >= listX) { selectedModule.toggle(); return true; }
            return true;
        }
        int sideY = panelY + 94;
        for (Category category : Category.values()) {
            if (mx >= panelX + 16 && mx <= panelX + 160 && my >= sideY && my <= sideY + 36) {
                selectedCategory = category; return true;
            }
            sideY += 37;
        }
        List<ClientModule> modules = LuciferClient.MODULES.in(selectedCategory);
        int cols = listW > 420 ? 2 : 1, gap = 10, cardW = (listW - gap * (cols - 1)) / cols, cardH = 76, shown = 0;
        for (ClientModule module : modules) {
            if (!module.name().toLowerCase().contains(search.toLowerCase())) continue;
            int x = listX + (shown % cols) * (cardW + gap);
            int y = listY + (shown / cols) * (cardH + gap);
            if (mx >= x && mx <= x + cardW && my >= y && my <= y + cardH) {
                if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) selectedModule = module;
                else if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) module.toggle();
                return true;
            }
            shown++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (selectedModule != null) selectedModule = null; else close();
            return true;
        }
        if (selectedModule == null && keyCode == GLFW.GLFW_KEY_BACKSPACE && !search.isEmpty()) {
            search = search.substring(0, search.length() - 1); return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (selectedModule == null && !Character.isISOControl(chr) && search.length() < 32) {
            search += chr; return true;
        }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() { return false; }
}
