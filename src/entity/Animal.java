package entity;

import entity.island.Location;
import util.Settings;

public abstract class Animal implements Eatable {
    protected Location location;
    protected boolean alive = true;

    public Animal(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public double getWeight() {
        return Settings.SPECIES.get(getType()).weight;
    }
}
