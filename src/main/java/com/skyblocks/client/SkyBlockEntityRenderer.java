package com.skyblocks.client;

import com.skyblocks.block.entity.SkyBlockEntity;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.EndPortalBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class SkyBlockEntityRenderer extends AbstractEndPortalBlockEntityRenderer<SkyBlockEntity, EndPortalBlockEntityRenderState> {
	@Override
	public EndPortalBlockEntityRenderState createRenderState() {
		return new EndPortalBlockEntityRenderState();
	}

	@Override
	public void updateRenderState(SkyBlockEntity blockEntity, EndPortalBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, ModelCommandRenderer.CrumblingOverlayCommand crumbling) {
		super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumbling);
		SkyBlockTracker.add(blockEntity.getPos(), state.sides);
	}

	@Override
	public void render(EndPortalBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
	}

	@Override
	public int getRenderDistance() {
		return 256;
	}
}
