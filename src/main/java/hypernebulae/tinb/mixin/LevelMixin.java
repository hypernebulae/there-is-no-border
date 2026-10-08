package hypernebulae.tinb.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Level.class})
public class LevelMixin {
    @Inject(method = {"isOutsideSpawnableHeight"}, at = {@At("RETURN")}, cancellable = true)
    private static void isInvalidVertically(int y, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Inject(method = {"isInWorldBoundsHorizontal"}, at = {@At("RETURN")}, cancellable = true)
    private static void isValidHorizontally(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        cir.cancel();
    }
}
