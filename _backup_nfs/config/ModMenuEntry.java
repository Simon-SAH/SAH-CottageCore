package com.sah.farming.config;

import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Function;

public class ModMenuEntry implements ModMenuApi {
    public Optional<Function<Screen, ? extends Screen>> getConfigScreenFactory() {
        return Optional.empty(); // brak GUI konfiguracyjnego na razie
    }
}