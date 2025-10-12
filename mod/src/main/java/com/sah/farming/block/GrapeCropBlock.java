package com.sah.farming.block;

import com.sah.farming.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GrapeCropBlock extends CropBlock {
    public static final IntProperty AGE = Properties.AGE_3;

    public GrapeCropBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(getAgeProperty(), 0));
    }

    public IntProperty getAgeProperty() { return AGE; }
    public int getMaxAge() { return 3; }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    /* ───────────────────────── Interakcje ───────────────────────── */

    // Wersja z parametrem Hand (część mappingów 1.20/1.21)
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack held = player.getStackInHand(hand);
        return handleInteract(state, world, pos, player, held);
    }

    // Wersja bez Hand (inne mappingi 1.21.x)
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        ItemStack held = player.getMainHandStack(); // fallback – wystarczy do zbioru pustą ręką i bone meal w main hand
        return handleInteract(state, world, pos, player, held);
    }

    private ActionResult handleInteract(BlockState state, World world, BlockPos pos,
                                        PlayerEntity player, ItemStack held) {
        int age = this.getAge(state);

        // 1) Mączka kostna: zawsze +1 poziom (nie przeskakuje)
        if (!held.isEmpty() && held.isOf(Items.BONE_MEAL) && age < this.getMaxAge()) {
            if (!world.isClient) {
                world.setBlockState(pos, this.withAge(age + 1), Block.NOTIFY_LISTENERS);
                if (!player.getAbilities().creativeMode) held.decrement(1);
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return ActionResult.SUCCESS;
        }

        // 2) Zbiór owoców jak sweet_berry_bush (age >= 2)
        if (age > 1) {
            Random rng = world.getRandom();
            int base = (age == getMaxAge()) ? 2 : 1; // 2–3 z dojrzałego, 1–2 z „prawie”
            int count = base + rng.nextInt(2);

            if (!world.isClient) {
                player.giveItemStack(new ItemStack(ModItems.GRAPES, count));
                world.setBlockState(pos, this.withAge(1), Block.NOTIFY_LISTENERS);
                try {
                    world.playSound(null, pos,
                            SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES,
                            SoundCategory.BLOCKS, 1f, 1f);
                } catch (Throwable ignored) { /* różnice nazw dźwięku w mappingach */ }
                world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    /* ───────────────────────── Niszcznie ───────────────────────── */

    // Gwarantowany drop nasion przy zniszczeniu (fallback niezależny od loot_tables)
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            dropStack(world, pos, new ItemStack(ModItems.GRAPE_SEEDS, 1));
        }
        return super.onBreak(world, pos, state, player);
    }
}