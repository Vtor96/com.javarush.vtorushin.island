package service;

import config.Settings;
import config.Species;
import entity.Animal;
import entity.island.Island;
import entity.island.Location;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.*;

public class SimulationScheduler {

    private static final int SCHEDULED_THREADS = 3;
    private static final int WORKER_THREADS = Runtime.getRuntime().availableProcessors();

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(SCHEDULED_THREADS);
    private static final ThreadPoolExecutor workerPool =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(WORKER_THREADS);

    private static Island island;
    private static volatile boolean simulationRunning = true;

    public static void start(Island island) {
        SimulationScheduler.island = island;
        simulationRunning = true;

        Runnable animalLifecycleTask = () -> {
            for (int coordY = 0; coordY < Settings.ISLAND_HEIGHT; coordY++) {
                for (int coordX = 0; coordX < Settings.ISLAND_WIDTH; coordX++) {
                    final int finalCoordX = coordX;
                    final int finalCoordY = coordY;

                    workerPool.submit(() -> {
                        Location loc = island.getLocation(finalCoordX, finalCoordY);
                        if (loc != null) {
                            processLocation(loc);
                        }
                    });
                }
            }
        };

        Runnable statsTask = () -> {
                if (!simulationRunning) {
                    return;
                }

            calculateAndPrintStats();
        };

        scheduler.scheduleWithFixedDelay(animalLifecycleTask, 1, 2, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(statsTask, 6, 5, TimeUnit.SECONDS);
    }

    private static void processLocation(Location loc) {
        ArrayList<Animal> animalsCopy = new ArrayList<>(loc.getAnimals());
        for (Animal animal : animalsCopy) {
            if (animal == null || !animal.isAlive()) {
                continue;
            }

            animal.decreaseSatiety();
            animal.tryToEat();
            Animal offspring = animal.reproduce();
            if (offspring != null) loc.addAnimal(offspring);
            animal.incrementAge();

            if (animal.getAge() != null && animal.getAge() > 100) {
                animal.die();
            }

            if (animal.isAlive()) {
                animal.chooseDirectionAndMove(loc);
            }
        }
        loc.removeDead();
    }

    private static void calculateAndPrintStats() {
        Map<Species, Integer> animalsByType = new EnumMap<>(Species.class);
        Map<Species, Integer> aliveAnimalsByType = new EnumMap<>(Species.class);

        for (Species species : Species.animals()) {
            animalsByType.put(species, 0);
            aliveAnimalsByType.put(species, 0);
        }

        int totalPlants = 0;
        int totalAnimals = 0;
        int aliveAnimals = 0;

        for (int coordY = 0; coordY < Settings.ISLAND_HEIGHT; coordY++) {
            for (int coordX = 0; coordX < Settings.ISLAND_WIDTH; coordX++) {
                Location loc = island.getLocation(coordX, coordY);
                if (loc != null) {
                    totalPlants += loc.getPlants().size();

                    for (Animal animal : loc.getAnimals()) {
                        if (animal == null) continue;
                        Species species = animal.getSpecies();
                        animalsByType.put(species, animalsByType.getOrDefault(species, 0) + 1);
                        totalAnimals++;

                        if (animal.isAlive()) {
                            aliveAnimalsByType.put(species, aliveAnimalsByType.getOrDefault(species, 0) + 1);
                            aliveAnimals++;
                        }
                    }
                }
            }
        }

        printStats(animalsByType, aliveAnimalsByType, totalPlants, totalAnimals, aliveAnimals);
    }

    private static void printStats(Map<Species, Integer> animalsByType,
                                   Map<Species, Integer> aliveAnimalsByType,
                                   int totalPlants,
                                   int totalAnimals,
                                   int aliveAnimals) {
        String divider = "+-----------------------------------------------------+";
            System.out.println("\n+" + divider);
            System.out.println("|                      СТАТИСТИКА ОСТРОВА             |");
            System.out.println(divider);

            System.out.printf("| Растения %s: %5d%34s%n", Species.PLANT.getEmoji(), totalPlants, "|");
            System.out.println(divider);

            System.out.println("| ХИЩНИКИ:                                            |");
            printGroup(Species.carnivores(), animalsByType, aliveAnimalsByType);
            System.out.println(divider);

            System.out.println("| ТРАВОЯДНЫЕ:                                         |");
            printGroup(Species.herbivores(), animalsByType, aliveAnimalsByType);
            System.out.println(divider);

            System.out.printf("| Животных всего: %5d%31s%n", totalAnimals, "|");
            System.out.printf("| Животных живых: %5d%30s%n", aliveAnimals, "|");
            System.out.printf("| Растений:      %5d%32s%n", totalPlants, "|");
            System.out.println(divider);

            System.out.flush();
    }

    private static void printGroup(Iterable<Species> group,
                                   Map<Species, Integer> totals,
                                   Map<Species, Integer> alive) {
        for (Species species : group) {
            int total = totals.getOrDefault(species, 0);
            int aliveCount = alive.getOrDefault(species, 0);
            if (total == 0 && aliveCount == 0) {
                continue;
            }
            System.out.printf("|   %s %-12s: всего=%4d, живых=%4d%12s%n",
                    species.getEmoji(),
                    species.getDisplayName(),
                    total,
                    aliveCount,
                    "|");
        }
    }

    public static void shutdown() {
        System.out.println("Остановка симуляции...");
        simulationRunning = false;

        scheduler.shutdownNow();
        workerPool.shutdownNow();

        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Scheduler не завершился вовремя");
            }
            if (!workerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Worker pool не завершился вовремя");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Симуляция остановлена.");
    }
}
