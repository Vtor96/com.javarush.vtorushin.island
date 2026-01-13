import entity.island.Island;
import entity.island.Location;
import entity.Animal;
import util.Fabric;

public class Main {
    public static void main(String[] args) {
        Island island = Fabric.createIsland();

        Location first = island.getLocation(0, 0);

        Fabric.initLocation(first);

        System.out.println("На локации (0,0):");
        System.out.println("Растения: " + first.getPlants().size());
        System.out.println("Животные:");
        for (Animal a : first.getAnimals()) {
            System.out.println("  - " + a.getType());
        }
    }
}
