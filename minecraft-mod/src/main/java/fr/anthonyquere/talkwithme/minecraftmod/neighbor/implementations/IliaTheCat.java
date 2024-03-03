package fr.anthonyquere.talkwithme.minecraftmod.neighbor.implementations;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.vulpis.VulpisModel;
import fr.anthonyquere.talkwithme.minecraftmod.Voisin;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public class IliaTheCat extends Neighbor {

    public IliaTheCat() {
        super("Ilia The Cat");
    }

    @Override
    public String getMessage() throws Exception {
        return "§bChloe (She/her): §fOh, wow! They got this for me? That's so nice!! Thank you for delivering it!";
    }

    @Override
    public ResourceLocation geTextureResourceLocation() {
        return new ResourceLocation(Voisin.MODID, "textures/entity/vulpis.png");
    }

    @Override
    public LayerDefinition createBodyLayer() {
        return VulpisModel.createBodyLayer();
    }
}
