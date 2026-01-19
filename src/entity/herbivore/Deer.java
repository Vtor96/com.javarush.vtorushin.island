package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Deer extends Herbivore {
    public Deer(Location l) {
        super(l, Species.DEER);
    }
}
