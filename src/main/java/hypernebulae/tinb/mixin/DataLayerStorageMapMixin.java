package hypernebulae.tinb.mixin;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.lighting.DataLayerStorageMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DataLayerStorageMap.class)
public abstract class DataLayerStorageMapMixin {
    @Redirect(
            method = "copyDataLayer(J)Lnet/minecraft/world/level/chunk/DataLayer;",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectOpenHashMap;get(J)Ljava/lang/Object;"
            )
    )
    private Object emptyLayerWhenMissing(
            Long2ObjectOpenHashMap<DataLayer> map, long sectionNode
    ) {
        DataLayer layer = map.get(sectionNode);
        return layer != null ? layer : new DataLayer();
    }
}
