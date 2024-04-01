package fr.anthonyquere.talkwithme.minecraftmod.registries;

import fr.anthonyquere.talkwithme.minecraftmod.Voisin;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlocksRegistry {
  private final DeferredRegister<Block> blockRegistry;
  private final DeferredRegister<Item> itemRegistries;


  private static BlocksRegistry instance;

  public static BlocksRegistry getInstance(String modId) {
    if (instance == null) {
      instance = new BlocksRegistry(modId);
    }
    return instance;
  }

  private BlocksRegistry(String modId) {
    this.blockRegistry = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
    this.itemRegistries = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
    load();
  }

  public void registerRegistries(IEventBus modEventBus) {
    this.blockRegistry.register(modEventBus);
    this.itemRegistries.register(modEventBus);
  }

  private RegistryObject<Block> registerBlock(String path, Supplier<Block> blockSupplier) {
    var registryBlock = this.blockRegistry.register(path, blockSupplier);
    this.itemRegistries.register(path, () -> new BlockItem(registryBlock.get(), new Item.Properties()));
    return registryBlock;
  }

  private void load() {
    NeighborRegistry.getInstance().getNeighbors()
      .forEach(neighbor ->
        this.registerBlock(neighbor.getHouseBlueprintBlockId(), neighbor::getHouseBlock)
      );
  }

  public Block get(String id) {
    return this.blockRegistry.getEntries().stream().filter(e -> e.getId().getPath().equals(id)).findFirst().get().get();
  }
}
