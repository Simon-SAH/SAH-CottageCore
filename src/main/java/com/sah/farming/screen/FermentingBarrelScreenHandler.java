package com.sah.farming.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

/**
 * 4 wejścia (układ 2×2) + 1 wyjście.
 * Sloty: 0=baza, 1=kierunek, 2=drożdże, 3=cukier, 4=wynik.
 * Filtry na razie luźne (true), żeby nie blokować kompilacji — zwęzimy później.
 */
public class FermentingBarrelScreenHandler extends ScreenHandler {
    public static final int INPUTS = 4;
    public static final int OUTPUT = 4;

    private final Inventory inv;
    private final PropertyDelegate props; // [0]=progress, [1]=maxProgress

    public FermentingBarrelScreenHandler(int syncId, PlayerInventory playerInv) {
        this(syncId, playerInv, new SimpleInventory(INPUTS + 1), new ArrayPropertyDelegate(2));
    }

    public FermentingBarrelScreenHandler(int syncId, PlayerInventory playerInv, Inventory inventory, PropertyDelegate props) {
        super(com.sah.farming.registry.ModScreens.FERMENTING_BARREL, syncId);
        this.inv = inventory;
        this.props = props;
        this.addProperties(props);

// 2×2 wejścia
        this.addSlot(new FreeSlot(inv, 0, 26, 17)); // baza (woda/mleko)
        this.addSlot(new FreeSlot(inv, 1, 56, 17)); // surowiec (winogrona/chmiel)
        this.addSlot(new FreeSlot(inv, 2, 26, 53)); // drożdże
        this.addSlot(new FreeSlot(inv, 3, 56, 53)); // cukier (opcjonalnie)
        // ── wyjście ──
        this.addSlot(new Slot(inv, 4, 116, 35)); // wynik fermentacji

        // ── inwentarz gracza ──
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col)
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int hot = 0; hot < 9; ++hot)
            this.addSlot(new Slot(playerInv, hot, 8 + hot * 18, 142));
    }

    public Inventory getInventory() { return inv; }
    @Override public boolean canUse(PlayerEntity player) { return true; }

    /** Skala paska postępu (piksele). */
    public int getScaledProgress(int maxPixels) {
        int progress = props.get(0);
        int max = props.get(1);
        if (max <= 0) return 0;
        return Math.min(maxPixels, (int)((long)progress * maxPixels / max));
    }

    /** Shift-klik przenoszenia. */
    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stack = slot.getStack();
            copy = stack.copy();

            int machineStart = 0;
            int machineEnd = machineStart + INPUTS + 1; // 0..4
            int playerStart = machineEnd;
            int playerEnd = playerStart + 36;

            if (index < machineEnd) {
                if (!this.insertItem(stack, playerStart, playerEnd, true)) return ItemStack.EMPTY;
            } else {
                if (!this.insertItem(stack, 0, INPUTS, false)) return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.setStack(ItemStack.EMPTY); else slot.markDirty();
        }
        return copy;
    }

    /* ── Sloty ── */

    /** Na razie wpuszcza wszystko — zawęzimy po rejestracji itemów. */
    private static class FreeSlot extends Slot {
        public FreeSlot(Inventory inv, int index, int x, int y) { super(inv, index, x, y); }
        @Override public boolean canInsert(ItemStack stack) { return true; }
    }

    /** Wyjście — tylko odbiór. */
    private static class OutputOnlySlot extends Slot {
        public OutputOnlySlot(Inventory inv, int index, int x, int y) { super(inv, index, x, y); }
        @Override public boolean canInsert(ItemStack stack) { return false; }
    }
}