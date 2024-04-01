package fr.anthonyquere.talkwithme.minecraftmod.neighbor;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.domains.CoreAPI;
import fr.anthonyquere.talkwithme.domains.CoreAPIClientFactory;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.HouseBlueprintBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.List;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public abstract class Neighbor {
  private static final Logger LOGGER = LogUtils.getLogger();

  private final String name;
  private final String id;
  private EntityType<NeighborEntity> entityType;


  protected Neighbor(String name) {
    this.name = name;
    this.id = name.toLowerCase().replaceAll("[^a-z]", "_");
  }

  public String getName() {
    return this.name;
  }

  public abstract ResourceLocation geTextureResourceLocation();

  public abstract ResourceLocation getHouseStructure();

  public TagKey<Biome> getSpawnBiomes() {
    return BiomeTags.IS_OVERWORLD;
  }

  protected abstract List<Block> getRequiredBlocks();

  public ModelLayerLocation getModelLayerLocation() {
    return new ModelLayerLocation(new ResourceLocation(MODID, this.id), "main");
  }

  public final String getSpawnEggId() {
    return this.id + "_spawn_egg";
  }

  public final String getHouseBlueprintBlockId() {
    return this.id + "_house_blueprint";
  }

  public static ForgeSpawnEggItem getSpawnEgg(RegistryObject<EntityType<NeighborEntity>> registryObject) {
    return new ForgeSpawnEggItem(registryObject, 0x0077ff, 0xffffff, new Item.Properties());
  }

  public String getId() {
    return this.id;
  }

  public EntityType<NeighborEntity> getEntityType() {
    if (entityType == null) {
      entityType = EntityType.Builder.<NeighborEntity>of((et, world) -> new NeighborEntity(et, world, this),
          MobCategory.MONSTER)
        .sized(0.6F, 0.7F)
        .clientTrackingRange(8)
        .build(this.id);
    }

    return entityType;
  }

  /**
   * /!\ Call only when populating registry
   */
  public HouseBlueprintBlock getHouseBlock() {
    return new HouseBlueprintBlock(this);
  }

  public void chat(@NotNull Player player, String playerMessage) {
    var level = player.level();

    LOGGER.debug("start chat procedure on {}; player= {}; neighbor= {}", level.isClientSide() ? "client" : "server", player.getDisplayName().getString(), this.name);

    if (level.isClientSide() && Minecraft.getInstance().getCurrentServer() == null) {
      player.displayClientMessage(
        Component.literal("I need a server to speak with you"),
        false
      );
      return;
    }

    if (!level.isClientSide()) {
      var message = talkWithAi(playerMessage);
      LOGGER.debug("Neighbor message: {}", message);

      player.displayClientMessage(
        Component.literal(message),
        false
      );
    }
  }

  private String talkWithAi(String playerMessage) {
    try {
      return new CoreAPIClientFactory().getClient().talkWithCompanion(new CoreAPI.TalkRequestPayload(playerMessage), Neighbor.this.getId()).getMessage();
    } catch (Exception e) {
      LOGGER.error("Fail to communicate with companion", e);
      return "Oh... Something failed...";
    }
  }
}
