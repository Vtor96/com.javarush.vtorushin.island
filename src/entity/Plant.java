package entity;

import config.SpeciesInfo;
import config.Settings;
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
        try {
            SpeciesInfo plantInfo = Settings.SPECIES.get("Plant");
            if (plantInfo != null) {
                return plantInfo.weight;
            }
            return 1.0;
        } catch (Exception e) {
            return 1.0;
        }
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
