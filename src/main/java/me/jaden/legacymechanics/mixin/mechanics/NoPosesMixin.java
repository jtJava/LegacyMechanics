package me.jaden.legacymechanics.mixin.mechanics;


import me.jaden.legacymechanics.mixin.accessors.EntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class NoPosesMixin {

    @Final
    @Shadow
    protected static TrackedData<EntityPose> POSE;

    /**
     * @author Iowa
     * @reason I'm lazy
     */
    @Overwrite
    public void setPose(EntityPose pose) {
        if (pose == EntityPose.SWIMMING) {
            pose = EntityPose.STANDING;
        } else if (pose == EntityPose.FALL_FLYING) {
            pose = EntityPose.CROUCHING;
        }
        ((Entity) (Object) this).getDataTracker().set(POSE, pose);
    }

    @Shadow
    @Final
    private static int SWIMMING_FLAG_INDEX;

    /**
     * @author Iowa
     * @reason I'm lazy
     */
    @Overwrite
    public void setSwimming(boolean swimming) {
        ((EntityAccessor) (Object) this).callSetFlag(SWIMMING_FLAG_INDEX, false);
    }
}
