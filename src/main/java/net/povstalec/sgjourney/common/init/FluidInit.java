package net.povstalec.sgjourney.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;

import java.util.function.Supplier;

public class FluidInit
{
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, StargateJourney.MODID);
	
	
	public static final Supplier<FlowingFluid> LIQUID_NAQUADAH_SOURCE = FLUIDS.register("liquid_naquadah", 
			() -> new BaseFlowingFluid.Source(FluidTypeInit.LIQUID_NAQUADAH_PROPERTIES));
	public static final Supplier<FlowingFluid> LIQUID_NAQUADAH_FLOWING = FLUIDS.register("flowing_liquid_naquadah", 
			() -> new BaseFlowingFluid.Flowing(FluidTypeInit.LIQUID_NAQUADAH_PROPERTIES));

	public static final Supplier<FlowingFluid> HEAVY_LIQUID_NAQUADAH_SOURCE = FLUIDS.register("heavy_liquid_naquadah", 
			() -> new BaseFlowingFluid.Source(FluidTypeInit.HEAVY_LIQUID_NAQUADAH_PROPERTIES));
	public static final Supplier<FlowingFluid> HEAVY_LIQUID_NAQUADAH_FLOWING = FLUIDS.register("flowing_heavy_liquid_naquadah", 
			() -> new BaseFlowingFluid.Flowing(FluidTypeInit.HEAVY_LIQUID_NAQUADAH_PROPERTIES));
	
	
	
	public static void register(IEventBus eventBus)
	{
		FLUIDS.register(eventBus);
	}
}
