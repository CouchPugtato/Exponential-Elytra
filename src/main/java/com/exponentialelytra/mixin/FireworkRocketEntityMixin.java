package com.exponentialelytra.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FireworkRocketEntity.class)
abstract class FireworkRocketEntityMixin {
    @Shadow
    private LivingEntity attachedToEntity;

    @ModifyArg(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            ),
            index = 0
    )
    private Vec3 exponentialElytra$preventRocketFromRemovingMomentum(Vec3 vanillaVelocity) {
        if (attachedToEntity == null) {
            return vanillaVelocity;
        }

        Vec3 currentVelocity = attachedToEntity.getDeltaMovement();
        double currentSpeedSquared = currentVelocity.lengthSqr();
        double vanillaSpeedSquared = vanillaVelocity.lengthSqr();
        if (vanillaSpeedSquared >= currentSpeedSquared || vanillaSpeedSquared == 0.0D) {
            return vanillaVelocity;
        }

        Vec3 preservedVelocity = vanillaVelocity.scale(Math.sqrt(currentSpeedSquared / vanillaSpeedSquared));
        if (!attachedToEntity.level().isClientSide()) {
            attachedToEntity.hurtMarked = true;
        }
        return preservedVelocity;
    }
}
