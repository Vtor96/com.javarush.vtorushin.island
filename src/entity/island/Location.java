package entity.island;

import entity.Animal;
import entity.Plant;
import util.Settings;

import java.util.*;

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
        if (a == null) return false;
        String type = a.getType();
        int max = Settings.SPECIES.get(a.getType()).maxCount;
        long cnt = animals.stream().filter(an -> an.getType().equals(type)).count();
        if (cnt < max && animals.size() < Settings.MAX_ANIMALS_IN_LOCATION) {
            animals.add(a);
            return true;
        }
        return false;
    }

    public boolean addPlant(Plant p) {
        if (p == null) return false;
        if (plants.size() < Settings.MAX_PLANTS_PER_CELL) {
            plants.add(p);
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
}
