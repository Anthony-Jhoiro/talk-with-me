package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.registries.BlocksRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public class HouseBlueprintBlockStateProvider extends BlockStateProvider {
  private final Neighbor neighbor;

  public HouseBlueprintBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, Neighbor neighbor) {
    super(output, MODID, exFileHelper);
    this.neighbor = neighbor;
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlockWithItem(BlocksRegistry.getInstance(MODID).get(neighbor.getHouseBlueprintBlockId()), this.itemModels().getExistingFile(new ResourceLocation(MODID, "block/house_blueprint")));
  }
}
