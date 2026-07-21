package archives.tater.bannercape.client;

import archives.tater.bannercape.BannerCape;

import net.fabricmc.api.ClientModInitializer;

import eu.pb4.trinkets.impl.client.render.types.TrinketRenderElements;

public class BannerCapeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		TrinketRenderElements.ID_MAPPER.put(BannerCape.id("banner_cape"), BannerCapeTrinketElement.CODEC);
	}
}