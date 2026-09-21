package com.exponentialelytra.mixin;

import com.exponentialelytra.ExponentialElytra;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkRocketItem.class)
abstract class FireworkRocketItemMixin {
    @Inject(
            method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/Projectile;spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/Projectile;",
                    shift = At.Shift.BEFORE
            )
    )
    private void exponentialElytra$scaleExistingVelocity(
            Level level,
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> callbackInfo
    ) {
        double multiplier = ExponentialElytra.config().rocketVelocityMultiplier();
        if (multiplier == 1.0D) {
            return;
        }

        Vec3 currentVelocity = player.getDeltaMovement();
        player.setDeltaMovement(currentVelocity.scale(multiplier));
        player.hurtMarked = true;
    }
}
