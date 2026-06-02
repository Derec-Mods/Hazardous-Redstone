package io.github.derexxd.hazardous_redstone.mixin;

import io.github.derexxd.hazardous_redstone.RedstoneShockHandler;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void hazardous_redstone$shockOnRedstone(CallbackInfo ci) {
        RedstoneShockHandler.tick((LivingEntity) (Object) this);
    }
}