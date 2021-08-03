package me.sizableshrimp.gravityshift.network;

import me.sizableshrimp.gravityshift.GravityShiftMod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            GravityShiftMod.getModLocation("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        INSTANCE.messageBuilder(GravityStatusPacket.class, getNextId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(GravityStatusPacket::write)
                .decoder(GravityStatusPacket::read)
                .consumer(GravityStatusPacket::handle)
                .add();
    }

    private static int getNextId() {
        return nextId++;
    }
}
