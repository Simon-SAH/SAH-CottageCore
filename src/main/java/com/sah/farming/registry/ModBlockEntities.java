package com.sah.farming.registry;

import com.sah.farming.FarmingMod;
import com.sah.farming.blockentity.FermentingBarrelBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static BlockEntityType<FermentingBarrelBlockEntity> FERMENTING_BARREL;

    public static void init() {
        FERMENTING_BARREL = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(FarmingMod.MOD_ID, "fermenting_barrel"),
                FabricBlockEntityTypeBuilder
                        .create(FermentingBarrelBlockEntity::new, ModBlocks.FERMENTING_BARREL)
                        .build()
        );
    }
}