package com.sah.farming;

import com.sah.farming.registry.ModScreens;
import com.sah.farming.screen.FermentingBarrelScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SahFarmingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Rejestracja ekranu GUI dla beczki fermentacyjnej
        HandledScreens.register(ModScreens.FERMENTING_BARREL, FermentingBarrelScreen::new);
    }
}