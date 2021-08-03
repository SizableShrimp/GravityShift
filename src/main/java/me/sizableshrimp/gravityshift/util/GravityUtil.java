package me.sizableshrimp.gravityshift.util;

import me.sizableshrimp.gravityshift.capability.GravityHolderCapability;
import me.sizableshrimp.gravityshift.capability.IGravityHolder;
import me.sizableshrimp.gravityshift.core.IGravityAwarePlayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class GravityUtil {
    public static float getStandardEyeHeight(LivingEntity entity) {
        return entity instanceof PlayerEntity
                ? IGravityAwarePlayer.get((PlayerEntity) entity).getStandardEyeHeight()
                : entity.getEyeHeight();
    }

    public static AxisAlignedBB getGravityAdjustedHitbox(PlayerEntity player) {
        return GravityHolderCapability.getGravityDirection(player).getGravityAdjustedAABB(player);
    }

    public static AxisAlignedBB getGravityAdjustedHitbox(IGravityAwarePlayer player) {
        return getGravityAdjustedHitbox(player.toVanilla());
    }

    public static IGravityHolder getGravityHolder(PlayerEntity player) {
        return GravityHolderCapability.getGravityHolder(player).orElse(null);
    }

    public static IGravityHolder getGravityHolder(IGravityAwarePlayer player) {
        return getGravityHolder(player.toVanilla());
    }
}
