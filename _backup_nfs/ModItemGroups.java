package com.sah.farming.item;

import com.sah.farming.FarmingMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class ModItemGroups {

    public static ItemGroup FARMING_GROUP;

    public static void register() {
        FARMING_GROUP = Registry.register(
                Registries.ITEM_GROUP,
                Identifier.of(FarmingMod.MOD_ID, "sahfarming"),
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(ModItems.GRAPE_SEEDS))
                        .displayName(Text.translatable("itemGroup.sahfarming"))
                        .entries((context, entries) -> {
                            entries.add(ModItems.GRAPE_SEEDS);
                        })
                        .build()
        );

        FarmingMod.LOG("Registered creative tab: FARMING_GROUP");
    }
}