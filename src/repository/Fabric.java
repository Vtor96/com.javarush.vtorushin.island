package repository;

import config.Settings;
import config.Species;
import entity.Animal;
import entity.Plant;
import entity.herbivore.*;
import entity.island.Island;
import entity.island.Location;
import entity.predator.*;
import util.Random;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Fabric {

    public static Island createIsland() {
        return new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
    }

    public static void initLocation(Location loc) {
        if (loc == null) {
            return;
        }

        seedPlants(loc);
        seedAnimals(loc, Species.herbivores(), 5, 3);
        seedAnimals(loc, Species.carnivores(), 2, 2);
    }

    public static void initIsland(Island island) {
        if (island == null) {
            return;
        }

        System.out.println("Инициализация острова...");
        int totalLocations = 0;

        for (int coordY = 0; coordY < Settings.ISLAND_HEIGHT; coordY++) {
            for (int coordX = 0; coordX < Settings.ISLAND_WIDTH; coordX++) {
                Location loc = island.getLocation(coordX, coordY);
                if (loc != null) {
                    initLocation(loc);
                    totalLocations++;
                }
            }
        }

        System.out.println("Инициализировано локаций: " + totalLocations);
    }

    public static void growPlants(Location loc) {
        if (loc == null) {
            return;
        }

        int current = loc.getPlants().size();
        int maxPlants = Settings.MAX_PLANTS_PER_CELL;

        if (current < maxPlants * 0.8) {
            int toGrow = maxPlants - current;
            int newPlants = Math.min(1 + Random.nextInt(3), toGrow);
            for (int i = 0; i < newPlants; i++) {
                loc.addPlant(new Plant());
            }
        }
    }

    private static void seedPlants(Location loc) {
        int maxPlants = Species.PLANT.getMaxCount();
        int minPlants = maxPlants / 3;
        int range = maxPlants - minPlants + 1;
        if (range > 0) {
            int plantCount = minPlants + Random.nextInt(range);
            for (int i = 0; i < plantCount; i++) {
                loc.addPlant(new Plant());
            }
        }
    }

    private static void seedAnimals(Location loc, EnumSet<Species> pool, int base, int variance) {
        int count = base + Random.nextInt(variance + 1);
        List<Species> speciesList = new ArrayList<>(pool);

        for (Species species : speciesList) {
            Animal animal = createAnimal(species, loc);
            if (animal != null) {
                loc.addAnimal(animal);
            }
        }

        for (int i = speciesList.size(); i < count; i++) {
            Species species = speciesList.get(Random.nextInt(speciesList.size()));
            Animal animal = createAnimal(species, loc);
            if (animal != null) {
                loc.addAnimal(animal);
            }
        }
    }

    private static Animal createAnimal(Species species, Location loc) {
        return switch (species) {
            case HORSE -> new Horse(loc);
            case DEER -> new Deer(loc);
            case RABBIT -> new Rabbit(loc);
            case MOUSE -> new Mouse(loc);
            case GOAT -> new Goat(loc);
            case SHEEP -> new Sheep(loc);
            case BOAR -> new Boar(loc);
            case BUFFALO -> new Buffalo(loc);
            case DUCK -> new Duck(loc);
            case CATERPILLAR -> new Caterpillar(loc);
            case WOLF -> new Wolf(loc);
            case BOA -> new Boa(loc);
            case FOX -> new Fox(loc);
            case BEAR -> new Bear(loc);
            case EAGLE -> new Eagle(loc);
            default -> null;
        };
    }
}
