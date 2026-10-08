package hypernebulae.tinb.mixin;

import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ChunkPos.class)
public class ChunkPosMixin {
    @ModifyConstant(
            method = "<clinit>",
            constant = @Constant(intValue = 1875066)
    )
    private static int replaceInvalidChunkPosValue(int value) {
        return 2147483647;
    }

    @Overwrite
    public static boolean isValid(int x, int z) {
        return true;
    }
}