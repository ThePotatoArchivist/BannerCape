package archives.tater.bannercape.client.mixin;

import archives.tater.bannercape.client.BannerCapeClient;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(
            method = "onResourceManagerReload",
            at = @At("TAIL")
    )
    private void registerCapeRenderer(ResourceManager resourceManager, CallbackInfo ci, @Local(name = "context") EntityRendererProvider.Context context) {
        BannerCapeClient.registerRenderer(context);
    }
}
