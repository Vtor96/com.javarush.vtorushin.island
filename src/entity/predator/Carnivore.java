package entity.predator;

import config.Species;
import entity.Animal;
import entity.island.Location;

abstract class Carnivore extends Animal {

    protected Carnivore(Location l, Species species) {
        super(l, species);
    }
}
