package hypernebulae.tinb.mixin;

import hypernebulae.tinb.CommandScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Inject(
            method = "keyPress(JILnet/minecraft/client/input/KeyEvent;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tinb$openScreen(
            long handle,
            int action,
            KeyEvent event,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.keycode() == 92
                && action == 1
                && minecraft.player != null
                && minecraft.gui.screen() == null) {
            minecraft.gui.setScreen(new CommandScreen());
            ci.cancel();
        }
    }
}
