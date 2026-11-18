package com.skyblocks;

import com.skyblocks.block.EndBlock;
import com.skyblocks.block.SkyBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class ModBlocks {
	private ModBlocks() {}

	
	private static final float UNBREAKABLE_HARDNESS = -1.0F;
	private static final float UNBREAKABLE_RESISTANCE = 3600000.0F;

	public static final Block SKY_BLOCK = register(
		"sky_block",
		SkyBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.LIGHT_BLUE)
			.strength(UNBREAKABLE_HARDNESS, UNBREAKABLE_RESISTANCE)
			.dropsNothing()
			.nonOpaque()
			.sounds(BlockSoundGroup.GLASS)
			.pistonBehavior(PistonBehavior.BLOCK)
			.allowsSpawning((state, world, pos, type) -> false)
	);

	public static final Block END_BLOCK = register(
		"end_block",
		EndBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.BLACK)
			.strength(UNBREAKABLE_HARDNESS, UNBREAKABLE_RESISTANCE)
			.dropsNothing()
			.nonOpaque()
			.luminance(state -> 15)
			.pistonBehavior(PistonBehavior.BLOCK)
			.noBlockBreakParticles()
			.allowsSpawning((state, world, pos, type) -> false)
	);

	private static Block register(String name, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
		Identifier id = Identifier.of(SkyBlocks.MOD_ID, name);

		RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
		Block block = factory.apply(settings.registryKey(blockKey));
		Registry.register(Registries.BLOCK, blockKey, block);

		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
		BlockItem item = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
		Registry.register(Registries.ITEM, itemKey, item);

		return block;
	}

	
	public static void init() {}
}
