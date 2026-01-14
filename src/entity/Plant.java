package entity;

import entity.island.Location;
import util.Settings;

public class Plant implements Eatable {
    private Location location;
    private boolean alive = true;

    public Plant() {}

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public double getWeight() {
        return Settings.SPECIES.get("Plant").weight;
    }

    @Override
    public String getType() {
        return "Plant";
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void die() {
        this.alive = false;
    }

    @Override
    public String toString() {
        return "Растение";
    }
}
