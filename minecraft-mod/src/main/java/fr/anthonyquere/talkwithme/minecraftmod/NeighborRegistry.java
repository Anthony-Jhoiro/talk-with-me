package fr.anthonyquere.talkwithme.minecraftmod;

import fr.anthonyquere.talkwithme.minecraftmod.neighbor.Neighbor;
import fr.anthonyquere.talkwithme.minecraftmod.neighbor.implementations.IliaTheCat;

import java.util.List;

public class NeighborRegistry {

    private final List<Neighbor> neighbors;

    public NeighborRegistry() {
        neighbors = List.of(
                new IliaTheCat()
        );
    }

    List<Neighbor> getNeighbors() {
        return neighbors;
    }
}
