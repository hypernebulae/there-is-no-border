package hypernebulae.tinb.mixin;

import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntitySectionStorage.class)
public abstract class EntitySectionStorageMixin {
    @Redirect(
            method = "getChunkSections",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/longs/LongSortedSet;subSet(JJ)Lit/unimi/dsi/fastutil/longs/LongSortedSet;"
            )
    )
    private LongSortedSet fixChunkSectionsRange(
            LongSortedSet sections,
            long lowerBound,
            long upperBound
    ) {
        if (upperBound == Long.MIN_VALUE) {
            return sections.tailSet(lowerBound);
        }

        return sections.subSet(lowerBound, upperBound);
    }

    @Redirect(
            method = "forEachAccessibleNonEmptySection",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/longs/LongSortedSet;subSet(JJ)Lit/unimi/dsi/fastutil/longs/LongSortedSet;"
            )
    )
    private LongSortedSet fixEntitySearchRange(
            LongSortedSet sections,
            long lowerBound,
            long upperBound
    ) {
        if (upperBound == Long.MIN_VALUE) {
            return sections.tailSet(lowerBound);
        }

        return sections.subSet(lowerBound, upperBound);
    }
}