package fr.anthonyquere.talkwithme.minecraftmod;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.NeighborRenderer;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.house.NeighborHouseMenuScreen;
import fr.anthonyquere.talkwithme.minecraftmod.registries.MenuRegistry;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborModelRegistry;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = Voisin.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
  private static final Logger LOGGER = LogUtils.getLogger();

  private static final NeighborModelRegistry neighborModelRegistry = new NeighborModelRegistry();
  private static final NeighborRegistry neighborRegistry = NeighborRegistry.getInstance();

  @SubscribeEvent
  public static void onClientSetup(FMLClientSetupEvent event) {
    LOGGER.info("START REGISTERING RENDERERS");

    event.enqueueWork(
      () -> MenuScreens.register(MenuRegistry.NeighborHouseMenu.get(), NeighborHouseMenuScreen::new)
    );


    neighborRegistry.getNeighbors()
      .forEach(
        neighbor -> {
          LOGGER.debug("Register renderer for {}", neighbor.getId());
          EntityRenderers.register(
            neighbor.getEntityType(),
            (ctx) -> new NeighborRenderer<>(
              ctx,
              neighbor,
              neighborModelRegistry.getModelBuilder(neighbor.getId())
            )
          );

        }
      );

    LOGGER.info("STOP REGISTERING RENDERERS");

  }

  @SubscribeEvent
  public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
    LOGGER.info("START REGISTERING MODEL LAYERS");

    neighborRegistry.getNeighbors()
      .forEach(neighbor -> {
        LOGGER.debug("Register layer for {}", neighbor.getId());
        event.registerLayerDefinition(
          neighbor.getModelLayerLocation(),
          neighborModelRegistry.getLayerBuilder(neighbor.getId())
        );
      });
    LOGGER.info("STOP REGISTERING MODEL LAYERS");

  }

}
