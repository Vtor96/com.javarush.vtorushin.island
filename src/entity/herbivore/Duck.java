package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Duck extends Herbivore {
    public Duck(Location l) {
        super(l, Species.DUCK);
    }
}
