package hypernebulae.tinb.mixin;

import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin {
    @Shadow
    private int absoluteMaxSize;

    @Inject(
            method = "setAbsoluteMaxSize(I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void forceAbsoluteMaxSize(int size, CallbackInfo ci) {
        this.absoluteMaxSize = 2147483647;
        ci.cancel();
    }
}
