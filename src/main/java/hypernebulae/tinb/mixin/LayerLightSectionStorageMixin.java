package hypernebulae.tinb.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.lighting.DataLayerStorageMap;
import net.minecraft.world.level.lighting.LayerLightSectionStorage;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerLightSectionStorage.class)
public abstract class LayerLightSectionStorageMixin {
    @WrapOperation(
            method = "getStoredLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/DataLayer;get(III)I"
            )
    )
    private int guardMissingLayer(
            DataLayer layer, int x, int y, int z, Operation<Integer> original
    ) {
        if (layer == null) {
            // Fail soft rather than killing the lighting worker.
            return 0;
        }
        return original.call(layer, x, y, z);
    }
    @Shadow
    @Final
    protected DataLayerStorageMap<?> updatingSectionData;

    @Shadow @Final
    protected LongSet changedSections;

    @Shadow
    protected abstract @Nullable DataLayer getDataLayer(long sectionNode, boolean updating);

    @Inject(method = "setStoredLevel(JI)V", at = @At("HEAD"), cancellable = true)
    private void setStoredLevel(long blockNode, int level, CallbackInfo ci) {
        long sectionNode = SectionPos.blockToSection(blockNode);

        DataLayer layer;
        if (this.changedSections.add(sectionNode)) {
            layer = this.updatingSectionData.copyDataLayer(sectionNode);
        } else {
            layer = this.getDataLayer(sectionNode, true);
        }

        if (layer == null) {
            layer = new DataLayer();
            this.updatingSectionData.setLayer(sectionNode, layer);
            this.updatingSectionData.clearCache();
        }

        layer.set(
                SectionPos.sectionRelative(BlockPos.getX(blockNode)),
                SectionPos.sectionRelative(BlockPos.getY(blockNode)),
                SectionPos.sectionRelative(BlockPos.getZ(blockNode)),
                level
        );

        ci.cancel();
    }
}
