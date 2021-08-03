package me.sizableshrimp.gravityshift.core;

import me.sizableshrimp.gravityshift.capability.IGravityHolder;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class GravityAxisAlignedBB extends AxisAlignedBB {
    private final IGravityHolder gravityHolder;

    public GravityAxisAlignedBB(IGravityHolder gravityHolder, double x1, double y1, double z1, double x2, double y2, double z2) {
        super(x1, y1, z1, x2, y2, z2);
        this.gravityHolder = gravityHolder;
    }

    public GravityAxisAlignedBB(GravityAxisAlignedBB other, double x1, double y1, double z1, double x2, double y2, double z2) {
        this(other.gravityHolder, x1, y1, z1, x2, y2, z2);
    }

    public GravityAxisAlignedBB(IGravityHolder gravityHolder, BlockPos pos1, BlockPos pos2) {
        super(pos1, pos2);
        this.gravityHolder = gravityHolder;
    }

    public GravityAxisAlignedBB(IGravityHolder gravityHolder, BlockPos pos) {
        super(pos);
        this.gravityHolder = gravityHolder;
    }

    public GravityAxisAlignedBB(IGravityHolder gravityHolder, Vector3d vec1, Vector3d vec2) {
        super(vec1, vec2);
        this.gravityHolder = gravityHolder;
    }

    public GravityAxisAlignedBB(IGravityHolder gravityHolder, AxisAlignedBB bb) {
        this(gravityHolder, bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public GravityAxisAlignedBB(GravityAxisAlignedBB other, AxisAlignedBB bb) {
        this(other.gravityHolder, bb);
    }

    public static double getRelativeBottom(AxisAlignedBB bb) {
        if (bb instanceof GravityAxisAlignedBB) {
            return ((GravityAxisAlignedBB) bb).getRelativeBottom();
        }
        return bb.minY;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public double getRelativeBottom() {
        switch (this.getDirection()) {
            case UP:
                return -this.maxY;
            case DOWN:
                return this.minY;
            case SOUTH:
                return -this.maxZ;
            case WEST:
                return this.minX;
            case NORTH:
                return this.minZ;
            // case EAST:
            default:
                return -this.maxX;
        }
    }

    public GravityDirection getDirection() {
        return this.gravityHolder.getDirection();
    }

    @Override
    public GravityAxisAlignedBB expandTowards(double x, double y, double z) {
        Vector3d vec = this.gravityHolder.getDirection().adjustXYZValues(x, y, z);
        // DO NOT CHANGE THIS TO TAKE A SINGLE VEC - IT WILL STACK OVERFLOW
        return new GravityAxisAlignedBB(this, super.expandTowards(vec.x(), vec.y(), vec.z()));
    }

    @Override
    public GravityAxisAlignedBB inflate(double x, double y, double z) {
        // Sign of the arguments is important, we only want to get the new directions the values correspond to
        Vector3d vec = this.getDirection().adjustXYZValuesMaintainSigns(x, y, z);
        return new GravityAxisAlignedBB(this, super.inflate(vec.x(), vec.y(), vec.z()));
    }

    @Override
    public GravityAxisAlignedBB move(BlockPos pos) {
        return new GravityAxisAlignedBB(this, this.move(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public GravityAxisAlignedBB move(double x, double y, double z) {
        Vector3d vec = this.getDirection().adjustXYZValues(x, y, z);
        // DO NOT CHANGE THIS TO TAKE A SINGLE VEC - IT WILL STACK OVERFLOW
        return new GravityAxisAlignedBB(this, super.move(vec.x(), vec.y(), vec.z()));
    }

    public GravityAxisAlignedBB moveSuper(double x, double y, double z) {
        return new GravityAxisAlignedBB(this, super.move(x, y, z));
    }

    public GravityAxisAlignedBB expandUp(double y) {
        switch (this.gravityHolder.getDirection()) {
            case UP:
                return new GravityAxisAlignedBB(this, this.minX, this.minY - y, this.minZ, this.maxX, this.maxY, this.maxZ);
            case SOUTH:
                return new GravityAxisAlignedBB(this, this.minX, this.minY, this.minZ - y, this.maxX, this.maxY, this.maxZ);
            case WEST:
                return new GravityAxisAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX + y, this.maxY, this.maxZ);
            case NORTH:
                return new GravityAxisAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ + y);
            case EAST:
                return new GravityAxisAlignedBB(this, this.minX - y, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
            default://case DOWN:
                return new GravityAxisAlignedBB(this, this.minX, this.minY, this.minZ, this.maxX, this.maxY + y, this.maxZ);
        }
    }

    public IGravityHolder getGravityHolder() {
        return this.gravityHolder;
    }

    public Vector3d getOrigin() {
        switch (this.gravityHolder.getDirection()) {
            case UP:
                return new Vector3d(this.getXCenter(), this.maxY, this.getZCenter());
            case SOUTH:
                return new Vector3d(this.getXCenter(), this.getYCenter(), this.maxZ);
            case WEST:
                return new Vector3d(this.minX, this.getYCenter(), this.getZCenter());
            case NORTH:
                return new Vector3d(this.getXCenter(), this.getYCenter(), this.minZ);
            case EAST:
                return new Vector3d(this.maxX, this.getYCenter(), this.getZCenter());
            default://case DOWN:
                return new Vector3d(this.getXCenter(), this.minY, this.getZCenter());
        }
    }

    private double getXCenter() {
        return (this.minX + this.maxX) / 2d;
    }

    private double getYCenter() {
        return (this.minY + this.maxY) / 2d;
    }

    private double getZCenter() {
        return (this.minZ + this.maxZ) / 2d;
    }

    public GravityAxisAlignedBB offsetSuper(double x, double y, double z) {
        return new GravityAxisAlignedBB(this, super.move(x, y, z));
    }

    public AxisAlignedBB toVanilla() {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
}
