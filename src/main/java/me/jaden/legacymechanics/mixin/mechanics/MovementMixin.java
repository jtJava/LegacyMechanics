package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import me.jaden.legacymechanics.mixin.accessors.EntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class MovementMixin {
    /**
     * @author Iowa
     * @reason I'm lazy
     */
    @Overwrite
    public void travel(Vec3d movementInput) {
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            travelLegacy(movementInput);
        } else {
            travelModern(movementInput);
        }
    }

    @Shadow
    protected abstract boolean shouldSwimInFluids();

    @Shadow
    protected abstract float getBaseMovementSpeedMultiplier();

    @Shadow
    protected abstract SoundEvent getFallSound(int distance);

    @Shadow
    @Final
    private static int FALL_FLYING_FLAG;

    private void travelLegacy(Vec3d movementInput) {
        if (((LivingEntity) (Object) this).canMoveVoluntarily() || ((LivingEntity) (Object) this).isLogicalSideForUpdatingMovement()) {
            double d = 0.08;
            boolean falling = ((LivingEntity) (Object) this).getVelocity().y <= 0.0;
            FluidState fluidState = ((LivingEntity) (Object) this).world.getFluidState(((LivingEntity) (Object) this).getBlockPos());
            if (((LivingEntity) (Object) this).isTouchingWater() && this.shouldSwimInFluids() && !((LivingEntity) (Object) this).canWalkOnFluid(fluidState)) {
                double e = ((LivingEntity) (Object) this).getY();
                float f = 0.8f;
                float g = 0.02f;
                float h = EnchantmentHelper.getDepthStrider(((LivingEntity) (Object) this));

                if (h > 3.0f) {
                    h = 3.0f;
                }

                if (!((LivingEntity) (Object) this).isOnGround()) {
                    h *= 0.5f;
                }

                if (h > 0.0f) {
                    f += (0.54600006f - f) * h / 3.0f;
                    g += (((LivingEntity) (Object) this).getMovementSpeed() - g) * h / 3.0f;
                }

                ((LivingEntity) (Object) this).updateVelocity(g, movementInput);
                ((LivingEntity) (Object) this).move(MovementType.SELF, ((LivingEntity) (Object) this).getVelocity());
                Vec3d vec3d = ((LivingEntity) (Object) this).getVelocity();
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }
                ((LivingEntity) (Object) this).setVelocity(vec3d.multiply(f, 0.8f, f));
                Vec3d vec3d2 = this.applyFluidMovingSpeed(d, falling, ((LivingEntity) (Object) this).getVelocity());
                ((LivingEntity) (Object) this).setVelocity(vec3d2);
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).doesNotCollide(vec3d2.x, vec3d2.y + (double) 0.6f - ((LivingEntity) (Object) this).getY() + e, vec3d2.z)) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d2.x, 0.3f, vec3d2.z);
                }
            } else if (((LivingEntity) (Object) this).isInLava() && this.shouldSwimInFluids() && !((LivingEntity) (Object) this).canWalkOnFluid(fluidState)) {
                Vec3d vec3d3;
                double e = ((LivingEntity) (Object) this).getY();
                ((LivingEntity) (Object) this).updateVelocity(0.02f, movementInput);
                ((LivingEntity) (Object) this).move(MovementType.SELF, ((LivingEntity) (Object) this).getVelocity());
                if (((LivingEntity) (Object) this).getFluidHeight(FluidTags.LAVA) <= ((LivingEntity) (Object) this).getSwimHeight()) {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().multiply(0.5, 0.8f, 0.5));
                    vec3d3 = this.applyFluidMovingSpeed(d, falling, ((LivingEntity) (Object) this).getVelocity());
                    ((LivingEntity) (Object) this).setVelocity(vec3d3);
                } else {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().multiply(0.5));
                }
                if (!((LivingEntity) (Object) this).hasNoGravity()) {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().add(0.0, -d / 4.0, 0.0));
                }
                vec3d3 = ((LivingEntity) (Object) this).getVelocity();
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).doesNotCollide(vec3d3.x, vec3d3.y + (double) 0.6f - ((LivingEntity) (Object) this).getY() + e, vec3d3.z)) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d3.x, 0.3f, vec3d3.z);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                float p = ((LivingEntity) (Object) this).world.getBlockState(blockPos).getBlock().getSlipperiness();
                float f = ((LivingEntity) (Object) this).isOnGround() ? p * 0.91f : 0.91f;
                Vec3d vec3d6 = ((LivingEntity) (Object) this).applyMovementInput(movementInput, p);
                double q = vec3d6.y;
                if (((LivingEntity) (Object) this).hasStatusEffect(StatusEffects.LEVITATION)) {
                    q += (0.05 * (double) (((LivingEntity) (Object) this).getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
                    ((LivingEntity) (Object) this).onLanding();
                } else if (!((LivingEntity) (Object) this).world.isClient || ((LivingEntity) (Object) this).world.isChunkLoaded(blockPos)) {
                    if (!((LivingEntity) (Object) this).hasNoGravity()) {
                        q -= d;
                    }
                } else {
                    q = ((LivingEntity) (Object) this).getY() > (double) ((LivingEntity) (Object) this).world.getBottomY() ? -0.1 : 0.0;
                }
                if (((LivingEntity) (Object) this).hasNoDrag()) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d6.x, q, vec3d6.z);
                } else {
                    ((LivingEntity) (Object) this).setVelocity(vec3d6.x * (double) f, q * (double) 0.98f, vec3d6.z * (double) f);
                }
            }
        }
        ((LivingEntity) (Object) this).updateLimbs(((LivingEntity) (Object) this), this instanceof Flutterer);
    }

    private void travelModern(Vec3d movementInput) {
        if (((LivingEntity) (Object) this).canMoveVoluntarily() || ((LivingEntity) (Object) this).isLogicalSideForUpdatingMovement()) {
            boolean bl;
            double d = 0.08;
            boolean bl2 = bl = ((LivingEntity) (Object) this).getVelocity().y <= 0.0;
            if (bl && ((LivingEntity) (Object) this).hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                d = 0.01;
                ((LivingEntity) (Object) this).onLanding();
            }
            FluidState fluidState = ((LivingEntity) (Object) this).world.getFluidState(((LivingEntity) (Object) this).getBlockPos());
            if (((LivingEntity) (Object) this).isTouchingWater() && this.shouldSwimInFluids() && !((LivingEntity) (Object) this).canWalkOnFluid(fluidState)) {
                double e = ((LivingEntity) (Object) this).getY();
                float f = ((LivingEntity) (Object) this).isSprinting() ? 0.9f : this.getBaseMovementSpeedMultiplier();
                float g = 0.02f;
                float h = EnchantmentHelper.getDepthStrider(((LivingEntity) (Object) this));
                if (h > 3.0f) {
                    h = 3.0f;
                }
                if (!((LivingEntity) (Object) this).isOnGround()) {
                    h *= 0.5f;
                }
                if (h > 0.0f) {
                    f += (0.54600006f - f) * h / 3.0f;
                    g += (((LivingEntity) (Object) this).getMovementSpeed() - g) * h / 3.0f;
                }
                if (((LivingEntity) (Object) this).hasStatusEffect(StatusEffects.DOLPHINS_GRACE)) {
                    f = 0.96f;
                }
                ((LivingEntity) (Object) this).updateVelocity(g, movementInput);
                ((LivingEntity) (Object) this).move(MovementType.SELF, ((LivingEntity) (Object) this).getVelocity());
                Vec3d vec3d = ((LivingEntity) (Object) this).getVelocity();
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).isClimbing()) {
                    vec3d = new Vec3d(vec3d.x, 0.2, vec3d.z);
                }
                ((LivingEntity) (Object) this).setVelocity(vec3d.multiply(f, 0.8f, f));
                Vec3d vec3d2 = ((LivingEntity) (Object) this).applyFluidMovingSpeed(d, bl, ((LivingEntity) (Object) this).getVelocity());
                ((LivingEntity) (Object) this).setVelocity(vec3d2);
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).doesNotCollide(vec3d2.x, vec3d2.y + (double) 0.6f - ((LivingEntity) (Object) this).getY() + e, vec3d2.z)) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d2.x, 0.3f, vec3d2.z);
                }
            } else if (((LivingEntity) (Object) this).isInLava() && this.shouldSwimInFluids() && !((LivingEntity) (Object) this).canWalkOnFluid(fluidState)) {
                Vec3d vec3d3;
                double e = ((LivingEntity) (Object) this).getY();
                ((LivingEntity) (Object) this).updateVelocity(0.02f, movementInput);
                ((LivingEntity) (Object) this).move(MovementType.SELF, ((LivingEntity) (Object) this).getVelocity());
                if (((LivingEntity) (Object) this).getFluidHeight(FluidTags.LAVA) <= ((LivingEntity) (Object) this).getSwimHeight()) {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().multiply(0.5, 0.8f, 0.5));
                    vec3d3 = ((LivingEntity) (Object) this).applyFluidMovingSpeed(d, bl, ((LivingEntity) (Object) this).getVelocity());
                    ((LivingEntity) (Object) this).setVelocity(vec3d3);
                } else {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().multiply(0.5));
                }
                if (!((LivingEntity) (Object) this).hasNoGravity()) {
                    ((LivingEntity) (Object) this).setVelocity(((LivingEntity) (Object) this).getVelocity().add(0.0, -d / 4.0, 0.0));
                }
                vec3d3 = ((LivingEntity) (Object) this).getVelocity();
                if (((LivingEntity) (Object) this).horizontalCollision && ((LivingEntity) (Object) this).doesNotCollide(vec3d3.x, vec3d3.y + (double) 0.6f - ((LivingEntity) (Object) this).getY() + e, vec3d3.z)) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d3.x, 0.3f, vec3d3.z);
                }
            } else if (((LivingEntity) (Object) this).isFallFlying()) {
                double n;
                float o;
                double m;
                Vec3d vec3d4 = ((LivingEntity) (Object) this).getVelocity();
                if (vec3d4.y > -0.5) {
                    ((LivingEntity) (Object) this).fallDistance = 1.0f;
                }
                Vec3d vec3d5 = ((LivingEntity) (Object) this).getRotationVector();
                float f = ((LivingEntity) (Object) this).getPitch() * ((float) Math.PI / 180);
                double i = Math.sqrt(vec3d5.x * vec3d5.x + vec3d5.z * vec3d5.z);
                double j = vec3d4.horizontalLength();
                double k = vec3d5.length();
                double l = Math.cos(f);
                l = l * l * Math.min(1.0, k / 0.4);
                vec3d4 = ((LivingEntity) (Object) this).getVelocity().add(0.0, d * (-1.0 + l * 0.75), 0.0);
                if (vec3d4.y < 0.0 && i > 0.0) {
                    m = vec3d4.y * -0.1 * l;
                    vec3d4 = vec3d4.add(vec3d5.x * m / i, m, vec3d5.z * m / i);
                }
                if (f < 0.0f && i > 0.0) {
                    m = j * (double) (-MathHelper.sin(f)) * 0.04;
                    vec3d4 = vec3d4.add(-vec3d5.x * m / i, m * 3.2, -vec3d5.z * m / i);
                }
                if (i > 0.0) {
                    vec3d4 = vec3d4.add((vec3d5.x / i * j - vec3d4.x) * 0.1, 0.0, (vec3d5.z / i * j - vec3d4.z) * 0.1);
                }
                ((LivingEntity) (Object) this).setVelocity(vec3d4.multiply(0.99f, 0.98f, 0.99f));
                ((LivingEntity) (Object) this).move(MovementType.SELF, ((LivingEntity) (Object) this).getVelocity());
                if (((LivingEntity) (Object) this).horizontalCollision && !((LivingEntity) (Object) this).world.isClient && (o = (float) ((n = j - (m = ((LivingEntity) (Object) this).getVelocity().horizontalLength())) * 10.0 - 3.0)) > 0.0f) {
                    ((LivingEntity) (Object) this).playSound(this.getFallSound((int) o), 1.0f, 1.0f);
                    ((LivingEntity) (Object) this).damage(DamageSource.FLY_INTO_WALL, o);
                }
                if (((LivingEntity) (Object) this).isOnGround() && !((LivingEntity) (Object) this).world.isClient) {
                    ((EntityAccessor) ((LivingEntity) (Object) this)).callSetFlag(FALL_FLYING_FLAG, false);
                }
            } else {
                BlockPos blockPos = this.getVelocityAffectingPos();
                float p = ((LivingEntity) (Object) this).world.getBlockState(blockPos).getBlock().getSlipperiness();
                float f = ((LivingEntity) (Object) this).isOnGround() ? p * 0.91f : 0.91f;
                Vec3d vec3d6 = ((LivingEntity) (Object) this).applyMovementInput(movementInput, p);
                double q = vec3d6.y;
                if (((LivingEntity) (Object) this).hasStatusEffect(StatusEffects.LEVITATION)) {
                    q += (0.05 * (double) (((LivingEntity) (Object) this).getStatusEffect(StatusEffects.LEVITATION).getAmplifier() + 1) - vec3d6.y) * 0.2;
                    ((LivingEntity) (Object) this).onLanding();
                } else if (!((LivingEntity) (Object) this).world.isClient || ((LivingEntity) (Object) this).world.isChunkLoaded(blockPos)) {
                    if (!((LivingEntity) (Object) this).hasNoGravity()) {
                        q -= d;
                    }
                } else {
                    q = ((LivingEntity) (Object) this).getY() > (double) ((LivingEntity) (Object) this).world.getBottomY() ? -0.1 : 0.0;
                }
                if (((LivingEntity) (Object) this).hasNoDrag()) {
                    ((LivingEntity) (Object) this).setVelocity(vec3d6.x, q, vec3d6.z);
                } else {
                    ((LivingEntity) (Object) this).setVelocity(vec3d6.x * (double) f, q * (double) 0.98f, vec3d6.z * (double) f);
                }
            }
        }
        ((LivingEntity) (Object) this).updateLimbs(((LivingEntity) (Object) this), this instanceof Flutterer);
    }

    public Vec3d applyFluidMovingSpeed(double gravity, boolean falling, Vec3d motion) {
        if (!((LivingEntity) (Object) this).hasNoGravity() && !((LivingEntity) (Object) this).isSprinting()) {
            double d = falling && Math.abs(motion.y - 0.005) >= 0.005 && Math.abs(motion.y - gravity / 16.0) < 0.005 ? -0.005 : motion.y - gravity / 16.0;
            return new Vec3d(motion.x, motion.y - 0.02D, motion.z);
        }
        return motion;
    }


    protected BlockPos getVelocityAffectingPos() {
        return new BlockPos(((LivingEntity) (Object) this).getPos().x, ((LivingEntity) (Object) this).getBoundingBox().minY - 0.5000001, ((LivingEntity) (Object) this).getPos().z);
    }
}
