package hypernebulae.tinb.mixin;

import net.minecraft.server.dedicated.DedicatedServerProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedServerProperties.class)
public abstract class DedicatedServerPropertiesMixin {
    @Shadow
    @Final
    @Mutable
    private int maxWorldSize;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void forceMaxWorldSize(CallbackInfo ci) {
        this.maxWorldSize = 2147483647;
    }
}