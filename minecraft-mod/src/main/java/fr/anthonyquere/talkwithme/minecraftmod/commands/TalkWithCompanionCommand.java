package fr.anthonyquere.talkwithme.minecraftmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.NeighborEntity;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import java.util.Arrays;

public class TalkWithCompanionCommand {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final int MAX_DISTANCE_FROM_PLAYER = 16;

  public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    var talkCommand = Commands.literal("talk");

    NeighborRegistry.getInstance().getNeighbors()
      .forEach(neighbor -> talkCommand.then(Commands.literal(neighbor.getId())
        .then(Commands.argument("message", MessageArgument.message())
          .executes(command -> {
            execute(command, neighbor);
            return 1;
          }))));

    dispatcher.register(talkCommand);
  }

  public void execute(CommandContext<CommandSourceStack> command, Neighbor neighbor) {
    LOGGER.debug("running chat for: {}", neighbor.getName());
    var player = command.getSource().getPlayer();
    if (player == null) {
      return;
    }

    if (!isNeighborClose(player, neighbor)) {
      return;
    }

    var commandsParts = Arrays.stream(command.getInput().split(" ")).toList();

    neighbor.chat(player, String.join(" ", commandsParts.subList(2, commandsParts.size())));
  }

  public boolean isNeighborClose(Player player, Neighbor neighbor) {
    return player.level().getEntities(player, new AABB(
        player.getX() - MAX_DISTANCE_FROM_PLAYER,
        player.getY() - MAX_DISTANCE_FROM_PLAYER,
        player.getZ() - MAX_DISTANCE_FROM_PLAYER,
        player.getX() + MAX_DISTANCE_FROM_PLAYER,
        player.getY() + MAX_DISTANCE_FROM_PLAYER,
        player.getZ() + MAX_DISTANCE_FROM_PLAYER
      ), NeighborEntity.class::isInstance)
      .stream()
      .anyMatch(e -> e instanceof NeighborEntity ne && ne.getNeighbor().getId().equals(neighbor.getId()));
  }
}
