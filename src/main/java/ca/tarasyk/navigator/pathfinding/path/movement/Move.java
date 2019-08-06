package ca.tarasyk.navigator.pathfinding.path.movement;

import ca.tarasyk.navigator.BetterBlockPos;
import ca.tarasyk.navigator.pathfinding.Heuristic;
import ca.tarasyk.navigator.pathfinding.path.node.PathNode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Move {

    MOVE_POS_X(1, 0, 0), MOVE_NEG_X(-1, 0, 0),
    MOVE_POS_Z(0, 0, 1), MOVE_NEG_Z(0, 0, -1),
    MOVE_POSPOS_XZ(1, 0, 1), MOVE_NEGPOS_XZ(-1, 0, 1),
    MOVE_NEGNEG_XZ(-1, 0, -1), MOVE_POSNEG_XZ(1, 0, -1),
    MOVE_DOWN_POS_X(1, -1, 0), MOVE_DOWN_NEG_X(-1, -1, 0),
    MOVE_DOWN_POS_Z(0, -1, 1), MOVE_DOWN_NEG_Z(0, -1, -1),
    MOVE_DOWN_POS_XZ(1, -1, 1), MOVE_DOWN_NEGPOS_XZ(-1, -1, 1),
    MOVE_DOWN_NEGNEG_XZ(-1, -1, -1), MOVE_DOWN_POSNEG_XZ(1, -1, -1),
    MOVE_UP_POS_X(1, 1, 0), MOVE_UP_NEG_X(-1, 1, 0),
    MOVE_UP_POS_Z(0, 1, 1), MOVE_UP_NEG_Z(0, 1, -1),
    MOVE_UP_POS_XZ(1, 1, 1), MOVE_UP_NEGPOS_XZ(-1, 1, 1),
    MOVE_UP_NEGNEG_XZ(-1, 1, -1), MOVE_UP_POSNEG_XZ(1, 1, -1),
    MOVE_POS_Y(0, 1, 0), MOVE_NEG_Y(0, -1, 0);

    private int dx, dy, dz;

    public static final ArrayList<Move> moves = new ArrayList<>(Arrays.asList(Move.values()));

    Move(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public PathNode apply(PathNode prev) {
        BetterBlockPos pos = prev.getPos();
        return new PathNode(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
    }

    public static boolean isSolid(WorldClient ctx, BlockPos pos) {
        return ctx.getBlockState(pos).getBlock() != Blocks.AIR;
    }

    public static Optional<Double> calculateWeight(WorldClient ctx, PathNode src, PathNode dest) {
        boolean NOT_CLIMBING_OR_ASCENDING = dest.getPos().getY() - src.getPos().getY() == 0;

        System.out.println(dest + "," + ctx.getBlockState(dest.getPos().down()).getBlock() + " ," + ctx.getBlockState(dest.getPos()).getBlock());
        if (!isSolid(ctx, dest.getPos().down()) || isSolid(ctx, dest.getPos()) || isSolid(ctx, src.getPos())) {
            // Nothing to stand on, or unloaded chunk, dest block is solid (can't jump on it), or we have to dig to get to the block
            return Optional.ofNullable(null);
        }

        /*if (NOT_CLIMBING_OR_ASCENDING) {
            // A block is blocking the way
            if (ctx.getBlockState(src.getPos().up()).isBlockNormalCube() || ctx.getBlockState(dest.getPos().up()).isBlockNormalCube()) {
                return Optional.ofNullable(null);
            }
        }*/

        // It simply costs the movement cost in ticks to move there
        return Optional.of(Heuristic.BLOCKNODE_EUCLIDEAN_DISTANCE.apply(src, dest));
    }

    public static List<PathNode> neighborsOf(PathNode node) {
        ArrayList<PathNode> neighbors = new ArrayList<>();
        for (Move move : Move.moves) {
            neighbors.add(move.apply(node));
        }
        return neighbors;
    }

}
