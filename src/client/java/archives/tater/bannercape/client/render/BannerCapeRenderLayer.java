package archives.tater.bannercape.client.render;

import archives.tater.bannercape.client.BannerCapeClient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;

import java.util.Optional;

import static net.minecraft.util.Mth.DEG_TO_RAD;

public class BannerCapeRenderLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final EquipmentAssetManager equipmentAssets;
    private final BannerRenderer bannerRenderer;

    public BannerCapeRenderLayer(final RenderLayerParent<AvatarRenderState, PlayerModel> renderer, final EntityModelSet modelSet, final EquipmentAssetManager equipmentAssets, final SpriteGetter sprites) {
        super(renderer);
        this.equipmentAssets = equipmentAssets;
        bannerRenderer = new BannerRenderer(modelSet, sprites);
    }

    private boolean hasLayer(final ItemStack itemStack, final EquipmentClientInfo.LayerType layerType) {
        var equippable = itemStack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.assetId().isEmpty()) return false;
        return !equipmentAssets.get(equippable.assetId().get()).getLayers(layerType).isEmpty();
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final AvatarRenderState state, final float yRot, final float xRot) {
        if (state.isInvisible || !state.showCape) return;
        if (hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) return;
        var capeState = state.getDataOrDefault(BannerCapeClient.BANNER_CAPE, Optional.empty()).orElse(null);
        if (capeState == null) return;

        poseStack.pushPose();
        poseStack.translate(0, 0.03125, 0.125f);
//        poseStack.scale(-0.5f, 0.5f, -0.5f);
//
//        if (hasLayer(state.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID))
//            poseStack.translate(0.0F, -0.053125F, 0.06875F);

//        poseStack.mulPose(new Quaternionf()
//                .rotateY((float) -PI)
//                .rotateX((6.0F + state.capeLean / 2.0F + state.capeFlap) * (float) (PI / 180.0))
//                .rotateZ(state.capeLean2 / 2.0F * (float) (PI / 180.0))
//                .rotateY((180.0F - state.capeLean2 / 2.0F) * (float) (PI / 180.0))
//        );


        poseStack.scale(0.5f, 0.5f, 0.5f);

        poseStack.translate(0, 0, 0.0625f);

        poseStack.mulPose(Axis.XP.rotation((6 + state.capeLean / 2 + state.capeFlap) * DEG_TO_RAD));
        poseStack.mulPose(Axis.ZP.rotation(state.capeLean2 / 2 * DEG_TO_RAD));
        poseStack.mulPose(Axis.YP.rotation((180 - state.capeLean2 / 2) * DEG_TO_RAD));

        poseStack.translate(0, 1.21875, -0.65625);

        bannerRenderer.submitSpecial(
                BannerBlock.AttachmentType.WALL,
                poseStack,
                submitNodeCollector,
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                capeState.baseColor(),
                capeState.patterns(),
                0
        );

        poseStack.popPose();
    }
}
