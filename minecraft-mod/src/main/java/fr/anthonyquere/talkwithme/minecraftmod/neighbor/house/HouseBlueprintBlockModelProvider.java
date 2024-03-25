package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public class HouseBlueprintBlockModelProvider extends BlockModelProvider {
  public HouseBlueprintBlockModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    var door = modLoc("block/blueprint_door");
    var blank = modLoc("block/blueprint_block");

    withExistingParent("house_blueprint", mcLoc("block/cube"))
      .texture("down", blank)
      .texture("up", blank)
      .texture("north", door)
      .texture("east", blank)
      .texture("south", blank)
      .texture("west", blank);
  }
}
