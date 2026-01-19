package entity.predator;

import config.Species;
import entity.island.Location;

public class Bear extends Carnivore {
    public Bear(Location l) {
        super(l, Species.BEAR);
    }
}
