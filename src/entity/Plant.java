package entity;

import entity.island.Location;
import util.Settings;

public class Plant implements Eatable {
    private Location location;

    public Plant(Location location) {
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
    public String toString() {
        return "Растение";
    }
}
