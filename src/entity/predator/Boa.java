package entity.predator;

import config.Species;
import entity.island.Location;

public class Boa extends Carnivore {
    public Boa(Location l) {
        super(l, Species.BOA);
    }
}
