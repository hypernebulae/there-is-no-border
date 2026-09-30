package hypernebulae.tinb.mixin;

import hypernebulae.tinb.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Unique
    private ResourceKey<Level> lastDimension;

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void checkDimensionChange(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        if (!(player.level() instanceof ServerLevel currentWorld)) {
            return;
        }

        ResourceKey<Level> currentDimension = currentWorld.dimension();

        if (lastDimension == currentDimension) {
            return;
        }

        lastDimension = currentDimension;

        Commands.execute(currentWorld);
    }
}