package com.sah.farming;

import com.sah.farming.registry.ModBlocks;
import com.sah.farming.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class SahFarming implements ModInitializer {
    public static final String MOD_ID = "sahfarming";

    @Override
    public void onInitialize() {
        ModBlocks.init();
        ModItems.init();
    }
}