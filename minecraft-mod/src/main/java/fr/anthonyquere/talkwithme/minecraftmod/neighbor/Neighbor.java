package fr.anthonyquere.talkwithme.minecraftmod.neighbor;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.domains.CoreAPI;
import fr.anthonyquere.talkwithme.domains.CoreAPIClientFactory;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.goals.SleepInBedGoal;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.NeighborHouseMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.ITEMS;
import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public abstract class Neighbor {
  private static final Logger LOGGER = LogUtils.getLogger();

  private final String name;
  private final String id;
  private EntityType<Entity> entityType;


  public Neighbor(String name) {
    this.name = name;
    this.id = name.toLowerCase().replaceAll("[^a-z]", "_");
  }

  public abstract String getMessage() throws Exception;

  public abstract ResourceLocation geTextureResourceLocation();

  public abstract List<Block> getRequiredBlocks();

  public ModelLayerLocation getModelLayerLocation() {
    return new ModelLayerLocation(new ResourceLocation(MODID, this.id), "main");
  }

  public final String getSpawnEggId() {
    return this.id + "_spawn_egg";
  }

  public static ForgeSpawnEggItem getSpawnEgg(RegistryObject<EntityType<Entity>> registryObject) {
    return new ForgeSpawnEggItem(registryObject, 0x0077ff, 0xffffff, new Item.Properties());
  }

  public String getId() {
    return this.id;
  }

  public EntityType<Entity> getEntityType() {
    if (entityType == null) {
      entityType = EntityType.Builder.of((EntityType<Entity> e, Level l) -> new Entity(getId(), e, l), MobCategory.MONSTER)
        .sized(0.6F, 0.7F)
        .clientTrackingRange(8)
        .build(this.id);
    }

    return entityType;
  }

  public class Entity extends AgeableMob {
    private final String id;
    private int lastInteractedTick;

    public Entity(String id, EntityType<? extends AgeableMob> entityType, Level world) {
      super(entityType, world);
      this.id = id;
      this.setCustomName(Component.literal(name));
      this.setSpeed(0.6F);
    }


    @Override
    protected void dropFromLootTable(@NotNull DamageSource damageSource, boolean p_21390_) {
      super.dropFromLootTable(damageSource, p_21390_);
      this.spawnAtLocation(new ItemStack(
        ITEMS.getEntries()
          .stream()
          .filter(e -> e.getId().getPath().contains(this.id + "_spawn_egg"))
          .findFirst()
          .map(RegistryObject::get)
          .orElse(Items.SWEET_BERRIES)));
    }

    public static AttributeSupplier getAttributeSupplier() {
      return Mob.createMobAttributes()
        .add(Attributes.MAX_HEALTH, 10.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.3F)
        .add(Attributes.ATTACK_DAMAGE, 3.0D)
        .build();
    }

    @Override
    protected void registerGoals() {
      super.registerGoals();
      int i = 1;
      this.goalSelector.addGoal(++i, new SleepInBedGoal(this));
      this.goalSelector.addGoal(++i, new AvoidEntityGoal<>(this, Monster.class, 6.0F, 1.0D, 1.2D));
      this.goalSelector.addGoal(++i, new LookAtPlayerGoal(this, Player.class, 10.0F));
      this.goalSelector.addGoal(++i, new LookAtPlayerGoal(this, Neighbor.Entity.class, 10.0F));
      this.goalSelector.addGoal(++i, new RandomLookAroundGoal(this));
      this.goalSelector.addGoal(++i, new FloatGoal(this));
      this.goalSelector.addGoal(++i, new WaterAvoidingRandomStrollGoal(this, 0.75));
      this.goalSelector.addGoal(++i, new LeapAtTargetGoal(this, 0.5F));
    }


    public List<Player> getNearbyPlayers() {
      return this.level().getEntities(this, new AABB(
          this.getX() - 16,
          this.getY() - 16,
          this.getZ() - 16,
          this.getX() + 16,
          this.getY() + 16,
          this.getZ() + 16
        ), e -> e instanceof Player)
        .stream()
        .map(e -> e instanceof Player ? (Player) e : null)
        .filter(Objects::nonNull).toList();
    }

    private @NotNull InteractionResult chat(@NotNull Player player) {

      if (player.level().isClientSide() && Minecraft.getInstance().getCurrentServer() == null) {
        player.displayClientMessage(
          Component.literal("I need a server to speak with you"),
          false
        );
      }

      if (!player.level().isClientSide()) {
        String message;
        try {
          message = new CoreAPIClientFactory().getClient().talkWithCompanion(new CoreAPI.TalkRequestPayload("Hello! How are you today?"), "vulpis").getMessage();
        } catch (Exception e) {
          LOGGER.error("Fail to communicate with companion", e);
          message = "Oh... Something failed...";
        }

        player.displayClientMessage(
          Component.literal(message),
          false
        );
      }
      return InteractionResult.SUCCESS;

    }

    private @NotNull InteractionResult openMenu(@NotNull Player player) {

      if (!this.level().isClientSide()) {
        player.openMenu(//getRequiredBlocks
          new SimpleMenuProvider((int containerId, Inventory inventory, Player p) -> new NeighborHouseMenu(containerId, inventory, p, getRequiredBlocks()), this.getDisplayName())
        );
        return InteractionResult.SUCCESS;
      }

      return InteractionResult.CONSUME;
    }


    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
      var dbg = "Player [%s] interacted with [%s] with [%s] in [%s]".formatted(player.getName().getString(), name, hand.name(), this.level().isClientSide() ? "client" : "server");
      LOGGER.debug(dbg);

      player.swing(hand);
      super.mobInteract(player, hand);


      if (!hand.equals(InteractionHand.MAIN_HAND)) {
        return InteractionResult.PASS;
      }

      if (player.isCrouching()) {
        return openMenu(player);
      }
      return openMenu(player);


//      return chat(player);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
      return null;
    }
  }
}
