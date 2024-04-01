package fr.anthonyquere.talkwithme.minecraftmod;

import com.mojang.logging.LogUtils;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.registries.BlocksRegistry;
import fr.anthonyquere.talkwithme.minecraftmod.registries.MenuRegistry;
import fr.anthonyquere.talkwithme.minecraftmod.registries.NeighborRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
  public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

  private static final Logger LOGGER = LogUtils.getLogger();

  public Voisin() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    LOGGER.info("START REGISTERING ENTITIES");

    NeighborRegistry.getInstance().getNeighbors()
      .forEach(neighbor -> {
        var registeredEntity = ENTITIES.register(neighbor.getId(), neighbor::getEntityType);
        ITEMS.register(neighbor.getSpawnEggId(), () -> Neighbor.getSpawnEgg(registeredEntity));
      });

    LOGGER.info("STOP REGISTERING ENTITIES");

    BlocksRegistry.getInstance(MODID).registerRegistries(modEventBus);
    MenuRegistry.REGISTRY.register(modEventBus);

    ENTITIES.register(modEventBus);
    ITEMS.register(modEventBus);



    MinecraftForge.EVENT_BUS.register(new ModEventBusEvents());
    MinecraftForge.EVENT_BUS.register(this);

    // Register the commonSetup method for modloading
    modEventBus.addListener(this::commonSetup);

    // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {
    // Some common setup code
    LOGGER.info("Starting common setup");
  }
}
