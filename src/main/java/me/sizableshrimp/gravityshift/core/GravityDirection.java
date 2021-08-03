package me.sizableshrimp.gravityshift.core;

import me.sizableshrimp.gravityshift.util.GravityUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

/**
 * Enum of the gravity direction objects. Created by Mysteryem on 2016-08-14.
 */
public enum GravityDirection implements IStringSerializable {
    UP(new Vector3i(0, 0, 180), "up", Direction.UP) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(-x, -y, z);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - widthOver2, player.getY() - height, player.getZ() - widthOver2,
                    player.getX() + widthOver2, player.getY(), player.getZ() + widthOver2
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY() - (player.getBbHeight() / 2), player.getZ());
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ());
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return this;
        }

        @Override
        public GravityDirection getOpposite() {
            return DOWN;
        }

        @Override
        public float getEntityEyeHeight(LivingEntity entity) {
            return -GravityUtil.getStandardEyeHeight(entity);
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos((bb.minX + bb.maxX) / 2.0D,
                    bb.maxY,
                    (bb.minZ + bb.maxZ) / 2.0D);
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos pos) {
            return new DirectionAwareBlockPos(this, pos) {
                @Override
                public BlockPos below(int n) {
                    return super.above(n);
                }

                @Override
                public BlockPos above(int n) {
                    return super.below(n);
                }

                @Override
                public BlockPos east(int n) {
                    return super.west(n);
                }

                @Override
                public BlockPos west(int n) {
                    return super.east(n);
                }
            };
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return WEST;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return SOUTH;
        }
    },
    DOWN(new Vector3i(0, 0, 0), "down", Direction.DOWN) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(x, y, z);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - widthOver2, player.getY(), player.getZ() - widthOver2,
                    player.getX() + widthOver2, player.getY() + height, player.getZ() + widthOver2
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ());
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY() - (player.getBbHeight() / 2), player.getZ());
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return this;
        }

        @Override
        public GravityDirection getOpposite() {
            return UP;
        }

        @Override
        public float getEntityEyeHeight(LivingEntity entity) {
            return GravityUtil.getStandardEyeHeight(entity);
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos((bb.minX + bb.maxX) / 2.0D,
                    bb.minY,
                    (bb.minZ + bb.maxZ) / 2.0);
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos pos) {
            return pos;
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return EAST;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return SOUTH;
        }
    },
    NORTH(new Vector3i(90, 0, 0), "north", Direction.NORTH) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(x, -z, y);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            float eyeHeight = GravityUtil.getStandardEyeHeight(player);
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - widthOver2, player.getY() - widthOver2, player.getZ() - eyeHeight,
                    player.getX() + widthOver2, player.getY() + widthOver2, player.getZ() + (height - eyeHeight)
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY(), player.getZ() - (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)));
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY(), player.getZ() + (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)));
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return SOUTH;
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos((bb.minX + bb.maxX) / 2.0D,
                    (bb.minY + bb.maxY) / 2.0,
                    bb.minZ + GravityUtil.getStandardEyeHeight(entity));
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos blockPos) {
            return new DirectionAwareBlockPos(this, blockPos) {
                @Override
                public BlockPos below(int n) {
                    return super.north(n);
                }

                @Override
                public BlockPos above(int n) {
                    return super.south(n);
                }

                @Override
                public BlockPos north(int n) {
                    return super.above(n);
                }

                @Override
                public BlockPos south(int n) {
                    return super.below(n);
                }
            };
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return EAST;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return DOWN;
        }
    },
    EAST(new Vector3i(0, 0, 90), "east", Direction.EAST) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(-y, x, z);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            float eyeHeight = GravityUtil.getStandardEyeHeight(player);
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - (height - eyeHeight), player.getY() - widthOver2, player.getZ() - widthOver2,
                    player.getX() + eyeHeight, player.getY() + widthOver2, player.getZ() + widthOver2
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX() + (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)), player.getY(), player.getZ());
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX() - (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)), player.getY(), player.getZ());
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return WEST;
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos(bb.maxX - GravityUtil.getStandardEyeHeight(entity),
                    (bb.minY + bb.maxY) / 2.0D,
                    (bb.minZ + bb.maxZ) / 2.0D);
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos pos) {
            return new DirectionAwareBlockPos(this, pos) {
                @Override
                public BlockPos below(int n) {
                    return super.east(n);
                }

                @Override
                public BlockPos above(int n) {
                    return super.west(n);
                }

                @Override
                public BlockPos west(int n) {
                    return super.below(n);
                }

                @Override
                public BlockPos east(int n) {
                    return super.above(n);
                }
            };
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return UP;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return SOUTH;
        }
    },
    SOUTH(new Vector3i(-90, 0, 0), "south", Direction.SOUTH) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(x, z, -y);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            float eyeHeight = GravityUtil.getStandardEyeHeight(player);
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - widthOver2, player.getY() - widthOver2, player.getZ() - (height - eyeHeight),
                    player.getX() + widthOver2, player.getY() + widthOver2, player.getZ() + eyeHeight
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY(), player.getZ() + (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)));
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX(), player.getY(), player.getZ() - (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)));
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return NORTH;
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos((bb.minX + bb.maxX) / 2.0D,
                    (bb.minY + bb.maxY) / 2.0,
                    bb.maxZ - GravityUtil.getStandardEyeHeight(entity));
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos pos) {
            return new DirectionAwareBlockPos(this, pos) {
                @Override
                public BlockPos below(int n) {
                    return super.south(n);
                }

                @Override
                public BlockPos above(int n) {
                    return super.north(n);
                }

                @Override
                public BlockPos north(int n) {
                    return super.below(n);
                }

                @Override
                public BlockPos south(int n) {
                    return super.above(n);
                }
            };
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return EAST;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return UP;
        }
    },
    WEST(new Vector3i(0, 0, -90), "west", Direction.WEST) {
        @Override
        public Vector3d adjustXYZValues(double x, double y, double z) {
            return new Vector3d(y, -x, z);
        }

        @Override
        public GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height) {
            double widthOver2 = width / 2f;
            float eyeHeight = GravityUtil.getStandardEyeHeight(player);
            return new GravityAxisAlignedBB(
                    GravityUtil.getGravityHolder(player),
                    player.getX() - eyeHeight, player.getY() - widthOver2, player.getZ() - widthOver2,
                    player.getX() + (height - eyeHeight), player.getY() + widthOver2, player.getZ() + widthOver2
            );
        }

        @Override
        public void returnCenterOfGravityToPlayerPos(PlayerEntity player) {
            player.setPos(player.getX() - (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)), player.getY(), player.getZ());
        }

        @Override
        public void offsetCenterOfGravityFromPlayerPos(PlayerEntity player) {
            player.setPos(player.getX() + (GravityUtil.getStandardEyeHeight(player) - (player.getBbHeight() / 2)), player.getY(), player.getZ());
        }

        @Override
        public GravityDirection getInverseAdjustmentFromDOWNDirection() {
            return EAST;
        }

        @Override
        public void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb) {
            entity.setPos(bb.minX + GravityUtil.getStandardEyeHeight(entity),
                    (bb.minY + bb.maxY) / 2.0,
                    (bb.minZ + bb.maxZ) / 2.0);
        }

        @Override
        public BlockPos makeRelativeBlockPos(BlockPos pos) {
            return new DirectionAwareBlockPos(this, pos) {
                @Override
                public BlockPos below(int n) {
                    return super.west(n);
                }

                @Override
                public BlockPos above(int n) {
                    return super.east(n);
                }

                @Override
                public BlockPos east(int n) {
                    return super.below(n);
                }

                @Override
                public BlockPos west(int n) {
                    return super.above(n);
                }
            };
        }

        @Override
        public GravityDirection getRelativePositiveX() {
            return DOWN;
        }

        @Override
        public GravityDirection getRelativePositiveZ() {
            return SOUTH;
        }
    };

    private final Vector3i cameraTransformVars;
    private final Direction facingEquivalent;
    private final String name;

    GravityDirection(Vector3i cameraTransformVars, String name, Direction facingEquivalent) {
        this.cameraTransformVars = cameraTransformVars;
        this.name = name;
        this.facingEquivalent = facingEquivalent;
    }

    public static GravityDirection fromDirection(Direction Direction) {
        switch (Direction) {
            case DOWN:
                return DOWN;
            case UP:
                return UP;
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            default://case EAST:
                return EAST;
        }
    }

    public abstract BlockPos makeRelativeBlockPos(BlockPos blockPos);

    public Vector3d adjustXYZValuesMaintainSigns(double x, double y, double z) {
        Vector3d values = this.adjustXYZValues(x, y, z);
        Vector3d signs = this.adjustXYZValues(1, 1, 1);
        return new Vector3d(values.x * signs.x, values.y * signs.y, values.z * signs.z);
    }

    public Vector3d adjustXYZValues(Vector3d vec) {
        return adjustXYZValues(vec.x(), vec.y(), vec.z());
    }

    public abstract Vector3d adjustXYZValues(double x, double y, double z);

    // Overridden in UP and DOWN
    public float getEntityEyeHeight(LivingEntity entity) {
        return 0F;
    }

    public Direction getFacingEquivalent() {
        return this.facingEquivalent;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public GravityDirection getRelativeDown() {
        return this.getRelativeNegativeY();
    }

    public GravityDirection getRelativeNegativeY() {
        return this;
    }

    public GravityDirection getRelativeEast() {
        return this.getRelativePositiveX();
    }

    public abstract GravityDirection getRelativePositiveX();

    public GravityDirection getRelativeNorth() {
        return this.getRelativeNegativeZ();
    }

    public GravityDirection getRelativeNegativeZ() {
        return this.getRelativePositiveZ().getOpposite();
    }

    /**
     * Get the opposite gravity direction to this. For all but UP and DOWN, it is the same as this.getInverseAdjustmentFromDOWNDirection()
     *
     * @return The opposite direction to this.
     */
    public GravityDirection getOpposite() {
        return this.getInverseAdjustmentFromDOWNDirection();
    }

    public abstract GravityDirection getRelativePositiveZ();

    public abstract GravityDirection getInverseAdjustmentFromDOWNDirection();

    public GravityDirection getRelativeSouth() {
        return this.getRelativePositiveZ();
    }

    public GravityDirection getRelativeUp() {
        return this.getRelativePositiveY();
    }

    public GravityDirection getRelativePositiveY() {
        return this.getOpposite();
    }

    public GravityDirection getRelativeWest() {
        return this.getRelativeNegativeX();
    }

    public GravityDirection getRelativeNegativeX() {
        return this.getRelativePositiveX().getOpposite();
    }

    public boolean isValidLadderDirection(Direction ladderFacing) {
        return ladderFacing != this.facingEquivalent && ladderFacing.getOpposite() != this.facingEquivalent;
    }

    public void postModifyPlayerOnGravityChange(PlayerEntity player, GravityDirection oldDirection, Vector3d inputEyePos) {

        // Moves the player's position to their centre of gravity
        oldDirection.returnCenterOfGravityToPlayerPos(player);
        // Moves the player's position to the new place for this gravity direction, such that the player's centre of
        // gravity is in the same place as when postModifyPlayerOnGravityChange was called
        this.offsetCenterOfGravityFromPlayerPos(player);

        // Move the player to try and get them out of a wall. Being inside of a block for even a single tick causes
        // suffocation damage
        this.setBoundingBoxAndPositionOnGravityChange(player, oldDirection, inputEyePos);
    }

    public abstract void returnCenterOfGravityToPlayerPos(PlayerEntity player);

    public abstract void offsetCenterOfGravityFromPlayerPos(PlayerEntity player);

    private void setBoundingBoxAndPositionOnGravityChange(PlayerEntity player, GravityDirection oldDirection, Vector3d oldEyePos) {
        AxisAlignedBB bb = this.getGravityAdjustedAABB(player);
        player.setLocationFromBoundingbox();

        if (!player.level.noCollision(bb)) {
            // After rotating about the player's centre of gravity, the player is now partially inside of a block

            // TODO: Test being a 'spider' player and trying to change gravity direction in tight places
            // TODO: Re-add choosing the correct gravity direction and using that to inverseAdjust the movement values, so that 'spider' players don't get
            // stuck in walls
            // Instead of trying to move the player in all 6 directions to see which would, we can eliminate all but 2
            GravityDirection directionToTry;
            double distanceToMove;

            // The player has a relatively normal hitbox
            if (player.getBbHeight() > player.getBbWidth()) {
                // Collision will be happening either above or below the player's centre of gravity from the
                // NEW gravity direction's perspective
                distanceToMove = (player.getBbHeight() - player.getBbWidth()) / 2;
                directionToTry = this;
            }
            // The player must be some sort of pancake, e.g. a spider
            else if (player.getBbHeight() < player.getBbWidth()) {
                // Collision will be happening either above or below the player's centre of gravity from the
                // OLD gravity direction's perspective
                distanceToMove = (player.getBbWidth() - player.getBbHeight()) / 2;
                directionToTry = oldDirection;
            }
            // The player is a cube, meaning that their collision/bounding box won't have actually changed shape after being rotated/moved
            else {
                // As rotation of the player occurs about their centre of gravity
                // This scenario means that the player was already inside a block when they rotated as this should be impossible otherwise

                // Not going to do anything in this case
                player.setBoundingBox(bb);
                return;
            }

            // Get the movement that is considered 'up' by distanceToMove
            Vector3d adjustedMovement = directionToTry.adjustXYZValues(0, distanceToMove, 0);

            // Inverse to undo the adjustment caused by GravityAxisAlignedBB.offset(...)
            adjustedMovement = this.getInverseAdjustmentFromDOWNDirection().adjustXYZValues(adjustedMovement);

            // Moving 'up' from the rotated player's perspective
            AxisAlignedBB secondTry = bb.move(adjustedMovement);


            // We try 'up' first because even if we move the player too far, their gravity will move them back 'down'
            if (!player.level.noCollision(secondTry)) {

                // Moving 'down' from the rotated player's perspective
                AxisAlignedBB thirdTry = bb.move(adjustedMovement.multiply(-1, -1, -1));

                if (!player.level.noCollision(thirdTry)) {
                    // Uh oh, looks like the player decided to rotate in a too small place
                    // Imagine a 2 block tall, 1 block wide player standing in a 2 block tall, one block wide space
                    // and then changing from UP/DOWN gravity to NORTH/EAST/SOUTH/WEST gravity
                    // they cannot possibly fit, we'll settle for putting the bottom of their bb at an integer value
                    // as well as trying one block up (relative)

                    // Move the player such that their old eye position is the same as the new one, this should limit suffocation
                    player.setBoundingBox(bb);
                    player.setLocationFromBoundingbox();

                    Vector3d newEyePos = player.position().add(0, player.getEyeHeight(), 0);
                    Vector3d eyesDifference = oldEyePos.subtract(newEyePos);
                    Vector3d adjustedDifference = this.getInverseAdjustmentFromDOWNDirection().adjustXYZValues(eyesDifference.x, eyesDifference.y, eyesDifference.z);
                    AxisAlignedBB givenUp = bb.move(adjustedDifference.x, adjustedDifference.y, adjustedDifference.z);
                    //TODO: Set position at feet to closest int so we don't fall through blocks
                    double relativeBottomOfBB = GravityAxisAlignedBB.getRelativeBottom(givenUp);
                    long rounded = Math.round(relativeBottomOfBB);
                    double difference = rounded - relativeBottomOfBB;
                    //Try one block up (relative) from the found position to start with, to try and avoid falling through the block that is now at our feet
                    givenUp = givenUp.move(0, difference + 1, 0);
                    //If one block up collided, then we have no choice but to choose the block below
                    if (!player.level.noCollision(givenUp)) {
                        givenUp = givenUp.move(0, -1, 0);
                    }
                    bb = givenUp;

                } else {
                    // Moving 'down' did not collide with the world
                    bb = thirdTry;

                }
            } else {
                // Moving 'up' did not collide with the world
                bb = secondTry;
            }
        }
        player.setBoundingBox(bb);
        player.setLocationFromBoundingbox();
    }

    public AxisAlignedBB getGravityAdjustedAABB(PlayerEntity player) {
        return this.getGravityAdjustedAABB(player, player.getBbWidth(), player.getBbHeight());
    }

    public abstract GravityAxisAlignedBB getGravityAdjustedAABB(PlayerEntity player, float width, float height);

    public void resetPositionToBB(LivingEntity entity) {
        this.resetPositionToBB(entity, entity.getBoundingBox());
    }

    public abstract void resetPositionToBB(LivingEntity entity, AxisAlignedBB bb);

    public Vector3i getCameraTransformVars() {
        return this.cameraTransformVars;
    }
}
