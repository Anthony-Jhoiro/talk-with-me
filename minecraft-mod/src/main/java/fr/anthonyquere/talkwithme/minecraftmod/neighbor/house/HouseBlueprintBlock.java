package fr.anthonyquere.talkwithme.minecraftmod.neighbor.house;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.VariantProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import static fr.anthonyquere.talkwithme.minecraftmod.Voisin.MODID;

public abstract class HouseBlueprintBlock extends Block {
  private static final Logger LOGGER = LogUtils.getLogger();

  protected abstract ResourceLocation getHouseResourceLocation();

  protected HouseBlueprintBlock() {
    super(Properties.of().instabreak());
  }

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos position, Player player, @NotNull InteractionHand ignored, @NotNull BlockHitResult ignored2) {
    if (level.isClientSide) {
      return InteractionResult.PASS;
    }

    // On server
    var maybeTemplate = level.getServer().getStructureManager()
      .get(getHouseResourceLocation());

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

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
  }
}
