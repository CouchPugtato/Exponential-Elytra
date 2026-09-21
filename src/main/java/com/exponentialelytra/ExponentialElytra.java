package com.exponentialelytra;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExponentialElytra implements ModInitializer {
    public static final String MOD_ID = "exponential_elytra";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ExponentialElytraConfig config = ExponentialElytraConfig.defaults();

    @Override
    public void onInitialize() {
        config = ExponentialElytraConfig.load(
                FabricLoader.getInstance().getConfigDir().resolve("exponential-elytra.json"),
                LOGGER
        );
        LOGGER.info("Exponential Elytra initialized with rocket velocity multiplier {}", config.rocketVelocityMultiplier());
    }

    public static ExponentialElytraConfig config() {
        return config;
    }
}
