package net.povstalec.sgjourney.common.init;

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;

import java.util.function.Supplier;

//A class that registers Villager professions and their points of interest
public class VillagerInit
{
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, StargateJourney.MODID);
	
	public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, StargateJourney.MODID);
	
	/*public static void registerPOIs()
	{
		try
		{
			ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class).invoke(null, ARCHEOLOGIST_POI.get());
		}
		catch(InvocationTargetException | IllegalAccessException exception)
		{
			exception.printStackTrace();
		}
	}*/
	
	public static void register(IEventBus eventBus)
	{
		POI_TYPES.register(eventBus);
		VILLAGER_PROFESSIONS.register(eventBus);
	}
}
