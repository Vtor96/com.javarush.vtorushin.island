package util;

import entity.Animal;
import entity.herbivore.*;
import entity.island.Location;
import entity.predator.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AnimalFactory {
    private static final Random random = new Random();

    private static final List<Class<? extends Animal>> herbivoreTypes = Arrays.asList(
            Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class,
            Sheep.class, Boar.class, Buffalo.class, Duck.class, Caterpillar.class
    );

    private static final List<Class<? extends Animal>> carnivoreTypes = Arrays.asList(
            Wolf.class, Boa.class, Fox.class, Bear.class, Eagle.class
    );

    public static Animal randomHerbivore(Location l) {
        try {
            Class<? extends Animal> c = herbivoreTypes.get(random.nextInt(herbivoreTypes.size()));
            return c.getDeclaredConstructor(Location.class).newInstance(l);
        } catch (Exception e) {
            return null;
        }
    }

    public static Animal randomCarnivore(Location l) {
        try {
            Class<? extends Animal> c = carnivoreTypes.get(random.nextInt(carnivoreTypes.size()));
            return c.getDeclaredConstructor(Location.class).newInstance(l);
        } catch (Exception e) {
            return null;
        }
    }
}
