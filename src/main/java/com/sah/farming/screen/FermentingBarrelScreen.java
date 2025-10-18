package com.sah.farming.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;

import java.lang.reflect.Method;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FermentingBarrelScreen extends HandledScreen<FermentingBarrelScreenHandler> {

    // ── Tekstury ──────────────────────────────────────────────────────────────
    private static final Identifier BG_TEX =
            Identifier.of("sahfarming", "textures/gui/container/fermenting_barrel.png");
    private static final Identifier ARROW_TEX =
            Identifier.of("sahfarming", "textures/gui/container/fermentation_progress.png");

    // Ghosty
    private static final Identifier GHOST_BUCKET =
            Identifier.of("sahfarming", "textures/gui/sprites/container/slots/bucket.png");
    private static final Identifier GHOST_FUEL =
            Identifier.of("sahfarming", "textures/gui/sprites/container/slots/fermenting_fuel.png");
    private static final Identifier GHOST_INGREDIENT =
            Identifier.of("sahfarming", "textures/gui/sprites/container/slots/ingredient.png");
    private static final Identifier GHOST_DISABLED =
            Identifier.of("sahfarming", "textures/gui/sprites/container/slots/disabled.png");

    private static final RenderPipeline GUI_PIPELINE = RenderPipelines.GUI_TEXTURED;

    // ── Layout ────────────────────────────────────────────────────────────────
    private static final int BG_W = 176, BG_H = 166;
    private static final int BG_TEX_W = 256, BG_TEX_H = 256;

    private static final int ARROW_W = 22, ARROW_H = 15;
    private static final int ARROW_X = 79, ARROW_Y = 34;

    // Sloty
    private static final int S0_X = 26, S0_Y = 17;
    private static final int S1_X = 56, S1_Y = 17;
    private static final int S2_X = 26, S2_Y = 53;
    private static final int S3_X = 56, S3_Y = 53;
    private static final int OUT_X = 116, OUT_Y = 35;

    public FermentingBarrelScreen(FermentingBarrelScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        this.backgroundWidth = BG_W;
        this.backgroundHeight = BG_H;
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext ctx, float delta, int mouseX, int mouseY) {
        final int x = this.x, y = this.y;

        // Tło
        ctx.drawTexture(GUI_PIPELINE, BG_TEX, x, y,
                0.0f, 0.0f,
                this.backgroundWidth, this.backgroundHeight,
                BG_TEX_W, BG_TEX_H);

        // Ghosty
        drawGhostIfEmpty(ctx, GHOST_BUCKET,    x + S0_X, y + S0_Y, 0);
        drawGhostIfEmpty(ctx, GHOST_INGREDIENT,x + S1_X, y + S1_Y, 1);
        drawGhostIfEmpty(ctx, GHOST_FUEL,      x + S2_X, y + S2_Y, 2);
        ctx.drawTexture(GUI_PIPELINE, GHOST_DISABLED, x + S3_X, y + S3_Y, 0.0f, 0.0f, 16, 16, 16, 16);

        // Pasek progresu
        int w = this.handler.getScaledProgress(ARROW_W);
        if (w > 0) {
            ctx.drawTexture(GUI_PIPELINE, ARROW_TEX,
                    x + ARROW_X + 1, y + ARROW_Y + 1,
                    0.0f, 0.0f,
                    w, ARROW_H,
                    ARROW_W, ARROW_H);
        }
        // 🔧 Reset kontekstu renderowania, aby przywrócić poprawne alpha dla reszty GUI
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, Identifier.of("minecraft", "textures/gui/demo_background.png"),
                -9999, -9999, 0f, 0f, 1, 1, 1, 1);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        super.render(ctx, mouseX, mouseY, delta);

        final int gx = this.x, gy = this.y;

        // base (bucket) – ciemnoszary
        maybeTooltipEmptySlot(ctx, mouseX, mouseY, 0, gx + S0_X, gy + S0_Y,
                Text.translatable("tooltip.sahfarming.fermenting_barrel.base")
                        .formatted(Formatting.DARK_GRAY));

        // ingredient (grapes/hops) – fioletowy składnik + ciemnoszary opis
        Text ingr = Text.literal("grapes / hops").formatted(Formatting.LIGHT_PURPLE);
        maybeTooltipEmptySlot(ctx, mouseX, mouseY, 1, gx + S1_X, gy + S1_Y,
                Text.translatable("tooltip.sahfarming.fermenting_barrel.ingredient_fmt", ingr)
                        .formatted(Formatting.DARK_GRAY));

        // fuel (sugar) – biały składnik + ciemnoszary opis
        Text fuel = Text.literal("sugar").formatted(Formatting.WHITE);
        maybeTooltipEmptySlot(ctx, mouseX, mouseY, 2, gx + S2_X, gy + S2_Y,
                Text.translatable("tooltip.sahfarming.fermenting_barrel.fuel_fmt", fuel)
                        .formatted(Formatting.DARK_GRAY));

        // yeast (v2) – dwie linie, szary italic
        java.util.List<Text> yeastLines = java.util.List.of(
                Text.translatable("tooltip.sahfarming.fermenting_barrel.yeast_line1").formatted(Formatting.GRAY, Formatting.ITALIC),
                Text.translatable("tooltip.sahfarming.fermenting_barrel.yeast_line2").formatted(Formatting.GRAY, Formatting.ITALIC)
        );
        maybeTooltipEmptySlotLines(ctx, mouseX, mouseY, 3, gx + S3_X, gy + S3_Y, yeastLines);

        this.drawMouseoverTooltip(ctx, mouseX, mouseY);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private boolean isSlotEmpty(int slotIndex) {
        return !this.handler.getSlot(slotIndex).hasStack();
    }

    private void drawGhostIfEmpty(DrawContext ctx, Identifier tex, int px, int py, int slotIndex) {
        if (isSlotEmpty(slotIndex)) {
            ctx.drawTexture(GUI_PIPELINE, tex, px, py, 0.0f, 0.0f, 16, 16, 16, 16);
        }
    }

    private static boolean isPointIn(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private void maybeTooltipEmptySlot(DrawContext ctx, int mouseX, int mouseY,
                                       int slotIndex, int px, int py, Text text) {
        if (isSlotEmpty(slotIndex) && isPointIn(mouseX, mouseY, px, py, 16, 16)) {
            showTooltipCompat(ctx, text, mouseX, mouseY);
        }
    }

    private void showTooltipCompat(DrawContext ctx, Text text, int mouseX, int mouseY) {
        try {
            Method m = DrawContext.class.getMethod("drawTooltip",
                    net.minecraft.client.font.TextRenderer.class, Text.class, int.class, int.class);
            m.invoke(ctx, this.textRenderer, text, mouseX, mouseY);
            return;
        } catch (ReflectiveOperationException ignored) {}
        try {
            Method m = DrawContext.class.getMethod("drawTooltip",
                    net.minecraft.client.font.TextRenderer.class, List.class, int.class, int.class);
            m.invoke(ctx, this.textRenderer, List.of(text), mouseX, mouseY);
            return;
        } catch (ReflectiveOperationException ignored) {}
    }

    private void maybeTooltipEmptySlotLines(DrawContext ctx, int mouseX, int mouseY,
                                            int slotIndex, int px, int py, java.util.List<Text> lines) {
        if (isSlotEmpty(slotIndex) && isPointIn(mouseX, mouseY, px, py, 16, 16)) {
            showTooltipLinesCompat(ctx, lines, mouseX, mouseY);
        }
    }

    private void showTooltipLinesCompat(DrawContext ctx, java.util.List<Text> lines, int mouseX, int mouseY) {
        try {
            // preferowana sygnatura: drawTooltip(TextRenderer, List<Text>, x, y)
            java.lang.reflect.Method m = DrawContext.class.getMethod(
                    "drawTooltip", net.minecraft.client.font.TextRenderer.class, java.util.List.class, int.class, int.class);
            m.invoke(ctx, this.textRenderer, lines, mouseX, mouseY);
            return;
        } catch (ReflectiveOperationException ignored) {}
        // awaryjnie: sklej w jedną linię, gdyby mapowania były nietypowe
        try {
            String joined = lines.stream().map(Text::getString).reduce((a,b)->a + " " + b).orElse("");
            java.lang.reflect.Method m = DrawContext.class.getMethod(
                    "drawTooltip", net.minecraft.client.font.TextRenderer.class, Text.class, int.class, int.class);
            m.invoke(ctx, this.textRenderer, Text.literal(joined), mouseX, mouseY);
        } catch (ReflectiveOperationException ignored) {}
    }
}