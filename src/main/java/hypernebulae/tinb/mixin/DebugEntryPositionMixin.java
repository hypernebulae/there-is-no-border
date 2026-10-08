package hypernebulae.tinb.mixin;

import hypernebulae.tinb.Main;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugEntryPosition;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Mixin(DebugEntryPosition.class)
public abstract class DebugEntryPositionMixin {
    private char getColorPrecision(double precision) {
        if (precision <= 0.03125) {
            return 'a';
        }
        return precision > 0.25 ? 'c' : 'e';
    }

    private char getXZColor(double coord) {
        double absolute = Math.abs(coord);
        if (absolute < 10000) {
            return 'f';
        } else if (absolute < 1E6) {
            return 'b';
        } else if (absolute < 3E7) {
            return 'a';
        } else if (absolute < 3.2E7) {
            return 'e';
        } else if (absolute < 33554432) {
            return '6';
        } else {
            return 'c';
        }
    }

    private char getYColor(double coord) {
        if (coord > -64 && coord < 320) {
            return 'f';
        } else {
            double absolute = Math.abs(coord);
            if (absolute < 2048) {
                return 'b';
            } else if (absolute < 2E7) {
                return 'a';
            } else if (absolute < 3.2E7) {
                return 'e';
            } else if (absolute < 2147483647) {
                return '6';
            } else {
                return 'c';
            }
        }
    }

    private String format(double value, String format) {
        if (Math.abs(value) >= 1E10 || Double.isNaN(value)) {
            return String.valueOf(value);
        }
        return new DecimalFormat(format).format(value);
    }

    @Overwrite
    public void display(
            DebugScreenDisplayer displayer,
            @Nullable Level level,
            @Nullable LevelChunk chunk1,
            @Nullable LevelChunk chunk2
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.getCameraEntity();

        if (entity == null) {
            return;
        }

        BlockPos blockPos = entity.blockPosition();
        ChunkPos chunkPos = new ChunkPos(
                SectionPos.blockToSectionCoord(blockPos.getX()),
                SectionPos.blockToSectionCoord(blockPos.getZ())
        );
        Direction dir = entity.getDirection();

        String direction = switch (dir) {
            case EAST -> "§feast§r";
            case WEST -> "§fwest§r";
            case SOUTH -> "§fsouth§r";
            case NORTH -> "§fnorth§r";
            default -> "§fInvalid§r";
        };

        String dirAxis = switch (dir) {
            case EAST -> "Towards §fpositive X§r";
            case WEST -> "Towards §fnegative X§r";
            case SOUTH -> "Towards §fpositive Z§r";
            case NORTH -> "Towards §fnegative Z§r";
            default -> "§fInvalid§r";
        };

        double maxPosition = Math.max(
                Math.abs(entity.getX()),
                Math.max(Math.abs(entity.getY()), Math.abs(entity.getZ()))
        );

        int exponent = Math.getExponent(maxPosition);

        double doublePrecision = Math.scalb(1.0, exponent - 52);
        float floatPrecision = (float) Math.scalb(1.0, exponent - 23);

        LongSet forcedChunks = level instanceof ServerLevel serverLevel
                ? serverLevel.getForceLoadedChunks()
                : LongSets.EMPTY_SET;

        float rotY = Mth.wrapDegrees(entity.getYRot());
        float rotX = Mth.wrapDegrees(entity.getXRot());

        displayer.addToGroup(
                DebugEntryPosition.GROUP,
                List.of(
                        "§cX§r: §" + getXZColor(entity.getX()) + format(entity.getX(), Main.precision),
                        "§aY§r: §" + getYColor(entity.getY()) + format(entity.getY(), Main.precision),
                        "§bZ§r: §" + getXZColor(entity.getZ()) + format(entity.getZ(), Main.precision),

                        "Current precision: §"
                                + getColorPrecision(doublePrecision)
                                + doublePrecision
                                + "§r (§ffloat§r: §"
                                + getColorPrecision(floatPrecision)
                                + floatPrecision
                                + "§r)",
                        "",

                        String.format(
                                Locale.ROOT,
                                "Block: §c%d §a%d §b%d",
                                blockPos.getX(),
                                blockPos.getY(),
                                blockPos.getZ()
                        ),

                        String.format(
                                Locale.ROOT,
                                "Chunk: §c%d §a%d §b%d §r[§c%d §b%d §rin §fr.§c%d§f.§b%d§f.mca§r]",
                                chunkPos.x(),
                                SectionPos.blockToSectionCoord(blockPos.getY()),
                                chunkPos.z(),
                                chunkPos.getRegionLocalX(),
                                chunkPos.getRegionLocalZ(),
                                chunkPos.getRegionX(),
                                chunkPos.getRegionZ()
                        ),

                        String.format(
                                Locale.ROOT,
                                "Facing: %s (%s) (§f%.1f§r / §f%.1f§r)",
                                direction,
                                dirAxis,
                                rotY,
                                rotX
                        ),

                        minecraft.level.dimension().identifier() + " FC: " + forcedChunks.size()
                )
        );
    }
}