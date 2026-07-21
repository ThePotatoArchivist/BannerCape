package archives.tater.bannercape.client;

import archives.tater.bannercape.client.render.BannerCapeRenderLayer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import java.util.Optional;

public class BannerCapeClient implements ClientModInitializer {
	public record BannerCapeRenderState(DyeColor baseColor, BannerPatternLayers patterns) {}

	public static final RenderStateDataKey<Optional<BannerCapeRenderState>> BANNER_CAPE = RenderStateDataKey.create();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof AvatarRenderer<?> avatarRenderer)
				registrationHelper.register(new BannerCapeRenderLayer(avatarRenderer, context.getModelSet(), context.getEquipmentAssets(), context.getSprites()));
		});
	}
}