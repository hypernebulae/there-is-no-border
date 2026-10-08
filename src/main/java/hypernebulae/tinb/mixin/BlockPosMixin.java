package hypernebulae.tinb.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin {
    @Overwrite
    public Vec3 clampLocationWithin(final Vec3 location) {
        BlockPos self = (BlockPos) (Object) this;

        double x = clampAxis(location.x, self.getX());
        double y = clampAxis(location.y, self.getY());
        double z = clampAxis(location.z, self.getZ());

        return new Vec3(x, y, z);
    }

    private static double clampAxis(double location, int blockCoordinate) {
        double lower = (double) blockCoordinate + 1.0E-5D;
        double upper = (double) blockCoordinate + 1.0D - 1.0E-5D;

        if (lower > upper) {
            return (double) blockCoordinate + 0.5D;
        }

        return Math.clamp(location, lower, upper);
    }
}