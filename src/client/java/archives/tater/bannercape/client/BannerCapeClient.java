package archives.tater.bannercape.client;

import archives.tater.bannercape.BannerCape;
import archives.tater.bannercape.client.render.BannerCapeRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import eu.pb4.trinkets.api.client.TrinketRendererRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BannerCapeClient implements ClientModInitializer {
	public record BannerCapeRenderState(DyeColor baseColor, BannerPatternLayers patterns) {}

	public static final RenderStateDataKey<Optional<BannerCapeRenderState>> BANNER_CAPE = RenderStateDataKey.create();

	public static final Identifier CAPE_PATTERN_ATLAS = BannerCape.id("cape_patterns");
	public static final Identifier CAPE_PATTERN_SHEET = AtlasRegistry.generateTextureLocation(CAPE_PATTERN_ATLAS);
	private static final Map<Identifier, SpriteId> CAPE_SPRITES = new HashMap<>();
	public static final SpriteMapper CAPE_MAPPER = new SpriteMapper(CAPE_PATTERN_SHEET, "bannercape/cape");
	public static final SpriteId CAPE_PATTERN_BASE = CAPE_MAPPER.defaultNamespaceApply("base");

	public static SpriteId getCapeSprite(final Holder<BannerPattern> pattern) {
		return CAPE_SPRITES.computeIfAbsent(pattern.value().assetId(), CAPE_MAPPER::apply);
	}

	public static void registerRenderer(EntityRendererProvider.Context context) {
        ColorCollection.zipApply(ColorCollection.VALUES, Items.BANNER, (baseColor, item) ->
				TrinketRendererRegistry.registerRenderer(item, new BannerCapeRenderer(
						baseColor,
						context.getModelSet(),
						context.getSprites(),
						context.getEquipmentAssets()
				))
		);
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		AtlasRegistry.register(new AtlasManager.AtlasConfig(CAPE_PATTERN_SHEET, CAPE_PATTERN_ATLAS, false));
	}
}