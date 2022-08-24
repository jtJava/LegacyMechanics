package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class AttackSwingOrderMixin {
    @Shadow
    public ClientPlayerEntity player;

    @Redirect(method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    public void doNothing(ClientPlayerEntity instance, Hand hand) {
        // Do nothing if legacy, we don't want this method to do anything, as we
        // will just call it earlier in the parent method.
        if (!LegacyMechanics.getInstance().isLegacyActive()) {
            instance.swingHand(Hand.MAIN_HAND);
        }
    }

    @Redirect(method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"))
    public void swingAndAttack(ClientPlayerInteractionManager instance, PlayerEntity player, Entity target) {
        if (LegacyMechanics.getInstance().isLegacyActive()) this.player.swingHand(Hand.MAIN_HAND);
        instance.attackEntity(this.player, target);

    }

    @Redirect(method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    public boolean swingAndDig(ClientPlayerInteractionManager instance, BlockPos pos, Direction direction) {
        if (LegacyMechanics.getInstance().isLegacyActive()) this.player.swingHand(Hand.MAIN_HAND);
        return instance.attackBlock(pos, direction);
    }

    @Redirect(method = "Lnet/minecraft/client/MinecraftClient;doAttack()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    public void swingAndMiss(ClientPlayerEntity instance) {
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            this.player.swingHand(Hand.MAIN_HAND);
            this.player.resetLastAttackedTicks();
        }
    }
}
