package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Caterpillar extends Herbivore {
    public Caterpillar(Location l) {
        super(l, Species.CATERPILLAR);
    }
}
