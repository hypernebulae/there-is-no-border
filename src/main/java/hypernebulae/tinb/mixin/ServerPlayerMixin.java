package hypernebulae.tinb.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
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

        execute(currentWorld);
    }

    private static void execute(ServerLevel world) {
        MinecraftServer server = world.getServer();
        for (ServerLevel level : server.getAllLevels()) {
            level.getWorldBorder().setSize(4294967296.0);
        }
    }
}