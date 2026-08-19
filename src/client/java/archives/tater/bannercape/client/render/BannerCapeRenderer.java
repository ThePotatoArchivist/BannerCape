package archives.tater.bannercape.client.render;

import archives.tater.bannercape.client.BannerCapeClient;
import archives.tater.bannercape.client.mixin.BannerRendererAccessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;

public class BannerCapeRenderer implements TrinketRenderer {
    private final DyeColor baseColor;
    private final SpriteGetter sprites;
    private final HumanoidModel<AvatarRenderState> model;
    private final EquipmentAssetManager equipmentAssets;

    public BannerCapeRenderer(DyeColor baseColor, EntityModelSet modelSet, SpriteGetter sprites, EquipmentAssetManager equipmentAssets) {
        this.baseColor = baseColor;
        this.sprites = sprites;
        model = new PlayerCapeModel(modelSet.bakeLayer(ModelLayers.PLAYER_CAPE));
        this.equipmentAssets = equipmentAssets;
    }

    private boolean hasLayer(final ItemStack itemStack, final EquipmentClientInfo.LayerType layerType) {
        var equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.assetId().isEmpty()) return false;
        var equipmentClientInfo = equipmentAssets.get(equippable.assetId().get());
        return !equipmentClientInfo.getLayers(layerType).isEmpty();
    }

    @Override
    public void submit(ItemStack stack, TrinketSlotAccess slotReference, EntityModel<? extends LivingEntityRenderState> contextModel, PoseStack poseStack, SubmitNodeCollector submit, int light, LivingEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.isInvisible) return;
        if (!(state instanceof AvatarRenderState avatar)) return;
        if (!avatar.showCape) return;
        if (hasLayer(avatar.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) return;

        poseStack.pushPose();
        if (hasLayer(avatar.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID))
            poseStack.translate(0.0F, -0.053125F, 0.06875F);

        submit.submitModel(model, avatar, poseStack, light, OverlayTexture.NO_OVERLAY, -1, BannerCapeClient.CAPE_PATTERN_BASE, sprites, state.outlineColor, null);

        BannerRendererAccessor.callSubmitPatternLayer(
                sprites,
                poseStack,
                submit,
                light,
                OverlayTexture.NO_OVERLAY,
                model,
                avatar,
                BannerCapeClient.CAPE_PATTERN_BASE,
                baseColor,
                null
        );

        var patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);

        for (var maskIndex = 0; maskIndex < 16 && maskIndex < patterns.layers().size(); maskIndex++) {
            var layer = patterns.layers().get(maskIndex);
            BannerRendererAccessor.callSubmitPatternLayer(
                    sprites,
                    poseStack,
                    submit.order(maskIndex + 1),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    model,
                    avatar,
                    BannerCapeClient.getCapeSprite(layer.pattern()),
                    layer.color(),
                    null
            );
        }

        poseStack.popPose();
    }
}
