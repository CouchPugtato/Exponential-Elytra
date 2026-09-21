# Exponential Elytra

Exponential Elytra is a small, server-authoritative Fabric mod for Minecraft Java Edition 1.21.10. Whenever a player successfully uses a firework rocket while actively Elytra gliding, the mod uniformly scales the player's current velocity vector. Vanilla then applies its normal firework boost.

With the default multiplier of `1.10`, a velocity of 40 blocks per second becomes 44 blocks per second before vanilla firework acceleration is applied. Every component (X, Y, and Z) is scaled by the same amount, so the mod preserves the current flight direction and does not replace vanilla movement, steering, or camera behavior.

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
  "rocketVelocityMultiplier": 1.1
}
```

`rocketVelocityMultiplier` is a non-negative finite number. For example, `1.0` adds no exponential scaling, `1.2` adds 20 percent to the current velocity, and `2.0` doubles it before vanilla acceleration. Invalid, negative, non-finite, or malformed values are logged and replaced in memory by the safe default of `1.10`; a malformed existing file is left intact so it can be corrected.

## Compatibility

Exponential Elytra is expected to work alongside **Do a Barrel Roll**. It has no required or direct integration with that mod: it only scales the player's existing movement vector and never derives a new direction from yaw or pitch, or modifies roll, steering, look direction, or camera behavior.

Only server-side, actively fall-flying rocket uses are affected. Rockets placed normally, launched by crossbows, or used while standing do not trigger the multiplier. In multiplayer, install the mod on the server; installing it on clients is harmless but is not sufficient by itself.

## High-speed warning

Extreme multipliers can cause very high speeds, chunks failing to load quickly enough, unreliable collision handling, apparent server/client desynchronization, and increased server load. These are practical game-engine and networking limitations, not a hidden speed cap in this mod.

## Building

Java 21 is required.

```text
Windows:       .\gradlew.bat build
Linux/macOS:   ./gradlew build
```

The installable mod is generated at `build/libs/exponential-elytra-1.0.0.jar`. The similarly named `-sources.jar` is for development and should not be placed in the `mods` folder.
