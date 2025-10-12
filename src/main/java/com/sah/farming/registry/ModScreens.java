package com.sah.farming.registry;

import com.sah.farming.FarmingMod;
import com.sah.farming.screen.FermentingBarrelScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreens {
    public static ScreenHandlerType<FermentingBarrelScreenHandler> FERMENTING_BARREL;

    public static void init() {
        FERMENTING_BARREL = Registry.register(
                Registries.SCREEN_HANDLER,
                Identifier.of(FarmingMod.MOD_ID, "fermenting_barrel"),
                new ScreenHandlerType<>(FermentingBarrelScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
        );
    }
}