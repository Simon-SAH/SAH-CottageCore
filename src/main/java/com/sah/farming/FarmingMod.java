package com.sah.farming;

import com.sah.farming.loot.ModLootInjector;
import com.sah.farming.registry.ModBlockEntities;
import com.sah.farming.registry.ModBlocks;
import com.sah.farming.registry.ModItems;
import com.sah.farming.registry.ModScreens;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sah.farming.config.SahFarmingConfig;
import net.minecraft.util.Identifier;
import net.minecraft.particle.SimpleParticleType;
import com.sah.farming.registry.ModParticles;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;

import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;


public class FarmingMod implements ModInitializer {
    public static final String MOD_ID = "sahfarming";
    public static final Logger LOGGER = LoggerFactory.getLogger("SAH Project: CottageCore");


    @Override
    public void onInitialize() {
        // 🧠 Wczytaj konfigurację
        SahFarmingConfig.load();
        // Rejestracje
        ModBlocks.init();
        ModItems.init();
        ModScreens.init();
        ModBlockEntities.init();
        ModParticles.register();


        // Wstrzyknięcia lootów (trawa + skrzynki wiosek)
        ModLootInjector.init();



        LOGGER.info("[{}] Initialized.", MOD_ID);
    }
}
