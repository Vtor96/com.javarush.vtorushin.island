package entity;

import config.Species;
import entity.island.Location;

public class Plant implements Eatable {
    private Location location;
    private boolean alive = true;

    public Plant() {
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public double getWeight() {
        return Species.PLANT.getWeight();
    }

    @Override
    public Species getSpecies() {
        return Species.PLANT;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void die() {
        this.alive = false;
    }

    public String getSymbol() {
        return Species.PLANT.getEmoji();
    }

    @Override
    public String toString() {
        return "Растение";
    }
}
