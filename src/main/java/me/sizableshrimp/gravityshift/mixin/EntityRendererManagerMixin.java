package me.sizableshrimp.gravityshift.mixin;

import me.sizableshrimp.gravityshift.core.GravityAxisAlignedBB;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererManager.class)
public class EntityRendererManagerMixin {
    @Redirect(method = "renderBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/AxisAlignedBB;move(DDD)Lnet/minecraft/util/math/AxisAlignedBB;"))
    private AxisAlignedBB gravityshift$redirectMoveRenderBox(AxisAlignedBB bb, double x, double y, double z, CallbackInfo cb) {
        if (!(bb instanceof GravityAxisAlignedBB))
            return bb;
        return ((GravityAxisAlignedBB) bb).moveSuper(x, y, z);
    }
}
