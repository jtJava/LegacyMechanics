package me.jaden.legacymechanics.mixin.features;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class SwingFixMixin {
    @Shadow
    protected int attackCooldown;

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void doAttackHead(CallbackInfoReturnable<Boolean> cir) {
        attackCooldown = 0;
    }
}
