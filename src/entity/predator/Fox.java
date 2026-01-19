package entity.predator;

import config.Species;
import entity.island.Location;

public class Fox extends Carnivore {
    public Fox(Location l) {
        super(l, Species.FOX);
    }
}
