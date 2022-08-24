package me.jaden.legacymechanics.mixin.mechanics;

import me.jaden.legacymechanics.LegacyMechanics;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Entity.class)
public class ReachFixMixin {
    /**
     * @author Iowa
     * @reason Reach fix
     */
    @Overwrite
    public float getTargetingMargin() {
        if (LegacyMechanics.getInstance().isLegacyActive()) {
            return 0.1f;
        } else {
            return 0.0f;
        }
    }
}
