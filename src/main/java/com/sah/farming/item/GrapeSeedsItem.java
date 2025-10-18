package com.sah.farming.item;

import com.sah.farming.registry.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GrapeSeedsItem extends Item {
    public GrapeSeedsItem(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnBlock(ItemUsageContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        if (ctx.getSide() != Direction.UP) return ActionResult.PASS;

        BlockState base = world.getBlockState(pos);
        if (!base.isOf(Blocks.FARMLAND)) return ActionResult.PASS;

        BlockPos up = pos.up();
        if (!world.getBlockState(up).isAir()) return ActionResult.FAIL;

        if (!world.isClient()) {
            world.setBlockState(up, ModBlocks.GRAPE_CROP.getDefaultState());
            if (ctx.getPlayer() != null && !ctx.getPlayer().getAbilities().creativeMode) {
                ctx.getStack().decrement(1);
            }
        }
        return ActionResult.SUCCESS;
    }
}