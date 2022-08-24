package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerInteractionManager.class)
public class ExtraPosPacketMixin {
    @Final
    @Shadow
    private ClientPlayNetworkHandler networkHandler;

    @Redirect(method = "interactItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"))
    public void doNothing(ClientPlayNetworkHandler instance, Packet<?> packet) {
        if (!LegacyMechanics.getInstance().isLegacyActive()) {
            this.networkHandler.sendPacket(packet);
        }
    }
}
