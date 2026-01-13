package entity.predator;

import entity.Animal;
import entity.island.Location;

abstract class Carnivore extends Animal {
    public Carnivore(Location l) {
        super(l);
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}
