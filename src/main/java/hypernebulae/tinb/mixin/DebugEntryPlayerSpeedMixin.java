package hypernebulae.tinb.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPlayerSpeed;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Locale;

@Mixin(DebugEntryPlayerSpeed.class)
public class DebugEntryPlayerSpeedMixin {
    @Overwrite
    public void display(
            final DebugScreenDisplayer displayer,
            final @Nullable Level serverOrClientLevel,
            final @Nullable LevelChunk clientChunk,
            final @Nullable LevelChunk serverChunk
    ) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getCameraEntity() != null) {
            String text = String.format(
                    Locale.ROOT,
                    "§rSpeed: §a%.3f §fblocks/s",
                    minecraft.getCameraEntity().getKnownSpeed().length() * 20.0
            );

            if (minecraft.player != null && minecraft.player.isSpectator()) {
                String speed = String.format(
                        java.util.Locale.ROOT,
                        " §r(§fspec speed§r: §a%.2f§r)",
                        minecraft.player.getAbilities().getFlyingSpeed()
                );
                text += speed;
            }

            displayer.addToGroup(DebugEntryPosition.GROUP, text);
        }
    }

}