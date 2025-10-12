package com.sah.farming.block;

import com.mojang.serialization.MapCodec;
import com.sah.farming.blockentity.FermentingBarrelBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FermentingBarrelBlock extends BlockWithEntity {
    // WYMAGANY CODEC dla BlockWithEntity w nowszych wersjach
    public static final MapCodec<FermentingBarrelBlock> CODEC = createCodec(FermentingBarrelBlock::new);
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    // Stan bloku
    public static final Property<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;

    public FermentingBarrelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    // celowo bez @Override (różne mappingi)
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    // celowo bez @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    // PPM: jeśli NIE fermentuje → otwórz wieko; jeśli fermentuje → wieko zostaje zamknięte (ale GUI się otworzy)
    // celowo bez @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            boolean canOpenLid = true;
            if (be instanceof FermentingBarrelBlockEntity barrelBE) {
                if (barrelBE.isFermenting()) {
                    canOpenLid = false; // fermentuje → zostaw zamknięte
                }
            }
            if (canOpenLid && !state.get(OPEN)) {
                world.setBlockState(pos, state.with(OPEN, true), net.minecraft.block.Block.NOTIFY_ALL);
                world.playSound(null, pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1f, 1f);
            }
            if (be instanceof NamedScreenHandlerFactory factory) {
                player.openHandledScreen(factory);
            }
        }
        return ActionResult.SUCCESS;
    }

    // Fallback dla mappingów wywołujących onUse bez Hand
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, BlockHitResult hit) {
        return this.onUse(state, world, pos, player, Hand.MAIN_HAND, hit);
    }

    // celowo bez @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FermentingBarrelBlockEntity(pos, state);
    }

    // Ticker BE (serwer) — bez użycia BlockWithEntity#checkType
    public <T extends net.minecraft.block.entity.BlockEntity> net.minecraft.block.entity.BlockEntityTicker<T> getTicker(
            net.minecraft.world.World world,
            net.minecraft.block.BlockState state,
            net.minecraft.block.entity.BlockEntityType<T> type
    ) {
        if (world.isClient) return null;

        // Zwracamy ticker tylko dla naszej beczki
        if (type == com.sah.farming.registry.ModBlockEntities.FERMENTING_BARREL) {
            return (w, pos, st, be) -> {
                if (be instanceof com.sah.farming.blockentity.FermentingBarrelBlockEntity barrel) {
                    com.sah.farming.blockentity.FermentingBarrelBlockEntity.serverTick(w, pos, st, barrel);
                }
            };
        }
        return null;
    }

}