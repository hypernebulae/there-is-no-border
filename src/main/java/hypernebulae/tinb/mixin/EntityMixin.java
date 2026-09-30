package hypernebulae.tinb.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Redirect(
            method = "absSnapTo(DDD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;clamp(DDD)D"
            )
    )
    private double modifySnapLimit(
            double value,
            double min,
            double max
    ) {
        return value;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = 3.0000512E7D)
    )
    private double expandHorizontalPositiveLimit(double original) {
        return Double.POSITIVE_INFINITY;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = -3.0000512E7D)
    )
    private double expandHorizontalNegativeLimit(double original) {
        return Double.NEGATIVE_INFINITY;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = 2.0E7D)
    )
    private double expandVerticalPositiveLimit(double original) {
        return Double.POSITIVE_INFINITY;
    }

    @ModifyConstant(
            method = "load",
            constant = @Constant(doubleValue = -2.0E7D)
    )
    private double expandVerticalNegativeLimit(double original) {
        return Double.NEGATIVE_INFINITY;
    }
}