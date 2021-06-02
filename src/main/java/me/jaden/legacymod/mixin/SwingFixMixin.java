package me.jaden.legacymod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class SwingFixMixin {

	@Shadow
	private int attackCooldown;

	@Inject(method = "doAttack", at = @At("HEAD"))
	private void doAttackHead(CallbackInfo info) {
		attackCooldown = 0;
	}
}
