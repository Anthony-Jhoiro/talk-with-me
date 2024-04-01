package fr.anthonyquere.talkwithme.minecraftmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import org.slf4j.Logger;

public class TalkWithCompanionCommand {
  private static final Logger LOGGER = LogUtils.getLogger();

  public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    var talkCommand = Commands.literal("talk");

    NeighborRegistry.getInstance().getNeighbors()
      .forEach(neighbor -> {
        talkCommand.then(Commands.literal(neighbor.getId())
          .then(Commands.argument("message", MessageArgument.message())
            .executes(command -> {
              LOGGER.debug("running chat for: {}", neighbor.getName());
              neighbor.chat(command.getSource().getPlayer(), "Hello ! What's up ?");
              return 1;
            })));
      });

    dispatcher.register(talkCommand);
  }
}
