package me.jaden.legacymechanics.mixin.features;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChatHud.class)
public class ChatFadeMixin {
    /**
     * @author Iowa
     * @reason I'm lazy
     */
    @Overwrite
    private static double getMessageOpacityMultiplier(int age) {
        if (age < 6) {
            return .2D * age;
        } else {
            double d = (double) age / 200.0;
            d = 1.0 - d;
            d *= 10.0;
            d = MathHelper.clamp(d, 0.0, 1.0);
            d *= d;
            return d;
        }
    }
}
