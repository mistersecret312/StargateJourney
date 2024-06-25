package net.povstalec.sgjourney.common.misc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Pretty much just a copy paste of the vanilla Jigsaw placement, but with rotation set to none
 * @author Povstalec
 *
 */
public class SGJourneyJigsawPlacement extends JigsawPlacement
{
	static final Logger LOGGER = LogUtils.getLogger();

	private static Optional<BlockPos> getRandomNamedJigsaw(
			StructurePoolElement pElement,
			ResourceLocation pStartJigsawName,
			BlockPos pPos,
			Rotation pRotation,
			StructureTemplateManager pStructureTemplateManager,
			WorldgenRandom pRandom
	) {
		List<StructureTemplate.StructureBlockInfo> list = pElement.getShuffledJigsawBlocks(pStructureTemplateManager, pPos, pRotation, pRandom);
		Optional<BlockPos> optional = Optional.empty();

		for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : list) {
			ResourceLocation resourcelocation = ResourceLocation.tryParse(
					Objects.requireNonNull(structuretemplate$structureblockinfo.nbt(), () -> structuretemplate$structureblockinfo + " nbt was null")
							.getString("name")
			);
			if (pStartJigsawName.equals(resourcelocation)) {
				optional = Optional.of(structuretemplate$structureblockinfo.pos());
				break;
			}
		}

		return optional;
	}
}
