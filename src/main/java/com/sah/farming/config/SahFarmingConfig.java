package com.sah.farming.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SahFarmingConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "sahfarming.json");

    public static SahFarmingConfig INSTANCE = new SahFarmingConfig();

    // 🧪 Domyślna wartość mnożnika fermentacji (1.0 = normalnie)
    public float fermentationTimeMultiplier = 1.0f;

    private SahFarmingConfig() {}

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, SahFarmingConfig.class);
                System.out.println("[SAH] Config loaded: " + CONFIG_FILE.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("[SAH] Failed to load config: " + e.getMessage());
            }
        } else {
            save(); // zapisz domyślny plik
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
            System.out.println("[SAH] Config saved: " + CONFIG_FILE.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[SAH] Failed to save config: " + e.getMessage());
        }
    }
}
