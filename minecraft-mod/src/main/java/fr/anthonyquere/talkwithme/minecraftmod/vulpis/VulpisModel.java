package fr.anthonyquere.talkwithme.minecraftmod.vulpis;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class VulpisModel<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("voisins", "vulpis"), "main");
    private final ModelPart Tete;
    private final ModelPart Queue;
    private final ModelPart Corps;
    private final ModelPart Pattedroite;
    private final ModelPart Pattegauche;
    private final ModelPart Brasgazuche;
    private final ModelPart root;

    public VulpisModel(ModelPart root) {
        this.root = root;
        this.Tete = root.getChild("Tete");
        this.Queue = root.getChild("Queue");
        this.Corps = root.getChild("Corps");
        this.Pattedroite = root.getChild("Pattedroite");
        this.Pattegauche = root.getChild("Pattegauche");
        this.Brasgazuche = root.getChild("Brasgazuche");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Tete = partdefinition.addOrReplaceChild("Tete", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -20.0F, -3.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(33, 8).addBox(-7.0F, -22.0F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(33, 8).mirror().addBox(2.0F, -22.0F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(32, 32).addBox(-2.5F, -15.0F, -5.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Queue = partdefinition.addOrReplaceChild("Queue", CubeListBuilder.create(), PartPose.offset(-1.0F, 18.0F, 4.0F));

        PartDefinition Queue_r1 = Queue.addOrReplaceChild("Queue_r1", CubeListBuilder.create().texOffs(52, 62).addBox(0.0F, -8.0F, 0.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.0F, -5.0F, -1.6468F, -0.9586F, 1.633F));

        PartDefinition Corps = partdefinition.addOrReplaceChild("Corps", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -12.0F, -2.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Pattedroite = partdefinition.addOrReplaceChild("Pattedroite", CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, 1.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 19.0F, 1.0F));

        PartDefinition Pattegauche = partdefinition.addOrReplaceChild("Pattegauche", CubeListBuilder.create().texOffs(25, 17).addBox(-1.173F, 0.9962F, -1.5228F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 1.0F));

        PartDefinition Brasgazuche = partdefinition.addOrReplaceChild("Brasgazuche", CubeListBuilder.create().texOffs(0, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 13.0F, 1.0F));

        PartDefinition Brasdroit = Brasgazuche.addOrReplaceChild("Brasdroit", CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateWalk(VulpisWalkAnimation.Marche, limbSwing, limbSwingAmount, 2f, 2.5f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Tete.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Queue.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Corps.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Pattedroite.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Pattegauche.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Brasgazuche.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
