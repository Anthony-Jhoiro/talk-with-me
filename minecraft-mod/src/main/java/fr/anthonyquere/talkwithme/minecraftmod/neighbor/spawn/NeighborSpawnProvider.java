package fr.anthonyquere.talkwithme.minecraftmod.neighbor.spawn;

import fr.anthonyquere.talkwithme.minecraftmod.Voisin;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NeighborSpawnProvider extends DatapackBuiltinEntriesProvider {

  public static void bootstapBiomeModifiers(BootstapContext<BiomeModifier> context, Neighbor neighbor) {
    var biomes = context.lookup(Registries.BIOME);

    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Voisin.MODID, "add_neighbor_" + neighbor.getId() + "_spawns")),
      new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
        biomes.getOrThrow(neighbor.getSpawnBiomes()),
        List.of(
          new MobSpawnSettings.SpawnerData(
            neighbor.getEntityType(),
            Weight.of(1), // 1 = rarest; 100 = most common
            1, // only spawn a single entity each time
            1 // only spawn a single entity each time
          ))
      )
    );


  }

  public NeighborSpawnProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Neighbor neighbor) {
//    super(output, registries, new RegistrySetBuilder()
//        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, context -> context.register(
//          ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Voisin.MODID, "add_neighbor_" + neighbor.getId() + "_spawns")),
//          new NeighborSpawn(neighbor)
//        )),
//      Set.of(Voisin.MODID));


    super(output, registries, new RegistrySetBuilder()
        .add(ForgeRegistries.Keys.BIOME_MODIFIERS, context -> NeighborSpawnProvider.bootstapBiomeModifiers(context, neighbor)),
      Set.of(Voisin.MODID));
  }
}
