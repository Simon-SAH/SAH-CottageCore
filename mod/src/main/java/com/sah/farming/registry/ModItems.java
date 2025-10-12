package com.sah.farming.registry;

import com.sah.farming.FarmingMod;
import com.sah.farming.item.GrapeSeedsItem;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {
    // ==== Grape Seeds ====
    public static Identifier GRAPE_SEEDS_ID;
    public static RegistryKey<Item> GRAPE_SEEDS_KEY;
    public static Item GRAPE_SEEDS;

    // ==== Grapes ====
    public static Identifier GRAPES_ID;
    public static RegistryKey<Item> GRAPES_KEY;
    public static Item GRAPES;

    // ==== Fermenting Barrel (BlockItem) ====
    public static Identifier FERMENTING_BARREL_ID;
    public static RegistryKey<Item> FERMENTING_BARREL_KEY;
    public static Item FERMENTING_BARREL_ITEM;

    public static void init() {
        // --- grape_seeds ---
        GRAPE_SEEDS_ID  = Identifier.of(FarmingMod.MOD_ID, "grape_seeds");
        GRAPE_SEEDS_KEY = RegistryKey.of(RegistryKeys.ITEM, GRAPE_SEEDS_ID);
        GRAPE_SEEDS = Registry.register(
                Registries.ITEM, GRAPE_SEEDS_KEY,
                new GrapeSeedsItem(new Item.Settings().registryKey(GRAPE_SEEDS_KEY))
        );

        // --- grapes ---
        GRAPES_ID  = Identifier.of(FarmingMod.MOD_ID, "grapes");
        GRAPES_KEY = RegistryKey.of(RegistryKeys.ITEM, GRAPES_ID);
        GRAPES = Registry.register(
                Registries.ITEM, GRAPES_KEY,
                new Item(new Item.Settings()
                        .food(FoodComponents.SWEET_BERRIES)
                        .registryKey(GRAPES_KEY))
        );

        // --- fermenting_barrel (BlockItem) ---
        FERMENTING_BARREL_ID  = Identifier.of(FarmingMod.MOD_ID, "fermenting_barrel");
        FERMENTING_BARREL_KEY = RegistryKey.of(RegistryKeys.ITEM, FERMENTING_BARREL_ID);
        FERMENTING_BARREL_ITEM = Registry.register(
                Registries.ITEM,
                FERMENTING_BARREL_KEY,
                new BlockItem(ModBlocks.FERMENTING_BARREL, new Item.Settings().registryKey(FERMENTING_BARREL_KEY))
        );
    }
}