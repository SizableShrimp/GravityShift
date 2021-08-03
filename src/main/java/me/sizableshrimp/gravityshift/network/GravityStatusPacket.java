package me.sizableshrimp.gravityshift.network;

import me.sizableshrimp.gravityshift.capability.IGravityHolder;
import me.sizableshrimp.gravityshift.client.network.ClientPacketHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GravityStatusPacket {
    public final int entityId;
    public final CompoundNBT tag;

    public GravityStatusPacket(int entityId, CompoundNBT tag) {
        this.entityId = entityId;
        this.tag = tag;
    }

    public GravityStatusPacket(int entityId, IGravityHolder holder) {
        this(entityId, holder.serializeNBT());
    }

    public void write(PacketBuffer buf) {
        buf.writeInt(entityId);
        buf.writeNbt(tag);
    }

    public static GravityStatusPacket read(PacketBuffer buf) {
        return new GravityStatusPacket(buf.readInt(), buf.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientPacketHandler.handleGravityStatus(this));
        ctx.get().setPacketHandled(true);
    }
}
