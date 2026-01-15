package service;

import config.Settings;
import entity.Animal;
import entity.island.Island;
import entity.island.Location;
import repository.Fabric;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;

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
                        try {
                            Location loc = island.getLocation(fx, fy);
                            if (loc == null) {
                                return;
                            }

                            ArrayList<Animal> animalsCopy = new ArrayList<>(loc.getAnimals());

                            for (Animal animal : animalsCopy) {
                                try {
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
                                } catch (Exception e) {
                                }
                            }

                            loc.removeDead();
                        } catch (Exception e) {
                        }
                    });
                }
            }
        };

        Runnable statsTask = () -> {
            try {
                int totalPlants = 0;
                int totalAnimals = 0;
                int aliveAnimals = 0;

                for (int y = 0; y < Settings.ISLAND_HEIGHT; y++) {
                    for (int x = 0; x < Settings.ISLAND_WIDTH; x++) {
                        try {
                            Location loc = island.getLocation(x, y);
                            if (loc != null) {
                                totalPlants += loc.getPlants().size();
                                totalAnimals += loc.getAnimals().size();
                                aliveAnimals += (int) loc.getAnimals().stream()
                                        .filter(Animal::isAlive)
                                        .count();
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                System.out.println("=== Статистика острова ===");
                System.out.println("Растения: " + totalPlants);
                System.out.println("Животные (всего): " + totalAnimals);
                System.out.println("Животные (живые): " + aliveAnimals);
                System.out.println("=========================");
            } catch (Exception e) {
            }
        };

        scheduler.scheduleAtFixedRate(growPlantsTask, 0, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(animalLifecycleTask, 1, 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(statsTask, 2, 5, TimeUnit.SECONDS);
    }

    public static void shutdown() {
        System.out.println("Остановка симуляции...");

        try (AutoCloseableExecutorService schedulerWrapper = new AutoCloseableExecutorService(scheduler, "Scheduler");
             AutoCloseableExecutorService workerPoolWrapper = new AutoCloseableExecutorService(workerPool, "Worker pool")) {
        } catch (Exception e) {
            System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
        }
        System.out.println("Симуляция остановлена.");
    }

    private static class AutoCloseableExecutorService implements AutoCloseable {
        private final ExecutorService executor;
        private final String name;

        public AutoCloseableExecutorService(ExecutorService executor, String name) {
            this.executor = executor;
            this.name = name;
        }

        @Override
        public void close() throws Exception {
            if (executor != null && !executor.isShutdown()) {
                try {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println(name + " не завершился вовремя");
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
    }
}
