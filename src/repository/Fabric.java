package repository;

import config.Settings;
import config.SpeciesInfo;
import entity.Animal;
import entity.Plant;
import entity.island.Island;
import entity.island.Location;
import util.AnimalFactory;
import util.Random;

public class Fabric {
    public static Island createIsland() {
        return new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
    }

    public static void initLocation(Location loc) {
        if (loc == null) {
            return;
        }

        try {
            SpeciesInfo plantInfo = Settings.SPECIES.get("Plant");
            if (plantInfo != null) {
                int maxPlants = plantInfo.maxCount;
                int minPlants = maxPlants / 3;
                int range = maxPlants - minPlants + 1;
                if (range > 0) {
                    int plantCount = minPlants + Random.nextInt(range);
                    for (int i = 0; i < plantCount; i++) {
                        loc.addPlant(new Plant());
                    }
                }
            }

            int herbivoreCount = 5 + Random.nextInt(4);
            for (int i = 0; i < herbivoreCount; i++) {
                Animal herb = AnimalFactory.randomHerbivore(loc);
                if (herb != null) {
                    loc.addAnimal(herb);
                }
            }

            int predatorCount = 2 + Random.nextInt(3);
            for (int i = 0; i < predatorCount; i++) {
                Animal pred = AnimalFactory.randomCarnivore(loc);
                if (pred != null) {
                    loc.addAnimal(pred);
                }
            }
        } catch (Exception e) {
        }
    }

    public static void initIsland(Island island) {
        if (island == null) {
            return;
        }

        System.out.println("Инициализация острова...");
        int totalLocations = 0;

        for (int y = 0; y < Settings.ISLAND_HEIGHT; y++) {
            for (int x = 0; x < Settings.ISLAND_WIDTH; x++) {
                Location loc = island.getLocation(x, y);
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

        try {
            int current = loc.getPlants().size();
            int maxPlants = Settings.MAX_PLANTS_PER_CELL;

            if (current < maxPlants * 0.8) {
                int toGrow = maxPlants - current;
                int newPlants = Math.min(1 + Random.nextInt(3), toGrow);
                for (int i = 0; i < newPlants; i++) {
                    loc.addPlant(new Plant());
                }
            }
        } catch (Exception e) {
        }
    }
}
