package entity;

import config.Species;

public interface Eatable {
    double getWeight();
    Species getSpecies();
    default String getType() {
        Species species = getSpecies();
        return species != null ? species.getDisplayName() : "Unknown";
    }
    boolean isAlive();
    void die();
}
