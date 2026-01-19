package entity.predator;

import config.Species;
import entity.island.Location;

public class Eagle extends Carnivore {
    public Eagle(Location l) {
        super(l, Species.EAGLE);
    }
}
