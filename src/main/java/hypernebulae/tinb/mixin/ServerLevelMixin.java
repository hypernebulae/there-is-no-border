package hypernebulae.tinb.mixin;

import java.util.function.BooleanSupplier;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Unique
    private boolean commandExecuted;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void runWorldBorderCommand(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (commandExecuted) {
            return;
        }

        commandExecuted = true;
        ServerLevel world = (ServerLevel) (Object) this;
        MinecraftServer server = world.getServer();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withLevel(world).withSuppressedOutput(), "wbmax");
    }
}