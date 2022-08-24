package me.jaden.legacymechanics.mixin.mechanics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public class CooldownRenderFixMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @ModifyVariable(method = "updateHeldItems()V", at = @At("STORE"), ordinal = 0)
    private float injected(float x) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        return clientPlayerEntity.getAttackCooldownProgress(20);
    }
}
