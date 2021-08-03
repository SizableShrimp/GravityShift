package me.sizableshrimp.gravityshift.capability;

import me.sizableshrimp.gravityshift.GravityShiftMod;
import me.sizableshrimp.gravityshift.core.GravityDirection;
import me.sizableshrimp.gravityshift.core.IGravityAwarePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GravityShiftMod.MODID)
public abstract class GravityHolderCapability extends CapabilityAttacher {
    @CapabilityInject(IGravityHolder.class)
    public static final Capability<IGravityHolder> GRAVITY_HOLDER_CAPABILITY = null;
    private static final ResourceLocation GRAVITY_HOLDER_RL = GravityShiftMod.getModLocation("gravity_holder");

    public static LazyOptional<IGravityHolder> getGravityHolder(PlayerEntity player) {
        return player.getCapability(GRAVITY_HOLDER_CAPABILITY);
    }

    public static GravityDirection getGravityDirection(PlayerEntity player) {
        return getGravityHolder(player).map(IGravityHolder::getDirection).orElse(null);
    }

    public static GravityDirection getGravityDirection(IGravityAwarePlayer player) {
        return getGravityDirection(player.toVanilla());
    }

    @SubscribeEvent
    public static void onAttachGravityHolderCap(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getObject();
            genericAttachCapability(event, new GravityHolderImpl(player), GravityHolderCapability.GRAVITY_HOLDER_CAPABILITY, GRAVITY_HOLDER_RL);
        }
    }

    public static void register() {
        CapabilityAttacher.registerCapability(IGravityHolder.class);
    }
}
