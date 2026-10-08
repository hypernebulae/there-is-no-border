package hypernebulae.tinb.mixin;

import hypernebulae.tinb.Commands;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(
            method = "sendChat(Ljava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tinb$handleCommand(String message, CallbackInfo ci) {
        if (Commands.handleInput(message)) {
            ci.cancel();
        }
    }
}