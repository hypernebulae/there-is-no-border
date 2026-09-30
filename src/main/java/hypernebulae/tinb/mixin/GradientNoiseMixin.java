package hypernebulae.tinb.mixin;

import hypernebulae.tinb.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.levelgen.synth.GradientNoise")
public class GradientNoiseMixin {

    @Inject(
            method = "wrap(D)D",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void farlands$modifyWrap(double value, CallbackInfoReturnable<Double> callback) {
        if (Main.farlands) {
            callback.setReturnValue(value);
        }
    }
}