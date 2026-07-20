package archives.tater.bannercape.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import eu.pb4.trinkets.api.DefaultTrinketSlotTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(DefaultTrinketSlotTags.CHEST_CAPE)
                .forceAddTag(ItemTags.BANNERS);
    }
}
