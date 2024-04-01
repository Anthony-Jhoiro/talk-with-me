package fr.anthonyquere.talkwithme.minecraftmod.neighbor;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.goals.SleepInBedGoal;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.NeighborHouseMenu;
import fr.anthonyquere.talkwithme.minecraftmod.registries.BlocksRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.ITEMS;
import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public class NeighborEntity extends AgeableMob {
  private static final Logger LOGGER = LogUtils.getLogger();

  private final Neighbor neighbor;

  public NeighborEntity(EntityType<? extends NeighborEntity> entityType, Level world, Neighbor neighbor) {
    super(entityType, world);
    this.setCustomName(Component.literal(neighbor.getName()));
    this.setSpeed(0.6F);
    this.neighbor = neighbor;
  }


  @Override
  protected void dropFromLootTable(@NotNull DamageSource damageSource, boolean p_21390_) {
    super.dropFromLootTable(damageSource, p_21390_);
    this.spawnAtLocation(new ItemStack(
      ITEMS.getEntries()
        .stream()
        .filter(e -> e.getId().getPath().contains(neighbor.getSpawnEggId()))
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
    this.goalSelector.addGoal(++i, new LookAtPlayerGoal(this, NeighborEntity.class, 10.0F));
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
      ), Player.class::isInstance)
      .stream()
      .map(e -> e instanceof Player p ? p : null)
      .filter(Objects::nonNull).toList();
  }

  private @NotNull InteractionResult openMenu(@NotNull Player player) {

    if (!this.level().isClientSide()) {
      player.openMenu(//getRequiredBlocks
        new SimpleMenuProvider((int containerId, Inventory inventory, Player p) -> new NeighborHouseMenu(
          containerId,
          inventory,
          p,
          neighbor.getRequiredBlocks(),
          BlocksRegistry.getInstance(MODID).get(neighbor.getHouseBlueprintBlockId())
        ), this.getDisplayName())
      );
      return InteractionResult.SUCCESS;
    }

    return InteractionResult.CONSUME;
  }


  @Override
  protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
    var dbg = "Player [%s] interacted with [%s] with [%s] in [%s]".formatted(player.getName().getString(), neighbor.getName(), hand.name(), this.level().isClientSide() ? "client" : "server");
    LOGGER.debug(dbg);

    player.swing(hand);
    super.mobInteract(player, hand);


    if (!hand.equals(InteractionHand.MAIN_HAND)) {
      return InteractionResult.PASS;
    }

    return openMenu(player);
  }

  @Nullable
  @Override
  public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
    return null;
  }
}
