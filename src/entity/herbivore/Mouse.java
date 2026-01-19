package entity.herbivore;

import config.Species;
import entity.island.Location;

public class Mouse extends Herbivore {
    public Mouse(Location l) {
        super(l, Species.MOUSE);
    }
}
