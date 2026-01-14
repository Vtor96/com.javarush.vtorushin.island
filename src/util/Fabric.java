package util;

import entity.Animal;
import entity.Plant;
import entity.island.Island;
import entity.island.Location;

import java.util.Random;

public class Fabric {
    private static final Random random = new Random();

    public static Island createIsland() {
        return new Island(Settings.ISLAND_WIDTH, Settings.ISLAND_HEIGHT);
    }

    public static void initLocation(Location loc) {
        int maxPlants = Settings.SPECIES.get("Plant").maxCount;
        int plantCount = random.nextInt(maxPlants + 1);
        for (int i = 0; i < plantCount; i++) {
            loc.addPlant(new Plant());
        }

        for (int i = 0; i < 3; i++) {
            Animal herb = AnimalFactory.randomHerbivore(loc);
            loc.addAnimal(herb);
        }
        for (int i = 0; i < 2; i++) {
            Animal pred = AnimalFactory.randomCarnivore(loc);
            loc.addAnimal(pred);
        }
    }

    public static void growPlants(Location loc) {
        int current = loc.getPlants().size();
        int toGrow = Settings.MAX_PLANTS_PER_CELL - current;
        if (toGrow > 0) {
            for (int i = 0; i < toGrow; i++) {
                loc.addPlant(new Plant());
            }
        }
    }
}
