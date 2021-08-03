package me.sizableshrimp.gravityshift.event;

import com.mojang.brigadier.CommandDispatcher;
import me.sizableshrimp.gravityshift.GravityShiftMod;
import me.sizableshrimp.gravityshift.core.GravityDirection;
import me.sizableshrimp.gravityshift.util.GravityUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;

@Mod.EventBusSubscriber(modid = GravityShiftMod.MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) event.getEntity();
            GravityUtil.getGravityHolder(serverPlayer).sendPlayerUpdatePacket(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof PlayerEntity) {
            PlayerEntity playerToTrack = (PlayerEntity) event.getTarget();
            GravityUtil.getGravityHolder(playerToTrack).sendPlayerUpdatePacket((ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("setdirection")
                .then(Commands.argument("direction", EnumArgument.enumArgument(GravityDirection.class))
                        .executes(cs -> {
                            ServerPlayerEntity player = cs.getSource().getPlayerOrException();
                            GravityUtil.getGravityHolder(player).setDirection(cs.getArgument("direction", GravityDirection.class));
                            player.refreshDimensions();
                            return 1;
                        })));
    }
}
