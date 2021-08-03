package me.sizableshrimp.gravityshift.core;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

public class DirectionAwareBlockPos extends BlockPos {
    private final GravityDirection gravityDirection;

    public DirectionAwareBlockPos(GravityDirection gravityDirection, int x, int y, int z) {
        super(x, y, z);
        this.gravityDirection = gravityDirection;
    }

    public DirectionAwareBlockPos(GravityDirection gravityDirection, double x, double y, double z) {
        super(x, y, z);
        this.gravityDirection = gravityDirection;
    }

    public DirectionAwareBlockPos(GravityDirection gravityDirection, Vector3d vec) {
        super(vec);
        this.gravityDirection = gravityDirection;
    }

    public DirectionAwareBlockPos(GravityDirection gravityDirection, Vector3i source) {
        super(source);
        this.gravityDirection = gravityDirection;
    }

    public DirectionAwareBlockPos(GravityDirection gravityDirection, BlockPos other) {
        super(other.getX(), other.getY(), other.getZ());
        this.gravityDirection = gravityDirection;
    }

    @Override
    public BlockPos offset(double x, double y, double z) {
        Vector3d d = gravityDirection.adjustXYZValues(x, y, z);
        return super.offset(d.x(), d.y(), d.z());
    }

    @Override
    public BlockPos offset(int x, int y, int z) {
        return offset((double) x, y, z);
    }

    @Override
    public BlockPos offset(Vector3i vec) {
        return offset(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public BlockPos subtract(Vector3i vec) {
        return offset(-vec.getX(), -vec.getY(), -vec.getZ());
    }
}
