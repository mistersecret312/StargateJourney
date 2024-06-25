package net.povstalec.sgjourney.common.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.povstalec.sgjourney.StargateJourney;
import net.povstalec.sgjourney.common.entities.PlasmaProjectile;

import java.util.function.Supplier;

public class EntityInit 
{
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, StargateJourney.MODID);
	
	// Projectiles
	public static final Supplier<EntityType<PlasmaProjectile>> JAFFA_PLASMA = ENTITIES.register("jaffa_plasma",
					() -> EntityType.Builder.<PlasmaProjectile>of(PlasmaProjectile::new, MobCategory.MISC)
						.sized(0.25F, 0.25F)
						.build(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "jaffa_plasma").toString()));

	
	// Creatures
	/*public static final Supplier<EntityType<Goauld>> GOAULD = ENTITIES.register("goauld",
					() -> EntityType.Builder.<Goauld>of(Goauld::new, MobCategory.CREATURE)
						.sized(0.4F, 0.4F)
						.build(ResourceLocation.fromNamespaceAndPath(StargateJourney.MODID, "goauld").toString()));*/
	
	public static void register(IEventBus eventBus)
	{
		ENTITIES.register(eventBus);
	}
}
