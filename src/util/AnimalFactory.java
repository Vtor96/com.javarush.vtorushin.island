package util;

import entity.Animal;
import entity.island.Location;
import entity.herbivore.*;
import entity.predator.*;

import java.util.Arrays;
import java.util.List;

public class AnimalFactory {
    private static final List<Class<? extends Animal>> herbivoreTypes = Arrays.asList(
            Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class,
            Sheep.class, Boar.class, Buffalo.class, Duck.class, Caterpillar.class
    );

    private static final List<Class<? extends Animal>> carnivoreTypes = Arrays.asList(
            Wolf.class, Boa.class, Fox.class, Bear.class, Eagle.class
    );

    public static Animal randomHerbivore(Location l) {
        try {
            Class<? extends Animal> c = herbivoreTypes.get(Random.nextInt(herbivoreTypes.size()));
            return c.getDeclaredConstructor(Location.class).newInstance(l);
        } catch (Exception e) {
            return null;
        }
    }

    public static Animal randomCarnivore(Location l) {
        try {
            Class<? extends Animal> c = carnivoreTypes.get(Random.nextInt(carnivoreTypes.size()));
            return c.getDeclaredConstructor(Location.class).newInstance(l);
        } catch (Exception e) {
            return null;
        }
    }
}
