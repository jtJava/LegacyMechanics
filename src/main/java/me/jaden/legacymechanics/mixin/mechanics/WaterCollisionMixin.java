package me.jaden.legacymechanics.mixin.mechanics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class WaterCollisionMixin {
    @ModifyVariable(method = "Lnet/minecraft/entity/Entity;updateMovementInFluid(Lnet/minecraft/tag/TagKey;D)Z", at = @At("STORE"), ordinal = 0)
    private Box injected(Box box) {
        return ((Entity) (Object) this).getBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D);
    }
}
