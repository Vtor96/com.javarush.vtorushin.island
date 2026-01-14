package entity.herbivore;

import entity.Animal;
import entity.island.Location;

abstract class Herbivore extends Animal {
    public Herbivore(Location l) {
        super(l);
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isAlive() {
        return super.isAlive();
    }
}
