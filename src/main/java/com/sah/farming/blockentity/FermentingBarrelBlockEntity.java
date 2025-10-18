package com.sah.farming.blockentity;

import com.sah.farming.block.FermentingBarrelBlock;
import com.sah.farming.registry.ModBlockEntities;
import com.sah.farming.registry.ModItems;
import com.sah.farming.screen.FermentingBarrelScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import com.sah.farming.registry.ModParticles;

public class FermentingBarrelBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, InventoryChangedListener {
    // Sloty
    public static final int SLOT_BASE = 0;     // woda/mleko (na razie dowolny item do testu)
    public static final int SLOT_DIR  = 1;     // kierunek (np. winogrona, chmiel)
    public static final int SLOT_YEAST= 2;     // drożdże
    public static final int SLOT_SUGAR= 3;     // opcjonalnie cukier
    public static final int SLOT_OUT  = 4;     // wynik
    public static final int SLOT_COUNT = 5;

    private static final int MAX_TICKS = 600;  // ~10s (20t/s) – łatwo zauważyć w teście

    private final SimpleInventory inventory = new SimpleInventory(SLOT_COUNT);
    private final ArrayPropertyDelegate props = new ArrayPropertyDelegate(2); // [0]=progress, [1]=max
    private boolean fermenting = false;

    public FermentingBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FERMENTING_BARREL, pos, state);
        inventory.addListener(this);
        props.set(0, 0);
        props.set(1, MAX_TICKS);
    }

    public Inventory getInventory() { return inventory; }
    public PropertyDelegate getProperties() { return props; }
    public boolean isFermenting() { return fermenting; }

    /* =================== GUI =================== */

    public Text getDisplayName() {
        return Text.translatable("container.sahfarming.fermenting_barrel");
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
        return new FermentingBarrelScreenHandler(syncId, playerInv, this.inventory, this.props);
    }

    /* ========= Reakcja na zmiany w ekwipunku ========= */

    @Override
    public void onInventoryChanged(Inventory inv) {
        if (this.world == null || this.world.isClient()) return;

        boolean hasBase      = !inventory.getStack(SLOT_BASE).isEmpty();
        boolean hasDirection = !inventory.getStack(SLOT_DIR).isEmpty();
        boolean hasYeast     = !inventory.getStack(SLOT_YEAST).isEmpty();

        if (!fermenting && hasBase && hasDirection && hasYeast) {
            // START fermentacji
            fermenting = true;
            props.set(0, 0);
            props.set(1, MAX_TICKS);
            // domknij wieko
            closeLidIfOpen();
            markDirty();
        } else if (fermenting && (!hasBase || !hasDirection || !hasYeast)) {
            // ANULUJ fermentację, jeśli ktoś wyjął składniki w trakcie
            fermenting = false;
            props.set(0, 0);
            // możesz otworzyć wieko, żeby było widać, że „nie pracuje”
            openLidIfClosed();
            markDirty();
        }
    }

    /* =================== Tick (serwer) =================== */

    public static void serverTick(World world, BlockPos pos, BlockState state, FermentingBarrelBlockEntity be) {
        if (world.isClient()) return;

        if (!be.fermenting) return;

        int prog = be.props.get(0);
        int max  = be.props.get(1);

        if (prog < max) {
            be.props.set(0, prog + 1);
            be.markDirty();
        } else {
            // ZAKOŃCZENIE: prosty prototyp rezultatu
            // Jeśli slot wyjścia pusty → włóż 1x winogrona (placeholder wyniku),
            // a wejścia wyczyść. (Docelowo: wino/piwo/ocet.)
            ItemStack out = be.inventory.getStack(SLOT_OUT);
            if (out.isEmpty()) {
                be.inventory.setStack(SLOT_OUT, new ItemStack(ModItems.GRAPES, 1));
                be.inventory.setStack(SLOT_BASE, ItemStack.EMPTY);
                be.inventory.setStack(SLOT_DIR,  ItemStack.EMPTY);
                be.inventory.setStack(SLOT_YEAST,ItemStack.EMPTY);
                be.inventory.setStack(SLOT_SUGAR,ItemStack.EMPTY);
            }
            be.fermenting = false;
            be.props.set(0, 0);
            be.markDirty();
            // otwórz wieko + dźwięk
            be.openLidIfClosed();
        }
        // 💨 Cząsteczki fermentacji (opary)
        if (be.fermenting) {
            ((ServerWorld) world).spawnParticles(
                    ModParticles.FERMENTATION,
                    pos.getX() + 0.5,
                    pos.getY() + 1.1,
                    pos.getZ() + 0.5,
                    2,
                    0.05, 0.05, 0.05,
                    0.01
            );
        }

    }

    /* =================== Pomocnicze =================== */

    private void closeLidIfOpen() {
        if (world == null) return;
        var pos = getPos();
        var state = world.getBlockState(pos);
        if (state.getBlock() instanceof FermentingBarrelBlock && state.contains(FermentingBarrelBlock.OPEN) && state.get(FermentingBarrelBlock.OPEN)) {
            world.setBlockState(pos, state.with(FermentingBarrelBlock.OPEN, false), net.minecraft.block.Block.NOTIFY_ALL);
            world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_BARREL_CLOSE, net.minecraft.sound.SoundCategory.BLOCKS, 1f, 1f);
        }
    }

    private void openLidIfClosed() {
        if (world == null) return;
        var pos = getPos();
        var state = world.getBlockState(pos);
        if (state.getBlock() instanceof FermentingBarrelBlock && state.contains(FermentingBarrelBlock.OPEN) && !state.get(FermentingBarrelBlock.OPEN)) {
            world.setBlockState(pos, state.with(FermentingBarrelBlock.OPEN, true), net.minecraft.block.Block.NOTIFY_ALL);
            world.playSound(null, pos, net.minecraft.sound.SoundEvents.BLOCK_BARREL_OPEN, net.minecraft.sound.SoundCategory.BLOCKS, 1f, 1f);
        }
    }
}