package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Sheep extends Herbivore {
    public Sheep(Location l) {
        super(l, Species.SHEEP);
    }
}
