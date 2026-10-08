package hypernebulae.tinb.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces$MineShaftPiece")
public abstract class MineshaftPieceMixin {
    @Overwrite
    protected boolean isInInvalidLocation(
            LevelAccessor level,
            BoundingBox chunkBB
    ) {
        BoundingBox pieceBB = ((StructurePiece) (Object) this).getBoundingBox();

        int x0 = Math.max(pieceBB.minX() - 1, chunkBB.minX());
        int y0 = Math.max(pieceBB.minY() - 1, chunkBB.minY());
        int z0 = Math.max(pieceBB.minZ() - 1, chunkBB.minZ());

        int x1 = Math.min(pieceBB.maxX() + 1, chunkBB.maxX());
        int y1 = Math.min(pieceBB.maxY() + 1, chunkBB.maxY());
        int z1 = Math.min(pieceBB.maxZ() + 1, chunkBB.maxZ());

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(
                x0 + (x1 - x0) / 2,
                y0 + (y1 - y0) / 2,
                z0 + (z1 - z0) / 2
        );

        if (level.getBiome(blockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
            return true;
        }

        // Check bottom and top surfaces.
        for (int x = x0; x <= x1; ++x) {
            for (int z = z0; z <= z1; ++z) {
                if (level.getBlockState(blockPos.set(x, y0, z)).liquid()) {
                    return true;
                }

                if (level.getBlockState(blockPos.set(x, y1, z)).liquid()) {
                    return true;
                }
            }
        }

        // Check north and south surfaces.
        for (int x = x0; x <= x1; ++x) {
            for (int y = y0; y <= y1; ++y) {
                if (level.getBlockState(blockPos.set(x, y, z0)).liquid()) {
                    return true;
                }

                if (level.getBlockState(blockPos.set(x, y, z1)).liquid()) {
                    return true;
                }
            }
        }

        // Check west and east surfaces.
        for (int z = z0; z <= z1; ++z) {
            for (int y = y0; y <= y1; ++y) {
                if (level.getBlockState(blockPos.set(x0, y, z)).liquid()) {
                    return true;
                }

                if (level.getBlockState(blockPos.set(x1, y, z)).liquid()) {
                    return true;
                }
            }
        }

        return false;
    }
}