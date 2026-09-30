package hypernebulae.tinb.mixin;

import net.minecraft.server.commands.ForceLoadCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ForceLoadCommand.class)
public class ForceLoadCommandMixin {
    @ModifyConstant(
            method = "changeForceLoad",
            constant = @Constant(intValue = -30_000_000)
    )
    private static int replaceMinWorldBorder(int value) {
        return Integer.MIN_VALUE;
    }

    @ModifyConstant(
            method = "changeForceLoad",
            constant = @Constant(intValue = 30_000_000)
    )
    private static int replaceMaxWorldBorder(int value) {
        return Integer.MAX_VALUE;
    }
}