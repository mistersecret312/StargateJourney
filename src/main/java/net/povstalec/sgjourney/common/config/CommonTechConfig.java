package net.povstalec.sgjourney.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonTechConfig
{
	public static ModConfigSpec.BooleanValue disable_kara_kesh_requirements;
	
	public static void init(ModConfigSpec.Builder server)
	{
		disable_kara_kesh_requirements = server
				.comment("If true Kara Kesh won't require its user to have Naquadah in their bloodstream")
				.define("server.disable_kara_kesh_requirements", true);
	}
}
