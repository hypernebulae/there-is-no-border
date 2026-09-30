package hypernebulae.tinb.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LevelReader.class)
public interface LevelReaderMixin {
    @Overwrite
    default int getMaxLocalRawBrightness(BlockPos pos, int amount) {
        return ((LevelReader) this).getRawBrightness(pos, amount);
    }
}