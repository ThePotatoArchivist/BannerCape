package archives.tater.bannercape.client.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityTypes;

import eu.pb4.trinkets.api.DefaultTrinketSlots;
import eu.pb4.trinkets.api.datagen.TrinketsDataProvider;

import java.util.concurrent.CompletableFuture;

public class TrinketsGenerator extends TrinketsDataProvider {
    public TrinketsGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries);
    }

    @Override
    protected void generate(TrinketsOutput output) {
        output.entitySlots("cape")
                .addSlot(DefaultTrinketSlots.CHEST_CAPE)
                .addPlayer()
                .addEntity(EntityTypes.MANNEQUIN);
    }

    @Override
    public String getName() {
        return "Trinkets Data";
    }
}
