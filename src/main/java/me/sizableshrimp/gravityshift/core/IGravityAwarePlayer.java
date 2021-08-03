package me.sizableshrimp.gravityshift.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;

public interface IGravityAwarePlayer {
    float getStandardEyeHeight();
    boolean isMotionRelative();
    boolean isMotionAbsolute();
    void makeMotionRelative();
    void makeMotionAbsolute();
    void popMotionStack();
    void makeRotationRelative();
    void makeRotationAbsolute();
    void popRotationStack();
    void storeMotion(Direction.Axis axis);
    Vector3d undoMotionChange(Direction.Axis axis);
    void makePositionRelative();
    void makePositionAbsolute();

    static IGravityAwarePlayer get(PlayerEntity player) {
        return (IGravityAwarePlayer) player;
    }

    default PlayerEntity toVanilla() {
        return (PlayerEntity) this;
    }
}
