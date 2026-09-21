package com.exponentialelytra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExponentialElytraConfig {
    public static final double DEFAULT_ROCKET_VELOCITY_MULTIPLIER = 1.10D;

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final double rocketVelocityMultiplier;

    private ExponentialElytraConfig(double rocketVelocityMultiplier) {
        this.rocketVelocityMultiplier = rocketVelocityMultiplier;
    }

    public static ExponentialElytraConfig defaults() {
        return new ExponentialElytraConfig(DEFAULT_ROCKET_VELOCITY_MULTIPLIER);
    }

    public static ExponentialElytraConfig load(Path path, Logger logger) {
        if (Files.notExists(path)) {
            ExponentialElytraConfig defaults = defaults();
            writeDefault(path, defaults, logger);
            return defaults;
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonObject()) {
                logger.error("Config {} must contain a JSON object; using the default multiplier {}",
                        path, DEFAULT_ROCKET_VELOCITY_MULTIPLIER);
                return defaults();
            }

            JsonElement value = root.getAsJsonObject().get("rocketVelocityMultiplier");
            if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
                logger.error("Config {} has no numeric rocketVelocityMultiplier; using the default {}",
                        path, DEFAULT_ROCKET_VELOCITY_MULTIPLIER);
                return defaults();
            }

            double multiplier = value.getAsDouble();
            if (!Double.isFinite(multiplier) || multiplier < 0.0D) {
                logger.error("Config {} has invalid rocketVelocityMultiplier {}; using the default {}",
                        path, multiplier, DEFAULT_ROCKET_VELOCITY_MULTIPLIER);
                return defaults();
            }

            return new ExponentialElytraConfig(multiplier);
        } catch (Exception exception) {
            logger.error("Could not read config {}; using the default multiplier {}",
                    path, DEFAULT_ROCKET_VELOCITY_MULTIPLIER, exception);
            return defaults();
        }
    }

    private static void writeDefault(Path path, ExponentialElytraConfig config, Logger logger) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            JsonObject root = new JsonObject();
            root.addProperty("rocketVelocityMultiplier", config.rocketVelocityMultiplier());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException exception) {
            logger.error("Could not create default config {}; continuing with multiplier {}",
                    path, DEFAULT_ROCKET_VELOCITY_MULTIPLIER, exception);
        }
    }

    public double rocketVelocityMultiplier() {
        return rocketVelocityMultiplier;
    }
}
