package com.skyblocks.block.entity;

import com.skyblocks.ModBlockEntities;
import com.skyblocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SkyBlockEntity extends EndPortalBlockEntity {
	public SkyBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SKY_BLOCK, pos, state);
	}

	@Override
	public boolean shouldDrawSide(Direction direction) {
		World world = this.getWorld();
		if (world == null) {
			return true;
		}
		return !world.getBlockState(this.getPos().offset(direction)).isOf(ModBlocks.SKY_BLOCK);
	}
}
