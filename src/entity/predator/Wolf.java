package entity.predator;

import config.Species;
import entity.island.Location;

public class Wolf extends Carnivore {
    public Wolf(Location l) {
        super(l, Species.WOLF);
    }
}
