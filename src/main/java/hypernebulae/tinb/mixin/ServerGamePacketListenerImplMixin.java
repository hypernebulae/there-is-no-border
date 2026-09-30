package hypernebulae.tinb.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Overwrite
    private static double clampHorizontal(double value) {
        return value;
    }

    @Overwrite
    private static double clampVertical(double value) {
        return value;
    }
}
