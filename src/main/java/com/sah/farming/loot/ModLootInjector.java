package com.sah.farming.loot;

import com.sah.farming.registry.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class ModLootInjector {

    private ModLootInjector() {}

    // Obsłużmy warianty traw/paproci między wersjami
    private static final Set<RegistryKey<LootTable>> GRASSY_BLOCK_LOOT = Set.of(
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/short_grass")),
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/grass")),
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/tall_grass")),
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/fern")),
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/large_fern"))
    );

    public static void init() {
        LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (!source.isBuiltin()) return; // tylko vanilla tabele

            // 1) Trawy/paprocie — rzadki drop 1 nasiona (3%)
            if (GRASSY_BLOCK_LOOT.contains(key)) {
                builder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.03f)) // 3%
                        .with(ItemEntry.builder(ModItems.GRAPE_SEEDS))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))));
                return;
            }

            // 2) Skrzynki wiosek — 12% szansy na 1–2 nasiona
            Identifier id = key.getValue();
            if ("minecraft".equals(id.getNamespace()) && id.getPath().startsWith("chests/village/")) {
                builder.pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.12f)) // 12%
                        .with(ItemEntry.builder(ModItems.GRAPE_SEEDS))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))));
            }
        });
    }
}