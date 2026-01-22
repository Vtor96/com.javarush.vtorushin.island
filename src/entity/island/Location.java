package entity.island;

import config.Species;
import entity.Animal;
import entity.Plant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Location {

    private final int coordX;
    private final int coordY;
    private final Island island;
    private final List<Animal> animals = Collections.synchronizedList(new ArrayList<>());
    private final List<Plant> plants = Collections.synchronizedList(new ArrayList<>());

    public Location(int coordX, int coordY, Island island) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.island = island;
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

    public synchronized List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }

    public synchronized List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    public synchronized List<Animal> getLivingAnimals() {
        return animals.stream()
                .filter(Animal::isAlive)
                .collect(Collectors.toList());
    }

    public synchronized List<Plant> getLivingPlants() {
        return plants.stream()
                .filter(Plant::isAlive)
                .collect(Collectors.toList());
    }

    public synchronized boolean addAnimal(Animal animal) {
        if (animal == null || !animal.isAlive()) {
            return false;
        }

        Species species = animal.getSpecies();
        long currentCount = animals.stream()
                .filter(a -> a.isAlive() && a.getSpecies() == species)
                .count();

        if (currentCount >= species.getMaxCount()) {
            return false;
        }

        animals.add(animal);
        return true;
    }

    public synchronized void removeAnimal(Animal animal) {
        if (animal != null) {
            animals.remove(animal);
        }
    }

    public synchronized boolean addPlant(Plant plant) {
        if (plant == null || !plant.isAlive()) {
            return false;
        }

        if (plants.size() >= Species.PLANT.getMaxCount()) {
            return false;
        }

        plants.add(plant);
        return true;
    }

    public synchronized void removeDead() {
        animals.removeIf(animal -> !animal.isAlive());
        plants.removeIf(plant -> !plant.isAlive());
    }

    public synchronized boolean isOvercrowded() {
        for (Species species : Species.animals()) {
            long count = animals.stream()
                    .filter(animal -> animal.isAlive() && animal.getSpecies() == species)
                    .count();
            if (count >= species.getMaxCount()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Location[%d,%d]", coordX, coordY);
    }
}
