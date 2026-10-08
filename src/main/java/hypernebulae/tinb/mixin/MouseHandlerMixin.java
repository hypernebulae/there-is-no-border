package hypernebulae.tinb.mixin;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @ModifyConstant(
            method = "onScroll",
            constant = @Constant(floatValue = 0.005F)
    )
    private float increaseSpectatorScrollStep(float original) {
        return 0.02F;
    }

    @ModifyConstant(
            method = "onScroll",
            constant = @Constant(floatValue = 0.2F)
    )
    private float increaseSpectatorMaxSpeed(float original) {
        return 1;
    }
}