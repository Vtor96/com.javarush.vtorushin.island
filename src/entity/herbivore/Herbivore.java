package entity.herbivore;

import config.Species;
import entity.Animal;
import entity.island.Location;

abstract class Herbivore extends Animal {

    protected Herbivore(Location l, Species species) {
        super(l, species);
    }
}
