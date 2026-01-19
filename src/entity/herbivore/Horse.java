package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Horse extends Herbivore {
    public Horse(Location l) {
        super(l, Species.HORSE);
    }
}
