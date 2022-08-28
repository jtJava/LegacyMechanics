package me.jaden.legacymechanics.mixin.mechanics;

import com.mojang.authlib.GameProfile;
import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class SprintMixin extends PlayerEntity {
    public SprintMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Redirect(method = "Lnet/minecraft/client/network/ClientPlayerEntity;tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tickMovement()V"))
    public void doNothing(AbstractClientPlayerEntity instance) {
        // Sprint fix
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            if (instance.isBlocking()
                    || instance.isUsingItem()
                    || instance.isSneaking()
                    || instance.isSubmergedInWater()) {
                instance.setSprinting(false);
            }
        }
        super.tickMovement();
    }
}

