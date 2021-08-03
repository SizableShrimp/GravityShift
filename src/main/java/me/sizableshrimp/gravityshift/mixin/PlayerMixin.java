package me.sizableshrimp.gravityshift.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleStack;
import me.sizableshrimp.gravityshift.capability.GravityHolderCapability;
import me.sizableshrimp.gravityshift.capability.IGravityHolder;
import me.sizableshrimp.gravityshift.core.GravityAxisAlignedBB;
import me.sizableshrimp.gravityshift.core.GravityDirection;
import me.sizableshrimp.gravityshift.core.GravityState;
import me.sizableshrimp.gravityshift.core.IGravityAwarePlayer;
import me.sizableshrimp.gravityshift.util.GravityUtil;
import me.sizableshrimp.gravityshift.util.VectorUtil;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.function.Function;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements IGravityAwarePlayer {
    protected PlayerMixin(EntityType<? extends LivingEntity> type, World level) {
        super(type, level);
    }

    private final Deque<GravityState> motionFieldStateStack = new ArrayDeque<>();
    private final Map<Direction.Axis, DoubleStack> motionStoreMap = Arrays.stream(Direction.Axis.values())
            .collect(ImmutableMap.toImmutableMap(Function.identity(), a -> new DoubleArrayList()));
    private final Deque<GravityState> rotationFieldStateStack = new ArrayDeque<>();
    // Relative position doesn't exist, but this allows adjustments made in other methods such as 'this.posY+=3',
    // to correspond to whatever we think 'UP' is
    private boolean positionVarsAreRelative;
    private float rotationPitchStore;
    private float rotationYawStore;
    private boolean superConstructorFinished;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructorTail(World level, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        superConstructorFinished = true;
        this.motionFieldStateStack.push(GravityState.ABSOLUTE);
        this.rotationFieldStateStack.push(GravityState.ABSOLUTE);
        this.setBoundingBox(GravityUtil.getGravityAdjustedHitbox(this));
    }

    @Override
    public float getStandardEyeHeight() {
        return super.getEyeHeight();
    }

    @Override
    public boolean isMotionRelative() {
        return this.motionFieldStateStack.peek() == GravityState.RELATIVE;
    }

    @Override
    public boolean isMotionAbsolute() {
        return this.motionFieldStateStack.peek() == GravityState.ABSOLUTE;
    }

    private void makeMotionFieldsRelative() {
        Vector3d newDelta = GravityHolderCapability.getGravityDirection(this).getInverseAdjustmentFromDOWNDirection().adjustXYZValues(this.getDeltaMovement());
        this.setDeltaMovement(newDelta);
    }

    private void makeMotionFieldsAbsolute() {
        Vector3d newDelta = GravityHolderCapability.getGravityDirection(this).adjustXYZValues(this.getDeltaMovement());
        this.setDeltaMovement(newDelta);
    }

    public void makeMotionRelative() {
        GravityState top = this.motionFieldStateStack.peek();
        if (top == GravityState.ABSOLUTE) {
            this.makeMotionFieldsRelative();
        }
        this.motionFieldStateStack.push(GravityState.RELATIVE);
    }

    @Override
    public void makeMotionAbsolute() {
        GravityState top = this.motionFieldStateStack.peek();
        if (top == GravityState.RELATIVE) {
            this.makeMotionFieldsAbsolute();
        }
        this.motionFieldStateStack.push(GravityState.ABSOLUTE);
    }

    @Override
    public void popMotionStack() {
        GravityState removed = this.motionFieldStateStack.pop();
        GravityState top = this.motionFieldStateStack.peek();
        if (top != removed) {
            if (top == GravityState.ABSOLUTE) {
                this.makeMotionFieldsAbsolute();
            } else if (top == GravityState.RELATIVE) {
                this.makeMotionFieldsRelative();
            }
        }
    }

    private void makeRotationFieldsRelative() {
        final Vector3d fastAdjustedVectorForRotation = GravityHolderCapability.getGravityDirection(this)
                .getInverseAdjustmentFromDOWNDirection()
                .adjustXYZValues(VectorUtil.getFastVectorForRotation(this.xRot, this.yRot));
        VectorUtil.Rotation fastPitchAndYawFromVector = VectorUtil.getFastPitchAndYawFromVector(fastAdjustedVectorForRotation);

        // Store the absolute yaw and pitch to instance fields so they can be restored easily
        this.rotationYawStore = this.yRot;
        this.rotationPitchStore = this.xRot;
        this.yRot = (float) fastPitchAndYawFromVector.yaw();
        this.xRot = (float) fastPitchAndYawFromVector.pitch();
    }

    private void makeRotationFieldsAbsolute() {
        this.yRot = this.rotationYawStore;
        this.xRot = this.rotationPitchStore;
    }

    @Override
    public void makeRotationRelative() {
        GravityState top = this.rotationFieldStateStack.peek();
        if (top == GravityState.ABSOLUTE) {
            this.makeRotationFieldsRelative();
        }
        this.rotationFieldStateStack.push(GravityState.RELATIVE);
    }

    @Override
    public void makeRotationAbsolute() {
        GravityState top = this.rotationFieldStateStack.peek();
        if (top == GravityState.RELATIVE) {
            this.makeRotationFieldsAbsolute();
        }
        this.rotationFieldStateStack.push(GravityState.ABSOLUTE);
    }

    @Override
    public void popRotationStack() {
        GravityState removed = this.rotationFieldStateStack.pop();
        GravityState top = this.rotationFieldStateStack.peek();
        if (top != removed) {
            if (top == GravityState.ABSOLUTE) {
                this.makeRotationFieldsAbsolute();
            } else if (top == GravityState.RELATIVE) {
                this.makeRotationFieldsRelative();
            }
        }
    }

    @Override
    public void storeMotion(Direction.Axis axis) {
        this.motionStoreMap.get(axis).push(this.getDeltaMovement().get(axis));
    }

    @Override
    public Vector3d undoMotionChange(Direction.Axis axis) {
        double stored = this.motionStoreMap.get(axis).popDouble();
        Vector3d modified = this.getDeltaMovement().subtract(axis == Direction.Axis.X ? stored : 0,
                axis == Direction.Axis.Y ? stored : 0,
                axis == Direction.Axis.Z ? stored : 0);
        this.setDeltaMovement(modified);
        return modified;
    }

    private void invertPositions(GravityDirection direction) {
        Vector3d adjusted = direction.adjustXYZValues(this.position());
        this.setPos(adjusted.x(), adjusted.y(), adjusted.z());

        adjusted = direction.adjustXYZValues(this.xOld, this.yOld, this.zOld);
        this.xOld = adjusted.x();
        this.yOld = adjusted.y();
        this.zOld = adjusted.z();
    }

    @Override
    public void makePositionRelative() {
        if (!this.positionVarsAreRelative) {
            GravityDirection direction = GravityHolderCapability.getGravityDirection(this);
            if (direction == null)
                return;
            invertPositions(direction.getInverseAdjustmentFromDOWNDirection());
            this.positionVarsAreRelative = true;
        }
    }

    @Override
    public void makePositionAbsolute() {
        if (this.positionVarsAreRelative) {
            GravityDirection direction = GravityHolderCapability.getGravityDirection(this);
            if (direction == null)
                return;
            invertPositions(direction);
            this.positionVarsAreRelative = false;
        }
    }

    @Inject(method = "travel", at = @At("HEAD"))
    public void injectTravelHead(Vector3d pTravelVector, CallbackInfo ci) {
        // this.makePositionRelative();
    }

    @Inject(method = "travel", at = @At("RETURN"))
    public void injectTravelReturn(Vector3d pTravelVector, CallbackInfo ci) {
        // this.makePositionAbsolute();
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        AxisAlignedBB bb = super.getBoundingBox();
        if (superConstructorFinished && !(bb instanceof GravityAxisAlignedBB)) {
            IGravityHolder gravityHolder = GravityUtil.getGravityHolder(this);
            if (gravityHolder != null) {
                bb = new GravityAxisAlignedBB(gravityHolder, bb);
                super.setBoundingBox(bb);
            }
        }
        return bb;
    }

    @Override
    public void setBoundingBox(AxisAlignedBB bb) {
        if (superConstructorFinished && !(bb instanceof GravityAxisAlignedBB)) {
            IGravityHolder gravityHolder = GravityUtil.getGravityHolder(this);
            if (gravityHolder != null)
                bb = new GravityAxisAlignedBB(gravityHolder, bb);
        }
        super.setBoundingBox(bb);
    }

    @Override
    public void setLocationFromBoundingbox() {
        GravityDirection direction = GravityHolderCapability.getGravityDirection(this);
        if (direction == null) {
            super.setLocationFromBoundingbox();
            return;
        }
        direction.resetPositionToBB(this);
    }

    // TODO: do I need to override refreshDimensions? (setSize in 1.12)
    @Override
    public void refreshDimensions() {
        EntitySize oldSize = this.dimensions;
        Pose pose = this.getPose();
        EntitySize newSize = this.getDimensions(pose);
        EntityEvent.Size sizeEvent = net.minecraftforge.event.ForgeEventFactory.getEntitySizeForge(this, pose, oldSize, newSize, this.getEyeHeight(pose, newSize));
        newSize = sizeEvent.getNewSize();
        this.dimensions = newSize;
        this.eyeHeight = sizeEvent.getNewEyeHeight();

        double heightExpansion = (newSize.height - oldSize.height) / 2d;
        double widthExpansion = (newSize.width - oldSize.width) / 2d;

        AxisAlignedBB oldBb = this.getBoundingBox();
        AxisAlignedBB newBb = oldBb.inflate(widthExpansion, heightExpansion, widthExpansion);
        this.setBoundingBox(newBb.move(0, heightExpansion, 0));

        if (newSize.width > oldSize.width && !this.firstTick && !this.level.isClientSide) {
            this.move(MoverType.SELF, new Vector3d(oldSize.width - newSize.width, 0, oldSize.width - newSize.width));
        }
    }

    // @Override
    // public float getEyeHeight() {
    //     GravityDirection direction = GravityHolderCapability.getGravityDirection(this);
    //     if (direction == null)
    //         return super.getEyeHeight();
    //     return direction.getEntityEyeHeight(this);
    // }

    @Override
    public void turn(double yaw, double pitch) {
        GravityDirection direction = GravityHolderCapability.getGravityDirection(this);
        if (direction == null) {
            super.turn(yaw, pitch);
            return;
        }
        GravityDirection inverse = direction.getInverseAdjustmentFromDOWNDirection();

        double relativeYawChange = yaw * 0.15d;
        double relativePitchChange = -pitch * 0.15d;

        Vector3d normalLookVec = VectorUtil.getPreciseVectorForRotation(this.xRot, this.yRot);

        final Vector3d relativeLookVec = inverse.adjustXYZValues(normalLookVec);
        final VectorUtil.Rotation relativeRot = VectorUtil.getPrecisePitchAndYawFromVector(relativeLookVec);

        final double changedRelativeYaw = relativeRot.yaw() + relativeYawChange;
        // We need to clamp the pitch.
        // Any closer to -90 or 90 produce tiny values that the trig functions will effectively treat as zero
        // this causes an inability to rotate the camera when looking straight up or down
        // While, it's not a problem for UP and DOWN directions, it causes issues when going from UP/DOWN to a different
        // direction, so I've capped UP and DOWN directions as well
        final double changedRelativePitch = MathHelper.clamp(relativeRot.pitch() + relativePitchChange, -89.99, 89.99);

        // Directly set pitch and yaw
        final Vector3d relativeChangedLookVec = VectorUtil.getPreciseVectorForRotation(changedRelativePitch, changedRelativeYaw);

        final Vector3d absoluteLookVec = direction.adjustXYZValues(relativeChangedLookVec);
        final VectorUtil.Rotation absoluteRot = VectorUtil.getPrecisePitchAndYawFromVector(absoluteLookVec);

        final double changedAbsolutePitch = absoluteRot.pitch();
        final double changedAbsoluteYaw = (absoluteRot.yaw() % 360);

        // Yaw calculated through yaw change
        final double effectiveStartingAbsoluteYaw = this.yRot % 360;
        final double absoluteYawChange = changedAbsoluteYaw - effectiveStartingAbsoluteYaw;

        // Limit the change in yaw to 180 degrees each tick
        // if (Math.abs(effectiveStartingAbsoluteYaw - changedAbsoluteYaw) > 180) {
        //     if (effectiveStartingAbsoluteYaw < changedAbsoluteYaw) {
        //         absoluteYawChange = changedAbsoluteYaw - (effectiveStartingAbsoluteYaw + 360);
        //     } else {
        //         absoluteYawChange = (changedAbsoluteYaw + 360) - effectiveStartingAbsoluteYaw;
        //     }
        // } else {
        //     absoluteYawChange = changedAbsoluteYaw - effectiveStartingAbsoluteYaw;
        // }

        float yawParam = (float) ((absoluteYawChange) / 0.15);
        float pitchParam = (float) ((this.xRot - changedAbsolutePitch) / 0.15);

        super.turn(yawParam, pitchParam);
    }
    //
    @Override
    public void move(MoverType pType, Vector3d pPos) {
        if (this.isMotionRelative()) {
            super.move(pType, pPos);
            return;
        }
        AxisAlignedBB bb = this.getBoundingBox();
        Vector3d target;
        if (bb instanceof GravityAxisAlignedBB) {
            target = ((GravityAxisAlignedBB) bb).getDirection().getInverseAdjustmentFromDOWNDirection().adjustXYZValues(pPos);
        } else {
            target = pPos;
        }
        this.makeMotionRelative();
        super.move(pType, target);
        this.popMotionStack();
    }
    //
    @Override
    public void jumpFromGround() {
        this.makeMotionRelative();
        super.jumpFromGround();
        this.popMotionStack();
    }
}
