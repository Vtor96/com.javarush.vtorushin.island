import config.Species;
import entity.island.Island;
import entity.island.Location;
import entity.Animal;
import repository.Fabric;
import service.SimulationScheduler;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Симуляция острова ===");

        Island island = Fabric.createIsland();
        System.out.println("Остров создан: " +
                island.getWidth() + "x" + island.getHeight() + " клеток");

        Fabric.initIsland(island);

        try {
            Location first = island.getLocation(0, 0);
            if (first != null) {
                System.out.println("\nНачальное состояние локации (0,0):");
                printLocationSummary(first);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при выводе начального состояния: " + e.getMessage());
        }

        try {
            int totalAnimals = 0;
            int totalPlants = 0;
            for (int coordY = 0; coordY < island.getHeight(); coordY++) {
                for (int coordX = 0; coordX < island.getWidth(); coordX++) {
                    Location loc = island.getLocation(coordX, coordY);
                    if (loc != null) {
                        totalAnimals += loc.getAnimals().size();
                        totalPlants += loc.getPlants().size();
                    }
                }
            }
            System.out.println("\nОбщая статистика острова:");
            System.out.println("Всего животных: " + totalAnimals);
            System.out.println("Всего растений: " + totalPlants);
        } catch (Exception e) {
            System.err.println("Ошибка при подсчете статистики: " + e.getMessage());
        }

        System.out.println("\nЗапуск симуляции...");
        SimulationScheduler.start(island);

        try {
            System.out.println("Симуляция будет работать 30 секунд...");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Прервано ожидание");
        } finally {
            try {
                SimulationScheduler.shutdown();
            } catch (Exception e) {
                System.err.println("Ошибка при остановке симуляции: " + e.getMessage());
            }

            try {
                Location first = island.getLocation(0, 0);
                if (first != null) {
                    System.out.println("\nФинальное состояние локации (0,0):");
                    printLocationSummary(first);
                }
            } catch (Exception e) {
                System.err.println("Ошибка при выводе финального состояния: " + e.getMessage());
            }
        }
    }

    private static void printLocationSummary(Location location) {
        String plantSymbol = Species.PLANT.getEmoji();
        System.out.println("Растения " + plantSymbol + ": " + location.getPlants().size());
        System.out.println("Животные:");
        for (Animal a : location.getAnimals()) {
            if (!a.isAlive()) {
                continue;
            }
            Integer age = a.getAge();
            String ageStr = age != null ? String.valueOf(age) : "неизвестен";
            Double satiety = a.getSatiety();
            String satietyStr = satiety != null ? String.format("%.2f", satiety) : "неизвестно";
            String symbol = a.getSymbol();
            System.out.println("  " + symbol + " " + a.getType() +
                    " (возраст: " + ageStr + ", насыщение: " + satietyStr + ")");
        }
    }
}
