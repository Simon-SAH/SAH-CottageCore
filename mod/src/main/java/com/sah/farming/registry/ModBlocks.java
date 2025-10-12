package com.sah.farming.registry;

import com.sah.farming.FarmingMod;
import com.sah.farming.block.FermentingBarrelBlock;
import com.sah.farming.block.GrapeCropBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // ==== Grape Crop ====
    public static Identifier GRAPE_CROP_ID;
    public static RegistryKey<Block> GRAPE_CROP_KEY;
    public static Block GRAPE_CROP;

    // ==== Fermenting Barrel ====
    public static Identifier FERMENTING_BARREL_ID;
    public static RegistryKey<Block> FERMENTING_BARREL_KEY;
    public static Block FERMENTING_BARREL;

    public static void init() {
        // --- GRAPE CROP ---
        GRAPE_CROP_ID  = Identifier.of(FarmingMod.MOD_ID, "grape_crop");
        GRAPE_CROP_KEY = RegistryKey.of(RegistryKeys.BLOCK, GRAPE_CROP_ID);

        GRAPE_CROP = Registry.register(
                Registries.BLOCK,
                GRAPE_CROP_KEY,
                new GrapeCropBlock(
                        AbstractBlock.Settings.copy(Blocks.BEETROOTS)
                                .registryKey(GRAPE_CROP_KEY)
                )
        );

        // --- FERMENTING BARREL ---
        FERMENTING_BARREL_ID  = Identifier.of(FarmingMod.MOD_ID, "fermenting_barrel");
        FERMENTING_BARREL_KEY = RegistryKey.of(RegistryKeys.BLOCK, FERMENTING_BARREL_ID);

        FERMENTING_BARREL = Registry.register(
                Registries.BLOCK,
                FERMENTING_BARREL_KEY,
                new FermentingBarrelBlock(
                        AbstractBlock.Settings.create()
                                .mapColor(MapColor.OAK_TAN)
                                .strength(2.5f)
                                .sounds(BlockSoundGroup.WOOD)
                                .registryKey(FERMENTING_BARREL_KEY) // << KLUCZOWE
                )
        );
    }
}