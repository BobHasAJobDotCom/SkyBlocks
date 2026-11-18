package com.skyblocks.client;

import com.skyblocks.block.entity.EndBlockEntity;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.EndPortalBlockEntityRenderState;

public class EndBlockEntityRenderer extends AbstractEndPortalBlockEntityRenderer<EndBlockEntity, EndPortalBlockEntityRenderState> {
	@Override
	public EndPortalBlockEntityRenderState createRenderState() {
		return new EndPortalBlockEntityRenderState();
	}

	protected float getTopYOffset() {
		return 1.0F;
	}

	protected float getBottomYOffset() {
		return 0.0F;
	}
}
