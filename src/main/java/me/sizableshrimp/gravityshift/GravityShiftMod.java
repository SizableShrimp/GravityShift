package me.sizableshrimp.gravityshift;

import me.sizableshrimp.gravityshift.capability.GravityHolderCapability;
import me.sizableshrimp.gravityshift.network.NetworkHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GravityShiftMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GravityShiftMod {
    public static final String MODID = "gravityshift";
    public static final Logger LOGGER = LogManager.getLogger();

    public GravityShiftMod() {}

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        GravityHolderCapability.register();
        NetworkHandler.register();
    }

    public static ResourceLocation getModLocation(String name) {
        return new ResourceLocation(MODID, name);
    }
}
