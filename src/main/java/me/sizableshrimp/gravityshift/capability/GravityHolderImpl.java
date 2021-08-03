package me.sizableshrimp.gravityshift.capability;

import me.sizableshrimp.gravityshift.core.GravityDirection;
import me.sizableshrimp.gravityshift.network.GravityStatusPacket;
import me.sizableshrimp.gravityshift.network.NetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;

public class GravityHolderImpl implements IGravityHolder {
    private final PlayerEntity player;
    private GravityDirection direction = GravityDirection.DOWN;

    public GravityHolderImpl(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public GravityDirection getDirection() {
        return direction;
    }

    @Override
    public void setDirection(@Nonnull GravityDirection direction) {
        this.direction = direction;
        this.updateTracking();
    }

    @Override
    public void updateTracking() {
        if (player.level.isClientSide)
            return;
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), this.createUpdatePacket());
    }

    public GravityStatusPacket createUpdatePacket() {
        return new GravityStatusPacket(player.getId(), this);
    }

    @Override
    public void sendPlayerUpdatePacket(ServerPlayerEntity serverPlayer) {
        NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), this.createUpdatePacket());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("direction", direction.name());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag) {
        this.direction = GravityDirection.valueOf(tag.getString("direction"));
    }
}
