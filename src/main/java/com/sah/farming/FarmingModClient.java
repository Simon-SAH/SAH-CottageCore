package com.sah.farming;

import com.sah.farming.registry.ModScreens;
import com.sah.farming.screen.FermentingBarrelScreen;
import com.sah.farming.registry.ModBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;

public class FarmingModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("[SAH] Client init called.");

        // GUI beczki fermentacyjnej
        HandledScreens.register(ModScreens.FERMENTING_BARREL, FermentingBarrelScreen::new);

        // 🌿 warstwa renderowania dla winorośli
        BlockRenderLayerMap.putBlock(ModBlocks.GRAPE_CROP, BlockRenderLayer.CUTOUT);

        // ModParticleFactories niepotrzebne – używamy SimpleParticleType bez fabryk
    }
}
