package com.skyblocks;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ModItemGroups {
	private ModItemGroups() {}

	public static final RegistryKey<ItemGroup> MAIN_KEY =
		RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(SkyBlocks.MOD_ID, "main"));

	public static void init() {
		Registry.register(Registries.ITEM_GROUP, MAIN_KEY, FabricItemGroup.builder()
			.icon(() -> new ItemStack(ModBlocks.SKY_BLOCK))
			.displayName(Text.translatable("itemGroup.skyblocks.main"))
			.entries((context, entries) -> {
				entries.add(ModBlocks.SKY_BLOCK);
				entries.add(ModBlocks.END_BLOCK);
			})
			.build());
	}
}
