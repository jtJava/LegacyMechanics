package me.jaden.legacymod.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerEntity.class)
public class ArmFixMixin {
    /**
     * @author Iowa
     * @reason I'm lazy
     */
    @Overwrite
    public Arm getMainArm() {
        return Arm.RIGHT;
    }
}
