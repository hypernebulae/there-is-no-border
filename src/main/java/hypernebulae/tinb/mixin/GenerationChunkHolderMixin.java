package hypernebulae.tinb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.GenerationChunkHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GenerationChunkHolder.class)
public class GenerationChunkHolderMixin {
    @ModifyExpressionValue(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/ChunkPos;isValid()Z"
            )
    )
    private boolean allowOutOfBoundsChunks(boolean isValid) {
        return true;
    }
}