package fr.anthonyquere.talkwithme.minecraftmod;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.NeighborRenderer;
import fr.anthonyquere.talkwithme.minecraftmod.vulpis.VulpisModel;
import com.mojang.logging.LogUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Voisin.MODID)
public class Voisin {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "voisins";
    public static final NeighborRegistry neighborRegistry = new NeighborRegistry();
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Voisin() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        LOGGER.info("START REGISTERING ENTITIES");

        neighborRegistry.getNeighbors()
                .forEach(neighbor -> {
                    var registeredEntity = ENTITIES.register(neighbor.getId(), neighbor::getEntityType);
                    ITEMS.register(neighbor.getSpawnEggId(), () -> Neighbor.getSpawnEgg(registeredEntity));
                });
        LOGGER.info("STOP REGISTERING ENTITIES");


        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("Starting common setup");
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Starting server");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventBusClientEvents {
        private static final NeighborModelRegistry neighborModelRegistry = new NeighborModelRegistry();

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent _ignored) {
            LOGGER.info("START REGISTERING RENDERERS");

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
            LOGGER.info("START REGISTERING LAYERS");

            neighborRegistry.getNeighbors()
                    .forEach(neighbor -> {
                        LOGGER.debug("Register layer for {}", neighbor.getId());
                        event.registerLayerDefinition(
                                neighbor.getModelLayerLocation(),
                                neighborModelRegistry.getLayerBuilder(neighbor.getId())
                        );
                    });

            event.registerLayerDefinition(new ModelLayerLocation(
                    new ResourceLocation(MODID, "vulpis"), "main"), VulpisModel::createBodyLayer);
            LOGGER.info("STOP REGISTERING LAYERS");

        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            LOGGER.info("START REGISTERING ENTITIES ATTRIBUTES");

            neighborRegistry.getNeighbors()
                    .forEach(
                            neighbor -> {
                                LOGGER.debug("Register attributes for {}", neighbor.getId());
                                event.put(
                                        neighbor.getEntityType(),
                                        Neighbor.Entity.getAttributeSupplier()
                                );
                            }
                    );

            LOGGER.info("STOP REGISTERING ENTITIES ATTRIBUTES");
        }
    }
}
