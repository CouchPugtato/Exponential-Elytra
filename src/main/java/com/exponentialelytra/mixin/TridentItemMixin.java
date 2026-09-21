package com.exponentialelytra.mixin;

import com.exponentialelytra.ExponentialElytra;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentItem.class)
abstract class TridentItemMixin {
    @Inject(
            method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;push(DDD)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void exponentialElytra$scaleExistingVelocityBeforeRiptide(
            ItemStack stack,
            Level level,
            LivingEntity livingEntity,
            int remainingUseDuration,
            CallbackInfoReturnable<Boolean> callbackInfo
    ) {
        if (!(level instanceof ServerLevel) || !(livingEntity instanceof Player player)) {
            return;
        }

        double multiplier = ExponentialElytra.config().riptideVelocityMultiplier();
        if (multiplier == 1.0D) {
            return;
        }

        Vec3 currentVelocity = player.getDeltaMovement();
        player.setDeltaMovement(currentVelocity.scale(multiplier));
        player.hurtMarked = true;
    }
}
