package fr.anthonyquere.talkwithme.minecraftmod.neighbor.implementations;

import fr.anthonyquere.talkwithme.minecraftmod.Voisin;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

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
  public List<Block> getRequiredBlocks() {
    return List.of(
      Blocks.CAKE,
      Blocks.CANDLE,
      Blocks.BEEHIVE
    );
  }

}
