package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Objects;

public class HouseBlueprintBlock extends Block {
  private static final Logger LOGGER = LogUtils.getLogger();
  private final Neighbor neighbor;

  public HouseBlueprintBlock(Neighbor neighbor) {
    super(Properties.of().instabreak());
    this.neighbor = neighbor;
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos position, Player player, @NotNull InteractionHand ignored, @NotNull BlockHitResult ignored2) {
    if (level.isClientSide) {
      return InteractionResult.PASS;
    }

    // On server
    var maybeTemplate = level.getServer().getStructureManager()
      .get(neighbor.getHouseStructure());

    if (maybeTemplate.isEmpty()) {
      player.displayClientMessage(Component.literal("The home was not found"), false);
      return InteractionResult.CONSUME;
    }

    var template = maybeTemplate.get();

    var settings = new StructurePlaceSettings();

    var corner = position.offset(-template.getSize().getX() / 2, -1, -template.getSize().getZ() / 2);

    LOGGER.info("Placing house at {}, corner = {}", position, corner);

    var placingHouseResult = template.placeInWorld(
      Objects.requireNonNull(level.getServer().getLevel(level.dimension())), corner, corner, settings, level.random, 2
    );

    return InteractionResult.sidedSuccess(placingHouseResult);
  }
}
