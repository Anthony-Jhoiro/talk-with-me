package fr.anthonyquere.talkwithme.minecraftmod.registries;

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
                "vulpis", new ModelSupplier(VulpisModel::new, VulpisModel::createBodyLayer)
        );
    }

    public Function<ModelPart, EntityModel<Neighbor.Entity>> getModelBuilder(String id) {
        return renderers.get(id).modelBuilder;
    }

    public Supplier<LayerDefinition> getLayerBuilder(String id) {
        return renderers.get(id).layerDefinitionBuilder;
    }

    public record ModelSupplier(
            Function<ModelPart, EntityModel<Neighbor.Entity>> modelBuilder,
            Supplier<LayerDefinition> layerDefinitionBuilder
    ) {
    }
}
