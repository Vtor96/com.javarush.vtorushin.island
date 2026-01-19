package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Buffalo extends Herbivore {
    public Buffalo(Location l) {
        super(l, Species.BUFFALO);
    }
}
