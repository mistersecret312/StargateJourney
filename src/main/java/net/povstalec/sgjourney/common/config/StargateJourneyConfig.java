package net.povstalec.sgjourney.common.config;

import java.io.File;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.povstalec.sgjourney.StargateJourney;

public class StargateJourneyConfig
{
	private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec COMMON_CONFIG;
	
	private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec CLIENT_CONFIG;
	
	public static SGJourneyConfigValue.BooleanValue disable_energy_use;
	public static SGJourneyConfigValue.BooleanValue disable_smooth_animations;
	
	static
	{
		COMMON_BUILDER.push("Stargate Journey Common Config");
		
		generalServerConfig(COMMON_BUILDER);
		
		COMMON_BUILDER.push("ZPM Config");
		CommonZPMConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Interface Config");
		CommonInterfaceConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Stargate Config");
		CommonStargateConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Naquadah Generator Config");
		CommonNaquadahGeneratorConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Stargate Network Config");
		CommonStargateNetworkConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Generation Config");
		CommonGenerationConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Tech Config");
		CommonTechConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.push("Genetic Config");
		CommonGeneticConfig.init(COMMON_BUILDER);
		COMMON_BUILDER.pop();
		
		COMMON_BUILDER.pop();
		COMMON_CONFIG = COMMON_BUILDER.build();
		
		

		CLIENT_BUILDER.push("Stargate Journey Client Config");
		
		generalClientConfig(CLIENT_BUILDER);
		
		CLIENT_BUILDER.push("Stargate Config");
		ClientStargateConfig.init(CLIENT_BUILDER);
		CLIENT_BUILDER.pop();
		
		CLIENT_BUILDER.push("DHD Config");
		ClientDHDConfig.init(CLIENT_BUILDER);
		CLIENT_BUILDER.pop();
		
		CLIENT_BUILDER.push("Sky Config");
		ClientSkyConfig.init(CLIENT_BUILDER);
		CLIENT_BUILDER.pop();
		
		CLIENT_BUILDER.pop();
		CLIENT_CONFIG = CLIENT_BUILDER.build();
	}
	
	public static void loadConfig(ModConfigSpec config, String path)
	{
		StargateJourney.LOGGER.info("Loading Config: " + path);
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		StargateJourney.LOGGER.info("Built config: " + path);
		file.load();
		StargateJourney.LOGGER.info("Loaded Config: " + path);
		config.setConfig(file);
	}
	
	private static void generalServerConfig(ModConfigSpec.Builder server)
	{
		server.comment("Stargate Journey General Config");
		
		disable_energy_use = new SGJourneyConfigValue.BooleanValue(server, "server.disable_energy_requirements", 
				true, 
				"Disable energy requirements for blocks added by Stargate Journey");
	}
	
	private static void generalClientConfig(ModConfigSpec.Builder client)
	{
		client.comment("Stargate Journey Client Config");
		
		disable_smooth_animations = new SGJourneyConfigValue.BooleanValue(client, "client.disable_smooth_animations", 
				false, 
				"Disables smooth animations of Stargate Journey Block Entities");
	}
}
