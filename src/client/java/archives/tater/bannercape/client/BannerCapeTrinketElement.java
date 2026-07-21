package archives.tater.bannercape.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.impl.client.render.TrinketRenderState;
import eu.pb4.trinkets.impl.client.render.types.AttachmentSettings;
import eu.pb4.trinkets.impl.client.render.types.TrinketRenderElement;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public final class BannerCapeTrinketElement implements TrinketRenderElement {
    public static final MapCodec<BannerCapeTrinketElement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AttachmentSettings.CODEC.forGetter(element -> element.attachmentSettings)
    ).apply(instance, BannerCapeTrinketElement::new));

    private final AttachmentSettings attachmentSettings;

    public BannerCapeTrinketElement(AttachmentSettings attachmentSettings) {
        this.attachmentSettings = attachmentSettings;
    }

    @Override
    public MapCodec<? extends TrinketRenderElement> codec() {
        return CODEC;
    }

    @Override
    public void apply(LivingEntity livingEntity, ItemStack stack, TrinketSlotAccess access, @Nullable TrinketRenderState state, Consumer<TrinketRenderState.PartAttachedRenderer> consumer) {
        if (!(stack.getItem() instanceof BannerItem bannerItem)) return;

        consumer.accept(new TrinketRenderState.PartAttachedRenderer(
                attachmentSettings, (poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor) -> {

        }
        ));
    }
}
