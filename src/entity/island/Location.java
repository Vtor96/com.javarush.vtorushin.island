package entity.island;

import config.Settings;
import config.Species;
import entity.Animal;
import entity.Plant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Location {

    private final int coordX;
    private final int coordY;
    private final Island island;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();

    public Location(int coordX, int coordY, Island island) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.island = island;
    }

    public boolean addAnimal(Animal a) {
        if (a == null) {
            return false;
        }

        Species species = a.getSpecies();
        long countOfSameType = animals.stream()
                .filter(an -> an.isAlive() && an.getSpecies() == species)
                .count();

        if (countOfSameType < species.getMaxCount() &&
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

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public Island getIsland() {
        return island;
    }

    public void removeDead() {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal == null || !animal.isAlive()) {
                iterator.remove();
            }
        }
    }
}
