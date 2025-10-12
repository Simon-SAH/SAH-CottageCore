package com.sah.farming.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    public boolean enableAlcoholEffects = true;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/sahfarming.json");

    public static ModConfig INSTANCE = new ModConfig();

    public static void load() {
        try {
            if (CONFIG_FILE.exists()) {
                INSTANCE = GSON.fromJson(new FileReader(CONFIG_FILE), ModConfig.class);
            } else {
                save(); // utwórz domyślny
            }
        } catch (IOException e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
}