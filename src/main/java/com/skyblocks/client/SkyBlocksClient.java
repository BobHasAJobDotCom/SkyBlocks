package com.skyblocks.client;

import com.skyblocks.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class SkyBlocksClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(ModBlockEntities.END_BLOCK, context -> new EndBlockEntityRenderer());
		BlockEntityRendererFactories.register(ModBlockEntities.SKY_BLOCK, context -> new SkyBlockEntityRenderer());
	}
}
