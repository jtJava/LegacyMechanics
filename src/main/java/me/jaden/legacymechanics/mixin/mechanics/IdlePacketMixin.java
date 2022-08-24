package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerEntity.class)
public abstract class IdlePacketMixin {
    @Final
    @Shadow
    public
    ClientPlayNetworkHandler networkHandler;

    @Final
    @Shadow
    protected MinecraftClient client;
    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean lastSneaking;
    @Shadow
    private boolean lastSprinting;
    @Shadow
    private double lastX;
    @Shadow
    private double lastBaseY;
    @Shadow
    private float lastYaw;
    @Shadow
    private double lastZ;
    @Shadow
    private float lastPitch;
    @Shadow
    private int ticksSinceLastPositionPacketSent;
    @Shadow
    private boolean autoJumpEnabled;

    /**
     * @author Iowa
     * @reason I'm also lazy here too.
     */
    @Overwrite
    private void sendMovementPackets() {
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            moveLegacy();
        } else {
            moveModern();
        }
    }

    private void moveLegacy() {
        boolean bl2;
        boolean bl = ((ClientPlayerEntity) (Object) this).isSprinting();
        if (bl != this.lastSprinting) {
            ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(((ClientPlayerEntity) (Object) this), mode));
            this.lastSprinting = bl;
        }
        if ((bl2 = ((ClientPlayerEntity) (Object) this).isSneaking()) != this.lastSneaking) {
            ClientCommandC2SPacket.Mode mode2 = bl2 ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(((ClientPlayerEntity) (Object) this), mode2));
            this.lastSneaking = bl2;
        }
        if (client.getCameraEntity() == (Object) this) {
            boolean bl4;
            double d = ((ClientPlayerEntity) (Object) this).getX() - this.lastX;
            double e = ((ClientPlayerEntity) (Object) this).getY() - this.lastBaseY;
            double f = ((ClientPlayerEntity) (Object) this).getZ() - this.lastZ;
            double g = ((ClientPlayerEntity) (Object) this).getYaw() - this.lastYaw;
            double h = ((ClientPlayerEntity) (Object) this).getPitch() - this.lastPitch;
            boolean bl3 = d * d + e * e + f * f > 9.0E-4D || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl5 = bl4 = g != 0.0 || h != 0.0;
            if (((ClientPlayerEntity) (Object) this).hasVehicle()) {
                Vec3d vec3d = ((ClientPlayerEntity) (Object) this).getVelocity();
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, ((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
                bl3 = false;
            } else if (bl3 && bl4) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(((ClientPlayerEntity) (Object) this).getX(), ((ClientPlayerEntity) (Object) this).getY(), ((ClientPlayerEntity) (Object) this).getZ(), ((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else if (bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(((ClientPlayerEntity) (Object) this).getX(), ((ClientPlayerEntity) (Object) this).getY(), ((ClientPlayerEntity) (Object) this).getZ(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else if (bl4) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(((ClientPlayerEntity) (Object) this).isOnGround()));
            }
            ++this.ticksSinceLastPositionPacketSent;
            if (bl3) {
                this.lastX = ((ClientPlayerEntity) (Object) this).getX();
                this.lastBaseY = ((ClientPlayerEntity) (Object) this).getY();
                this.lastZ = ((ClientPlayerEntity) (Object) this).getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }
            if (bl4) {
                this.lastYaw = ((ClientPlayerEntity) (Object) this).getYaw();
                this.lastPitch = ((ClientPlayerEntity) (Object) this).getPitch();
            }
            this.lastOnGround = ((ClientPlayerEntity) (Object) this).isOnGround();
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }
    }

    private void moveModern() {
        boolean bl2;
        boolean bl = ((ClientPlayerEntity) (Object) this).isSprinting();
        if (bl != this.lastSprinting) {
            ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(((ClientPlayerEntity) (Object) this), mode));
            this.lastSprinting = bl;
        }
        if ((bl2 = ((ClientPlayerEntity) (Object) this).isSneaking()) != this.lastSneaking) {
            ClientCommandC2SPacket.Mode mode2 = bl2 ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
            this.networkHandler.sendPacket(new ClientCommandC2SPacket(((ClientPlayerEntity) (Object) this), mode2));
            this.lastSneaking = bl2;
        }
        if (client.getCameraEntity() == (Object) this) {
            boolean bl4;
            double d = ((ClientPlayerEntity) (Object) this).getX() - this.lastX;
            double e = ((ClientPlayerEntity) (Object) this).getY() - this.lastBaseY;
            double f = ((ClientPlayerEntity) (Object) this).getZ() - this.lastZ;
            double g = ((ClientPlayerEntity) (Object) this).getYaw() - this.lastYaw;
            double h = ((ClientPlayerEntity) (Object) this).getPitch() - this.lastPitch;
            ++this.ticksSinceLastPositionPacketSent;
            boolean bl3 = MathHelper.squaredMagnitude(d, e, f) > MathHelper.square(2.0E-4) || this.ticksSinceLastPositionPacketSent >= 20;
            boolean bl5 = bl4 = g != 0.0 || h != 0.0;
            if (((ClientPlayerEntity) (Object) this).hasVehicle()) {
                Vec3d vec3d = ((ClientPlayerEntity) (Object) this).getVelocity();
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0, vec3d.z, ((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
                bl3 = false;
            } else if (bl3 && bl4) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(((ClientPlayerEntity) (Object) this).getX(), ((ClientPlayerEntity) (Object) this).getY(), ((ClientPlayerEntity) (Object) this).getZ(), ((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else if (bl3) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(((ClientPlayerEntity) (Object) this).getX(), ((ClientPlayerEntity) (Object) this).getY(), ((ClientPlayerEntity) (Object) this).getZ(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else if (bl4) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(((ClientPlayerEntity) (Object) this).getYaw(), ((ClientPlayerEntity) (Object) this).getPitch(), ((ClientPlayerEntity) (Object) this).isOnGround()));
            } else if (this.lastOnGround != ((ClientPlayerEntity) (Object) this).isOnGround()) {
                this.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(((ClientPlayerEntity) (Object) this).isOnGround()));
            }
            if (bl3) {
                this.lastX = ((ClientPlayerEntity) (Object) this).getX();
                this.lastBaseY = ((ClientPlayerEntity) (Object) this).getY();
                this.lastZ = ((ClientPlayerEntity) (Object) this).getZ();
                this.ticksSinceLastPositionPacketSent = 0;
            }
            if (bl4) {
                this.lastYaw = ((ClientPlayerEntity) (Object) this).getYaw();
                this.lastPitch = ((ClientPlayerEntity) (Object) this).getPitch();
            }
            this.lastOnGround = ((ClientPlayerEntity) (Object) this).isOnGround();
            this.autoJumpEnabled = this.client.options.getAutoJump().getValue();
        }
    }
}
