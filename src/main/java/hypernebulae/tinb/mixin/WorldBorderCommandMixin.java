package hypernebulae.tinb.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.WorldBorderCommand;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Locale;

@Mixin({WorldBorderCommand.class})
public class WorldBorderCommandMixin {
    @Inject(method = {"setSize"}, at = {@At(value = "INVOKE", target = "Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;create()Lcom/mojang/brigadier/exceptions/CommandSyntaxException;", remap = false)}, cancellable = true)
    private static void injectCreateException(CommandSourceStack source, double distance, long time, CallbackInfoReturnable<Integer> cir) {
        WorldBorder worldBorder = source.getLevel().getWorldBorder();
        worldBorder.setSize(distance);
        source.sendSuccess(() -> Component.translatable("commands.worldborder.set.immediate", String.format(Locale.ROOT, "%.1f", distance)), true);
        cir.cancel();
        cir.setReturnValue((int) (distance - worldBorder.getSize()));
    }

    @ModifyArgs(method = {"register"}, at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/arguments/DoubleArgumentType;doubleArg(DD)Lcom/mojang/brigadier/arguments/DoubleArgumentType;", ordinal = 0, remap = false))
    private static void modifyDoubleArgLimits(Args args) {
        args.set(0, Double.NEGATIVE_INFINITY);
        args.set(1, Double.POSITIVE_INFINITY);
    }

    @ModifyArgs(method = {"register"}, at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/arguments/DoubleArgumentType;doubleArg(DD)Lcom/mojang/brigadier/arguments/DoubleArgumentType;", ordinal = 1, remap = false))
    private static void modifyDoubleArgLimitsOther(Args args) {
        args.set(0, Double.NEGATIVE_INFINITY);
        args.set(1, Double.POSITIVE_INFINITY);
    }
}