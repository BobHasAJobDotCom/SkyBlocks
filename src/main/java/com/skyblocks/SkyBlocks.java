package com.skyblocks;

import net.fabricmc.api.ModInitializer;

public class SkyBlocks implements ModInitializer {
	public static final String MOD_ID = "skyblocks";

	@Override
	public void onInitialize() {

		ModBlocks.init();
		ModBlockEntities.init();
		ModItemGroups.init();
	}
}
