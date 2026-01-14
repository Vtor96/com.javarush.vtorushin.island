package entity.island;

import entity.Animal;
import entity.Plant;
import util.Settings;
import util.SpeciesInfo;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private int x, y;
    private Island island;
    private List<Animal> animals = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();

    public Location(int x, int y, Island island) {
        this.x = x;
        this.y = y;
        this.island = island;
    }

    public boolean addAnimal(Animal a) {
        if (a == null) {
            return false;
        }

        String type = a.getType();
        SpeciesInfo speciesInfo = Settings.SPECIES.get(type);

        if (speciesInfo == null) {
            return false;
        }

        int maxForSpecies = speciesInfo.maxCount;

        long countOfSameType = animals.stream()
                .filter(an -> an.getType().equals(type) && an.isAlive())
                .count();

        if (countOfSameType < maxForSpecies &&
                animals.size() < Settings.MAX_ANIMALS_IN_LOCATION) {
            animals.add(a);
            return true;
        }

        return false;
    }

    public boolean addPlant(Plant p) {
        if (p == null) {
            return false;
        }

        if (plants.size() < Settings.MAX_PLANTS_PER_CELL) {
            plants.add(p);
            p.setLocation(this);
            return true;
        }

        return false;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Island getIsland() {
        return island;
    }

    public void removeDead() {
        animals.removeIf(animal -> !animal.isAlive());
        plants.removeIf(plant -> !plant.isAlive());
    }
}
