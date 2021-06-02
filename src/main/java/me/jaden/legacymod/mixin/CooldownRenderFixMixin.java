package me.jaden.legacymod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class CooldownRenderFixMixin {

	@Shadow
	MinecraftClient client;

	@ModifyVariable(method = "updateHeldItems()V", at = @At("STORE"), ordinal = 0)
	private float injected(float x) {
		ClientPlayerEntity clientPlayerEntity = this.client.player;
		return clientPlayerEntity.getAttackCooldownProgress(20);
	}
}
