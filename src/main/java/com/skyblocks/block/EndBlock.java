package com.skyblocks.block;

import com.mojang.serialization.MapCodec;
import com.skyblocks.block.entity.EndBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class EndBlock extends BlockWithEntity {
	public static final MapCodec<EndBlock> CODEC = createCodec(EndBlock::new);

	public EndBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new EndBlockEntity(pos, state);
	}

	@Override
	protected BlockRenderType getRenderType(BlockState state) {

		return BlockRenderType.INVISIBLE;
	}
}
