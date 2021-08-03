package me.sizableshrimp.gravityshift.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<?> type, World level) {
        super(type, level);
    }

    // @Inject(method = "travel",
    //         locals = LocalCapture.CAPTURE_FAILHARD,
    //         slice = @Slice(from = @At(
    //                 value = "INVOKE",
    //                 target = "Lnet/minecraft/world/World;hasChunkAt(Lnet/minecraft/util/math/BlockPos;)Z")),
    //         at = @At(value = "INVOKE",
    //                 target = "Lnet/minecraft/entity/LivingEntity;setDeltaMovement(DDD)V",
    //                 shift = At.Shift.AFTER))
    // public void injectTravel(Vector3d pTravelVector, CallbackInfo ci, double d0, ModifiableAttributeInstance gravity, boolean flag, FluidState fluidState, BlockPos blockPos, float f3, float f4,
    //         Vector3d vector3d5, double d2) {
    //     if (!(this instanceof IGravityAwarePlayer) || this.isNoGravity())
    //         return;
    //     IGravityAwarePlayer gravPlayer = (IGravityAwarePlayer) this;
    //     GravityDirection direction = GravityHolderCapability.getGravityDirection(gravPlayer);
    //     if (direction == GravityDirection.DOWN)
    //         return;
    //     d2 += d0;
    //     double x = vector3d5.x * (double)f4;
    //     double y = d2 * (double)0.98F;
    //     double z = vector3d5.z * (double)f4;
    //
    // }
}
