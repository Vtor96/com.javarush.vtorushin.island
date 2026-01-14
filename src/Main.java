import entity.island.Island;
import entity.island.Location;
import entity.Animal;
import util.Fabric;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Симуляция острова ===");

        Island island = Fabric.createIsland();
        System.out.println("Остров создан: " +
                island.getWidth() + "x" + island.getHeight() + " клеток");

        Fabric.initIsland(island);

        Location first = island.getLocation(0, 0);
        System.out.println("\nНачальное состояние локации (0,0):");
        System.out.println("Растения: " + first.getPlants().size());
        System.out.println("Животные:");
        for (Animal a : first.getAnimals()) {
            System.out.println("  - " + a.getType() +
                    " (возраст: " + a.age + ", живое: " + a.isAlive() + ")");
        }

        int totalAnimals = 0;
        int totalPlants = 0;
        for (int y = 0; y < island.getHeight(); y++) {
            for (int x = 0; x < island.getWidth(); x++) {
                Location loc = island.getLocation(x, y);
                if (loc != null) {
                    totalAnimals += loc.getAnimals().size();
                    totalPlants += loc.getPlants().size();
                }
            }
        }
        System.out.println("\nОбщая статистика острова:");
        System.out.println("Всего животных: " + totalAnimals);
        System.out.println("Всего растений: " + totalPlants);

        System.out.println("\nЗапуск симуляции...");
        SimulationScheduler.start(island);

        try {
            System.out.println("Симуляция будет работать 30 секунд...");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Прервано ожидание");
        } finally {
            SimulationScheduler.shutdown();

            System.out.println("\nФинальное состояние локации (0,0):");
            System.out.println("Растения: " + first.getPlants().size());
            System.out.println("Животные:");
            for (Animal a : first.getAnimals()) {
                if (a.isAlive()) {
                    System.out.println("  - " + a.getType() +
                            " (возраст: " + a.age + ", насыщение: " +
                            String.format("%.2f", a.getSatiety()) + ")");
                }
            }
        }
    }
}
