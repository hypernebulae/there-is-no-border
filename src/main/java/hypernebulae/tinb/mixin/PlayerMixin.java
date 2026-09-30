package hypernebulae.tinb.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyConstant(
            method = "tick",
            constant = @Constant(doubleValue = 2.9999999E7D)
    )
    private double modifyPositiveWorldLimit(double original) {
        return Double.POSITIVE_INFINITY;
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(doubleValue = -2.9999999E7D)
    )
    private double modifyNegativeWorldLimit(double original) {
        return Double.NEGATIVE_INFINITY;
    }
}