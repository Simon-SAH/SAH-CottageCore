package com.sah.farming;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModItems {

    public static Item GRAPE_SEEDS;

    public static void register() {
        GRAPE_SEEDS = Registry.register(
                Registries.ITEM,
                id("grape_seeds"),
                new Item(new Item.Settings())
        );
        FarmingMod.LOG.info("Registered items: grape_seeds");
    }

    private static Identifier id(String path) {
        return new Identifier(FarmingMod.MOD_ID, path);
    }

    private ModItems() {}
}
