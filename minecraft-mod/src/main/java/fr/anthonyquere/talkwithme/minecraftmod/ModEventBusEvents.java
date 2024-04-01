package fr.anthonyquere.talkwithme.minecraftmod;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.commands.TalkWithCompanionCommand;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.NeighborEntity;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.HouseBlueprintBlockModelProvider;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.HouseBlueprintBlockStateProvider;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.spawn.NeighborSpawnProvider;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborRegistry;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Voisin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

  private static final Logger LOGGER = LogUtils.getLogger();
  private static final NeighborRegistry neighborRegistry = NeighborRegistry.getInstance();

  @SubscribeEvent
  public static void registerAttributes(EntityAttributeCreationEvent event) {
    LOGGER.info("START REGISTERING ENTITIES ATTRIBUTES");

    neighborRegistry.getNeighbors()
      .forEach(
        neighbor -> {
          LOGGER.debug("Register attributes for {}", neighbor.getId());
          event.put(
            neighbor.getEntityType(),
            NeighborEntity.getAttributeSupplier()
          );
        }
      );

    LOGGER.info("STOP REGISTERING ENTITIES ATTRIBUTES");
  }

  @SubscribeEvent
  public static void clientDataGeneration(GatherDataEvent event) {
    LOGGER.info("START REGISTERING DATA ASSETS GENERATORS");
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var existingFileHelper = event.getExistingFileHelper();


    // Register house blueprint block model
    generator.addProvider(event.includeClient(), new HouseBlueprintBlockModelProvider(packOutput, existingFileHelper));

    neighborRegistry.getNeighbors()
      .forEach(neighbor -> {
        // Register block state
        generator.addProvider(event.includeClient(), new HouseBlueprintBlockStateProvider(packOutput, existingFileHelper, neighbor));

        // Register spawn provider
        generator.addProvider(event.includeServer(), new NeighborSpawnProvider(packOutput, event.getLookupProvider(), neighbor));
      });

    LOGGER.info("STOP REGISTERING DATA ASSETS GENERATORS");
  }

  @SubscribeEvent
  public void registerCommandsEvent(RegisterCommandsEvent event) {
    LOGGER.info("START REGISTERING COMMANDS");
    new TalkWithCompanionCommand().register(event.getDispatcher());
    LOGGER.info("STOP REGISTERING COMMANDS");
  }
}
