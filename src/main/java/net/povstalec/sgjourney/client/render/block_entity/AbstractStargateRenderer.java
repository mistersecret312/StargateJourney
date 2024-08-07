package net.povstalec.sgjourney.client.render.block_entity;

import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.povstalec.sgjourney.client.models.AbstractStargateModel;
import net.povstalec.sgjourney.client.models.IrisModel;
import net.povstalec.sgjourney.client.models.ShieldModel;
import net.povstalec.sgjourney.client.models.WormholeModel;
import net.povstalec.sgjourney.common.block_entities.stargate.AbstractStargateEntity;
import net.povstalec.sgjourney.common.blockstates.Orientation;
import net.povstalec.sgjourney.common.blockstates.StargatePart;
import net.povstalec.sgjourney.common.config.ClientStargateConfig;
import net.povstalec.sgjourney.common.stargate.StargateVariant;

public abstract class AbstractStargateRenderer
{
	protected final WormholeModel wormholeModel;
	protected final ShieldModel shieldModel;
	protected final IrisModel irisModel;
	
	private final RandomSource randomsource = RandomSource.create();
	
	public AbstractStargateRenderer(BlockEntityRendererProvider.Context context,
			ResourceLocation eventHorizonTexture, ResourceLocation shinyEventHorizonTexture, float maxDefaultDistortion,
			boolean renderWhenOpen, float maxOpenIrisDegrees)
	{
		this.shieldModel = new ShieldModel();
		this.irisModel = new IrisModel(renderWhenOpen, maxOpenIrisDegrees);
		this.wormholeModel = new WormholeModel(eventHorizonTexture, Optional.of(shinyEventHorizonTexture), maxDefaultDistortion);
	}
	
	public AbstractStargateRenderer(BlockEntityRendererProvider.Context context,
			ResourceLocation eventHorizonTexture, float maxDefaultDistortion,
			boolean renderWhenOpen, float maxOpenIrisDegrees)
	{
		this.shieldModel = new ShieldModel();
		this.irisModel = new IrisModel(renderWhenOpen, maxOpenIrisDegrees);
		this.wormholeModel = new WormholeModel(eventHorizonTexture, Optional.empty(), maxDefaultDistortion);
	}
	
	protected void renderWormhole(AbstractStargateEntity stargate, PoseStack stack, MultiBufferSource source, @SuppressWarnings("rawtypes") @Nullable AbstractStargateModel model, int combinedLight, int combinedOverlay)
	{
		Optional<ResourceLocation> eventHorizonTexture = Optional.empty();
		int frames = 32;
		boolean hasVortex = ClientStargateConfig.enable_vortex.get();
		
		if(model != null)
		{
			Optional<StargateVariant> variantOptional = AbstractStargateModel.getVariant(stargate);

			if(variantOptional.isPresent())
			{
				StargateVariant variant = variantOptional.get();
				if(model.canUseVariant(variant))
				{
					if(ClientStargateConfig.shiny_event_horizons.get() && variant.getShinyEventHorizonTexture().isPresent())
						eventHorizonTexture = variant.getShinyEventHorizonTexture();
					else
						eventHorizonTexture = Optional.of(variant.getEventHorizonTexture());
					
					frames = variant.getEventHorizonFrames();
					if(frames <= 0)
						frames = 1;
					
					Optional<Boolean> variantHasVortex = variant.hasVortex();
					if(variantHasVortex.isPresent())
						hasVortex = variantHasVortex.get();
				}
			}
		}
		
		if(stargate.isConnected())
	    	this.wormholeModel.renderWormhole(stargate, stack, source, eventHorizonTexture, frames, combinedLight, combinedOverlay, hasVortex);
	}
	
	protected void renderCover(AbstractStargateEntity stargate, PoseStack stack, MultiBufferSource source, int combinedLight, int combinedOverlay)
	{
	    for(Map.Entry<StargatePart, BlockState> entry : stargate.blockCover.blockStates.entrySet())
	    {
	    	renderCoverBlock(stargate, entry.getValue(), entry.getKey(), stack, source, combinedOverlay);
	    }
	}
	
	protected void renderCoverBlock(AbstractStargateEntity stargate, BlockState state, StargatePart part, PoseStack stack, MultiBufferSource source, int combinedOverlay)
	{
		Level level = stargate.getLevel();
		Direction direction = stargate.getDirection();
		Orientation orientation = stargate.getOrientation();
		
		if(direction != null && orientation != null)
		{
			Vec3 relativeBlockPos = part.getRelativeRingPos(stargate.getBlockPos(), direction, orientation);
			BlockPos absolutePos = part.getRingPos(stargate.getBlockPos(), stargate.getDirection(), stargate.getOrientation());
			
			stack.pushPose();
			
			stack.translate(relativeBlockPos.x(), relativeBlockPos.y(), relativeBlockPos.z());
			BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();//Minecraft.getInstance().getBlockColors().
			//dispatcher.renderSingleBlock(state, stack, source, LevelRenderer.getLightColor(level, absolutePos), combinedOverlay, ModelData.EMPTY, null);
			
			
			BakedModel model = dispatcher.getBlockModel(state);
			for(RenderType renderType : model.getRenderTypes(state, randomsource, ModelData.EMPTY))
			{
				dispatcher.renderBatched(state, absolutePos, level, stack, source.getBuffer(renderType), true, randomsource, model.getModelData(level, absolutePos, state, ModelData.EMPTY), null);
			}
			
			stack.popPose();
		}
	}
	
	protected boolean canSink(AbstractStargateEntity stargate)
	{
	    return stargate.blockCover.canSinkGate;
	}
}
