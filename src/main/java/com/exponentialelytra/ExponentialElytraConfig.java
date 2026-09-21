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
    public static final double DEFAULT_RIPTIDE_VELOCITY_MULTIPLIER = 1.10D;

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final double rocketVelocityMultiplier;
    private final double riptideVelocityMultiplier;

    private ExponentialElytraConfig(double rocketVelocityMultiplier, double riptideVelocityMultiplier) {
        this.rocketVelocityMultiplier = rocketVelocityMultiplier;
        this.riptideVelocityMultiplier = riptideVelocityMultiplier;
    }

    public static ExponentialElytraConfig defaults() {
        return new ExponentialElytraConfig(
                DEFAULT_ROCKET_VELOCITY_MULTIPLIER,
                DEFAULT_RIPTIDE_VELOCITY_MULTIPLIER
        );
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

            JsonObject object = root.getAsJsonObject();
            double rocketMultiplier = readMultiplier(
                    object,
                    "rocketVelocityMultiplier",
                    DEFAULT_ROCKET_VELOCITY_MULTIPLIER,
                    path,
                    logger
            );
            double riptideMultiplier = readMultiplier(
                    object,
                    "riptideVelocityMultiplier",
                    DEFAULT_RIPTIDE_VELOCITY_MULTIPLIER,
                    path,
                    logger
            );

            return new ExponentialElytraConfig(rocketMultiplier, riptideMultiplier);
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
            root.addProperty("riptideVelocityMultiplier", config.riptideVelocityMultiplier());
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

    public double riptideVelocityMultiplier() {
        return riptideVelocityMultiplier;
    }

    private static double readMultiplier(
            JsonObject object,
            String key,
            double defaultValue,
            Path path,
            Logger logger
    ) {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber()) {
            logger.error("Config {} has no numeric {}; using the default {}", path, key, defaultValue);
            return defaultValue;
        }

        try {
            double multiplier = value.getAsDouble();
            if (Double.isFinite(multiplier) && multiplier >= 0.0D) {
                return multiplier;
            }

            logger.error("Config {} has invalid {} {}; using the default {}",
                    path, key, multiplier, defaultValue);
        } catch (RuntimeException exception) {
            logger.error("Config {} has an unreadable {}; using the default {}",
                    path, key, defaultValue, exception);
        }

        return defaultValue;
    }
}
