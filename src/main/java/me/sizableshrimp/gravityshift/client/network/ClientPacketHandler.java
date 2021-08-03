package me.sizableshrimp.gravityshift.client.network;

import me.sizableshrimp.gravityshift.network.GravityStatusPacket;
import me.sizableshrimp.gravityshift.util.GravityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class ClientPacketHandler {
    public static void handleGravityStatus(GravityStatusPacket packet) {
        Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
        if (entity == null)
            return;
        if (!(entity instanceof PlayerEntity))
            throw new IllegalStateException("Entity is not a player");

        GravityUtil.getGravityHolder(((PlayerEntity) entity)).deserializeNBT(packet.tag);
    }
}
