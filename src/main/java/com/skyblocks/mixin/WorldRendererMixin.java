package com.skyblocks.mixin;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.skyblocks.client.SkyBlockTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.DefaultFramebufferSet;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.FramePass;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Shadow @Final private MinecraftClient client;
	@Shadow @Final private BufferBuilderStorage bufferBuilders;
	@Shadow @Final private DefaultFramebufferSet framebufferSet;

	@Shadow
	protected abstract PostEffectProcessor getTransparencyPostEffectProcessor();

	@Shadow
	protected abstract void renderClouds(FrameGraphBuilder frameGraphBuilder, CloudRenderMode mode, Vec3d cameraPos, long time, float tickProgress, int color, float height);

	@Unique private boolean skyblocks$cloudsHandled;
	@Unique private boolean skyblocks$ownCloudCall;

	
	@Inject(method = "renderMain", at = @At("HEAD"))
	private void skyblocks$beforeMain(FrameGraphBuilder frameGraphBuilder, Frustum frustum, Matrix4f posMatrix, GpuBufferSlice fogBuffer,
			boolean renderBlockOutline, WorldRenderState state, RenderTickCounter tickCounter, Profiler profiler, CallbackInfo ci) {
		this.skyblocks$cloudsHandled = false;
		final List<SkyBlockTracker.Entry> entries = SkyBlockTracker.drain();
		if (entries.isEmpty()) {
			return;
		}
		final Vec3d cam = state.cameraRenderState.pos;
		float tickProgress = tickCounter.getTickProgress(false);

		if (this.getTransparencyPostEffectProcessor() == null) {
			CloudRenderMode mode = this.client.options.getCloudRenderModeValue();
			if (mode != CloudRenderMode.OFF) {
				Camera camera = this.client.gameRenderer.getCamera();
				int color = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.CLOUD_COLOR_VISUAL, tickProgress);
				if (ColorHelper.getAlpha(color) > 0) {
					float height = camera.getEnvironmentAttributeInterpolator().get(EnvironmentAttributes.CLOUD_HEIGHT_VISUAL, tickProgress);

					FramePass fogPass = frameGraphBuilder.createPass("skyblocks_fog");
					this.framebufferSet.mainFramebuffer = fogPass.transfer(this.framebufferSet.mainFramebuffer);
					fogPass.setRenderer(() -> RenderSystem.setShaderFog(fogBuffer));

					this.skyblocks$ownCloudCall = true;
					this.renderClouds(frameGraphBuilder, mode, cam, state.time, tickProgress, color, height);
					this.skyblocks$ownCloudCall = false;
					this.skyblocks$cloudsHandled = true;
				}
			}
		}

		FramePass maskPass = frameGraphBuilder.createPass("skyblocks_mask");
		this.framebufferSet.mainFramebuffer = maskPass.transfer(this.framebufferSet.mainFramebuffer);
		maskPass.setRenderer(() -> {
			VertexConsumerProvider.Immediate immediate = this.bufferBuilders.getEntityVertexConsumers();
			VertexConsumer consumer = immediate.getBuffer(RenderLayers.waterMask());
			for (SkyBlockTracker.Entry entry : entries) {
				for (Direction side : entry.sides()) {
					skyblocks$face(consumer, entry.pos(), side, cam);
				}
			}
			immediate.draw();
		});
	}

	
	@Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
	private void skyblocks$skipLateClouds(FrameGraphBuilder frameGraphBuilder, CloudRenderMode mode, Vec3d cameraPos, long time, float tickProgress, int color, float height, CallbackInfo ci) {
		if (this.skyblocks$cloudsHandled && !this.skyblocks$ownCloudCall) {
			ci.cancel();
		}
	}

	
	@Unique
	private static double skyblocks$edge(float v, int normal, float inset) {
		return normal != 0 ? v - normal * inset : v + (v * 2.0F - 1.0F) * inset;
	}

	@Unique
	private static void skyblocks$face(VertexConsumer c, BlockPos pos, Direction side, Vec3d cam) {
		float[] q = switch (side) {
			case DOWN -> new float[]{0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1};
			case UP -> new float[]{0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0};
			case NORTH -> new float[]{1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0};
			case SOUTH -> new float[]{0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1};
			case WEST -> new float[]{0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1};
			case EAST -> new float[]{1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0};
		};

		final float inset = 0.003F;
		float[] w = new float[12];
		for (int i = 0; i < 4; i++) {
			w[i * 3] = (float) (pos.getX() + skyblocks$edge(q[i * 3], side.getOffsetX(), inset) - cam.x);
			w[i * 3 + 1] = (float) (pos.getY() + skyblocks$edge(q[i * 3 + 1], side.getOffsetY(), inset) - cam.y);
			w[i * 3 + 2] = (float) (pos.getZ() + skyblocks$edge(q[i * 3 + 2], side.getOffsetZ(), inset) - cam.z);
		}

		for (int i = 0; i < 4; i++) {
			c.vertex(w[i * 3], w[i * 3 + 1], w[i * 3 + 2]);
		}
		for (int i = 3; i >= 0; i--) {
			c.vertex(w[i * 3], w[i * 3 + 1], w[i * 3 + 2]);
		}
	}
}
