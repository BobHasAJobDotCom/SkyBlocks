package com.skyblocks;

import com.skyblocks.block.entity.EndBlockEntity;
import com.skyblocks.block.entity.SkyBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntities {
	private ModBlockEntities() {}

	public static final BlockEntityType<EndBlockEntity> END_BLOCK = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		Identifier.of(SkyBlocks.MOD_ID, "end_block"),
		FabricBlockEntityTypeBuilder.create(EndBlockEntity::new, ModBlocks.END_BLOCK).build()
	);

	public static final BlockEntityType<SkyBlockEntity> SKY_BLOCK = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		Identifier.of(SkyBlocks.MOD_ID, "sky_block"),
		FabricBlockEntityTypeBuilder.create(SkyBlockEntity::new, ModBlocks.SKY_BLOCK).build()
	);

	public static void init() {}
}
