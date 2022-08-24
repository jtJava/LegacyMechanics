package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public class InertiaFixMixin {
    @ModifyConstant(method = "Lnet/minecraft/entity/LivingEntity;tickMovement()V", constant = @Constant(doubleValue = 0.003))
    private double injected(double value) {
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            return 0.005D;
        } else {
            return 0.003D;
        }
    }
}
