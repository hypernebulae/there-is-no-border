package hypernebulae.tinb.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugEntrySectionPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Locale;

@Mixin(DebugEntrySectionPosition.class)
public abstract class DebugEntrySectionPositionMixin {
    @Overwrite
    public void display(final DebugScreenDisplayer displayer, final @Nullable Level serverOrClientLevel, final @Nullable LevelChunk clientChunk, final @Nullable LevelChunk serverChunk) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity != null) {
            BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
            displayer.addToGroup(DebugEntryPosition.GROUP, String.format(Locale.ROOT, "Section-relative: §c%02d §a%02d §b%02d", feetPos.getX() & 15, feetPos.getY() & 15, feetPos.getZ() & 15));
        }
    }
}