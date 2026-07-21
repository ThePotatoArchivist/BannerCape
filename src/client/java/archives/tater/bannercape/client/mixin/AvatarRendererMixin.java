package archives.tater.bannercape.client.mixin;

import archives.tater.bannercape.client.BannerCapeClient;
import archives.tater.bannercape.client.BannerCapeClient.BannerCapeRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import eu.pb4.trinkets.api.DefaultTrinketSlots;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	@Inject(
			method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
			at = @At("TAIL")
	)
	private void extractCape(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		state.setData(BannerCapeClient.BANNER_CAPE, Optional.ofNullable(getCapeState(entity)));
	}

	@Unique
    private @Nullable BannerCapeRenderState getCapeState(AvatarlikeEntity entity) {
		var slotAccess = entity.getTrinkets().getSlotAccess(DefaultTrinketSlots.CHEST_CAPE, 0);
		if (slotAccess == null) return null;

		var stack = slotAccess.get();
		if (stack.isEmpty()) return null;

		var item = stack.getItem();
		if (!(item instanceof BannerItem bannerItem)) return null;

		return new BannerCapeRenderState(
				bannerItem.getColor(),
				stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
		);
	}
}