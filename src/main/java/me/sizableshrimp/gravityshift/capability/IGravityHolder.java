package me.sizableshrimp.gravityshift.capability;

import me.sizableshrimp.gravityshift.core.GravityDirection;
import me.sizableshrimp.gravityshift.network.GravityStatusPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public interface IGravityHolder extends INBTSerializable<CompoundNBT> {
    GravityDirection getDirection();

    void setDirection(@Nonnull GravityDirection direction);

    void updateTracking();

    GravityStatusPacket createUpdatePacket();

    void sendPlayerUpdatePacket(ServerPlayerEntity serverPlayer);

    // @Nonnull
    // Vector3d getEyePosChangeVector();
    //
    // void setEyePosChangeVector(@Nonnull Vector3d vec3d);
    //
    // GravityDirection getPendingDirection();
    //
    // GravityDirection getPrevDirection();
    //
    // int getTimeoutTicks();
    //
    // int getReverseTimeoutTicks();
    //
    // void setReverseTimeoutTicks(int newReverseTimeout);
    //
    // void setTimeoutTicks(int newTimeout);
    //
    // double getTransitionAngle();
    //
    // void setTransitionAngle(double angle);
    //
    // boolean hasTransitionAngle();
    //
    // boolean timeoutComplete();
    //
    // /**
    //  * Used to set client gravity when logging into a server.
    //  * Possibly also on both sides when a dimension change/respawn occurs
    //  *
    //  * @param direction
    //  */
    // void setDirectionNoTimeout(@Nonnull GravityDirection direction);
    //
    // void setPendingDirection(@Nonnull GravityDirection direction, int priority);
    // void forceSetPendingDirection(@Nonnull GravityDirection direction, int priority);
    //
    // int getPendingPriority();
    //
    // int getPreviousTickPriority();
    //
    // default void tickCommon() {}
    //
    // default void tickServer() {}
    //
    // default void tickClient() {}
}
