package hypernebulae.tinb.mixin;

import net.minecraft.world.level.chunk.status.ChunkPyramid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkPyramid.class)
public abstract class ChunkPyramidMixin {
    @Shadow
    @Final
    @Mutable
    public static int MAX_CHUNK_COORDINATE_VALUE;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void removeCoordinateLimit(CallbackInfo ci) {
        MAX_CHUNK_COORDINATE_VALUE = Integer.MAX_VALUE;
    }
}