package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Rabbit extends Herbivore {
    public Rabbit(Location l) {
        super(l, Species.RABBIT);
    }
}
