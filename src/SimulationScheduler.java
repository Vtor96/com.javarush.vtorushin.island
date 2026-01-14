import entity.Animal;
import entity.island.Island;
import entity.island.Location;
import util.Settings;
import util.Fabric;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimulationScheduler {

    private static final int SCHEDULED_THREADS = 3;

    private static final int WORKER_THREADS = Runtime.getRuntime().availableProcessors();

    private static ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(SCHEDULED_THREADS);

    private static ThreadPoolExecutor workerPool =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(WORKER_THREADS);

    private static Island island;

    public static void start(Island island) {
        SimulationScheduler.island = island;

        Runnable growPlantsTask = () -> {
            for (int y = 0; y < Settings.ISLAND_HEIGHT; y++) {
                for (int x = 0; x < Settings.ISLAND_WIDTH; x++) {
                    Location loc = island.getLocation(x, y);
                    if (loc != null) {
                        Fabric.growPlants(loc);
                    }
                }
            }
        };

        Runnable animalLifecycleTask = () -> {
            for (int y = 0; y < Settings.ISLAND_HEIGHT; y++) {
                for (int x = 0; x < Settings.ISLAND_WIDTH; x++) {
                    final int fx = x;
                    final int fy = y;

                    workerPool.submit(() -> {
                        Location loc = island.getLocation(fx, fy);
                        if (loc == null) {
                            return;
                        }

                        ArrayList<Animal> animalsCopy = new ArrayList<>(loc.getAnimals());

                        for (Animal animal : animalsCopy) {
                            if (!animal.isAlive()) {
                                continue;
                            }

                            animal.decreaseSatiety();

                            animal.tryToEat();

                            Animal offspring = animal.reproduce();
                            if (offspring != null) {
                                loc.addAnimal(offspring);
                            }

                            animal.age++;

                            if (animal.age > 100) {
                                animal.die();
                            }

                            if (animal.isAlive()) {
                                animal.chooseDirectionAndMove(loc);
                            }
                        }

                        loc.removeDead();
                    });
                }
            }
        };

        Runnable statsTask = () -> {
            int totalPlants = 0;
            int totalAnimals = 0;
            int aliveAnimals = 0;

            for (int y = 0; y < Settings.ISLAND_HEIGHT; y++) {
                for (int x = 0; x < Settings.ISLAND_WIDTH; x++) {
                    Location loc = island.getLocation(x, y);
                    if (loc != null) {
                        totalPlants += loc.getPlants().size();
                        totalAnimals += loc.getAnimals().size();
                        aliveAnimals += (int) loc.getAnimals().stream()
                                .filter(Animal::isAlive)
                                .count();
                    }
                }
            }

            System.out.println("=== Статистика острова ===");
            System.out.println("Растения: " + totalPlants);
            System.out.println("Животные (всего): " + totalAnimals);
            System.out.println("Животные (живые): " + aliveAnimals);
            System.out.println("=========================");
        };

        scheduler.scheduleAtFixedRate(growPlantsTask, 0, 5, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(animalLifecycleTask, 1, 2, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(statsTask, 2, 5, TimeUnit.SECONDS);
    }

    public static void shutdown() {
        System.out.println("Остановка симуляции...");
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
