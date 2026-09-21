# Exponential Elytra

Exponential Elytra is a small, server-authoritative Fabric mod for Minecraft Java Edition 1.21.10. It removes the practical speed limit from Elytra flight by making firework and Riptide boosts compound with the momentum you already have.

Instead of every boost pushing you toward the same practical top speed, each successful boost builds on your current velocity. Fireworks multiply current Elytra velocity before applying vanilla rocket thrust, while Riptide scales existing momentum immediately before vanilla adds its directional Riptide impulse.

With the default multiplier of `1.10`, a velocity of 40 blocks per second becomes 44 blocks per second before vanilla acceleration is applied. Every component (X, Y, and Z) is scaled by the same amount, so the mod preserves the current flight direction and does not replace vanilla movement, steering, or camera behavior.

There is intentionally no artificial speed cap. Vanilla drag still applies between boosts.

## Installation

1. Install Minecraft Java Edition 1.21.10.
2. Install a compatible Fabric Loader.
3. Put `exponential-elytra-1.0.0.jar` in the Minecraft `mods` folder.
4. Start Minecraft once so the configuration file is created.
5. Edit `config/exponential-elytra.json`.
6. Restart Minecraft after changing the multiplier.

Fabric API is not required.

## Configuration

The configuration is created at `config/exponential-elytra.json`:

```json
{
  "rocketVelocityMultiplier": 1.1,
  "riptideVelocityMultiplier": 1.1
}
```

Both multipliers are non-negative finite numbers and are validated independently. For example, `1.0` adds no exponential scaling, `1.2` adds 20 percent to current velocity, and `2.0` doubles it before the corresponding vanilla acceleration. Invalid, negative, non-finite, or malformed values are logged and replaced in memory by the safe default of `1.10`; a malformed existing file is left intact so it can be corrected.

- `rocketVelocityMultiplier` applies only to successful firework uses while actively Elytra gliding.
- `riptideVelocityMultiplier` applies only when a charged trident successfully activates Riptide. From rest, Riptide still behaves normally because vanilla thrust is applied after the zero existing velocity is scaled.

## Compatibility

Exponential Elytra is expected to work alongside **Do a Barrel Roll**. It has no required or direct integration with that mod: it only scales the player's existing movement vector and never derives a new direction from yaw or pitch, or modifies roll, steering, look direction, or camera behavior.

Only server-authoritative, successful boosts are scaled. Rockets placed normally, launched by crossbows, or used while standing do not trigger the firework multiplier. Failed, insufficiently charged, dry, non-Riptide, or about-to-break trident uses do not trigger the Riptide multiplier. In multiplayer, install the mod on the server; installing it on clients is harmless but is not sufficient by itself.

## High-speed warning

Extreme multipliers can cause very high speeds, chunks failing to load quickly enough, unreliable collision handling, apparent server/client desynchronization, and increased server load. These are practical game-engine and networking limitations, not a hidden speed cap in this mod.

## Building

Java 21 is required.

```text
Windows:       .\gradlew.bat build
Linux/macOS:   ./gradlew build
```

The installable mod is generated at `build/libs/exponential-elytra-1.0.0.jar`. The similarly named `-sources.jar` is for development and should not be placed in the `mods` folder.
