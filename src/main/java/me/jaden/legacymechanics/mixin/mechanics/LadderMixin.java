package me.jaden.legacymechanics.mixin.mechanics;

import net.minecraft.block.Block;
import net.minecraft.block.LadderBlock;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LadderBlock.class)
public class LadderMixin {
    @Mutable
    @Final
    @Shadow
    protected static VoxelShape EAST_SHAPE;
    @Mutable

    @Final
    @Shadow
    protected static VoxelShape WEST_SHAPE;

    @Mutable
    @Final
    @Shadow
    protected static VoxelShape SOUTH_SHAPE;

    @Mutable
    @Final
    @Shadow
    protected static VoxelShape NORTH_SHAPE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void injected(CallbackInfo ci) {
        EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);
        WEST_SHAPE = Block.createCuboidShape(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);
        NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);
    }
}
