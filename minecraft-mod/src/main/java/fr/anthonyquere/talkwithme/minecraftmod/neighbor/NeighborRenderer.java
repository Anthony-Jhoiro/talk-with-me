package fr.anthonyquere.talkwithme.minecraftmod.neighbor;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class NeighborRenderer<T extends NeighborEntity> extends MobRenderer<T, EntityModel<T>> {
    private final Neighbor neighbor;

    public NeighborRenderer(EntityRendererProvider.Context context, Neighbor neighbor, Function<ModelPart, EntityModel<T>> mb) {
        super(context, mb.apply(context.bakeLayer(neighbor.getModelLayerLocation())), 0.6F);
        this.neighbor = neighbor;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull NeighborEntity _ignored) {
        return neighbor.geTextureResourceLocation();
    }
}
