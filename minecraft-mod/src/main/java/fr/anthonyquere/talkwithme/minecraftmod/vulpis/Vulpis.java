package fr.anthonyquere.talkwithme.minecraftmod.vulpis;

import fr.anthonyquere.talkwithme.minecraftmod.Voisin;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class Vulpis extends Neighbor {

  public Vulpis() {
    super("Vulpis");
  }

  @Override
  public ResourceLocation geTextureResourceLocation() {
    return new ResourceLocation(Voisin.MODID, "textures/entity/vulpis.png");
  }

  @Override
  public ResourceLocation getHouseStructure() {
    return new ResourceLocation(Voisin.MODID, "chouette_house");
  }

  @Override
  public List<Block> getRequiredBlocks() {
    return List.of(
      Blocks.CAKE,
      Blocks.CANDLE,
      Blocks.BEEHIVE
    );
  }

}
