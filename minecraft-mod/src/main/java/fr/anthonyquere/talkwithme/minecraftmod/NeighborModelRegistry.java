package fr.anthonyquere.talkwithme.minecraftmod;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.vulpis.VulpisModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class NeighborModelRegistry {

    private final Map<String, ModelSupplier> renderers;

    public NeighborModelRegistry() {
        renderers = Map.of(
                "ilia_the_cat", new ModelSupplier(VulpisModel::new, VulpisModel::createBodyLayer)
        );
    }

    Function<ModelPart, EntityModel<Neighbor.Entity>> getModelBuilder(String id) {
        return renderers.get(id).modelBuilder;
    }

    Supplier<LayerDefinition> getLayerBuilder(String id) {
        return renderers.get(id).layerDefinitionBuilder;
    }

    record ModelSupplier(
            Function<ModelPart, EntityModel<Neighbor.Entity>> modelBuilder,
            Supplier<LayerDefinition> layerDefinitionBuilder
    ) {
    }
}
