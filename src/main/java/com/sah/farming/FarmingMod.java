package com.sah.farming;

import com.sah.farming.loot.ModLootInjector;
import com.sah.farming.registry.ModBlockEntities;
import com.sah.farming.registry.ModBlocks;
import com.sah.farming.registry.ModItems;
import com.sah.farming.registry.ModScreens;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmingMod implements ModInitializer {
    public static final String MOD_ID = "sahfarming";
    public static final Logger LOGGER = LoggerFactory.getLogger("SAH Project: CottageCore");

    @Override
    public void onInitialize() {
        // Rejestracje
        ModBlocks.init();
        ModItems.init();
        ModScreens.init();
        ModBlockEntities.init();

        // Wstrzyknięcia lootów (trawa + skrzynki wiosek)
        ModLootInjector.init();

        LOGGER.info("[{}] Initialized.", MOD_ID);
    }
}
