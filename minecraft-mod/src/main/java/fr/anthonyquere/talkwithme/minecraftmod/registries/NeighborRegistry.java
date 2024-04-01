package fr.anthonyquere.talkwithme.minecraftmod.registries;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.vulpis.Vulpis;

import java.util.List;

public class NeighborRegistry {
  private static NeighborRegistry instance;
  public static NeighborRegistry getInstance() {
    if (instance == null) {
      instance = new NeighborRegistry();
    }
    return instance;
  }

    private final List<Neighbor> neighbors;

    public NeighborRegistry() {
        neighbors = List.of(
                new Vulpis()
        );
    }

    public List<Neighbor> getNeighbors() {
        return neighbors;
    }
}
