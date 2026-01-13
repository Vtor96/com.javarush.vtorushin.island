package util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Settings {
    public static final int ISLAND_WIDTH = 100;
    public static final int ISLAND_HEIGHT = 20;
    public static final int MAX_ANIMALS_IN_LOCATION = 2545;
    public static final int MAX_PLANTS_PER_CELL = 200;

    public static final String[] HERBIVORES = {
            "Horse", "Deer", "Rabbit", "Mouse", "Goat", "Sheep",
            "Boar", "Buffalo", "Duck", "Caterpillar"
    };

    public static final String[] CARNIVORES = {
            "Wolf", "Boa", "Fox", "Bear", "Eagle"
    };

    public static final String[] ALL_ANIMALS = {
            "Wolf", "Boa", "Fox", "Bear", "Eagle",
            "Horse", "Deer", "Rabbit", "Mouse", "Goat",
            "Sheep", "Boar", "Buffalo", "Duck", "Caterpillar"
    };

    public static final String[] PLANTS = {"Plant"};

    public static final Map<String, Map<String, Double>> EAT_PROBABILITIES;
    static {
        Map<String, Map<String, Double>> eat = new LinkedHashMap<>();

        eat.put("Wolf", Map.of(
                "Horse", 0.10, "Deer", 0.15, "Rabbit", 0.60, "Mouse", 0.80,
                "Goat", 0.60, "Sheep", 0.70, "Boar", 0.15, "Buffalo", 0.10, "Duck", 0.40
        ));
        eat.put("Boa", Map.of(
                "Fox", 0.15, "Rabbit", 0.20, "Mouse", 0.40, "Duck", 0.10
        ));
        eat.put("Fox", Map.of(
                "Rabbit", 0.70, "Mouse", 0.90, "Duck", 0.60, "Caterpillar", 0.40
        ));
        eat.put("Bear", Map.of(
                "Boa", 0.80, "Horse", 0.40, "Deer", 0.80, "Rabbit", 0.80,
                "Mouse", 0.90, "Goat", 0.70, "Sheep", 0.70, "Boar", 0.50,
                "Buffalo", 0.20, "Duck", 0.10
        ));
        eat.put("Eagle", Map.of(
                "Fox", 0.10, "Rabbit", 0.90, "Mouse", 0.90, "Duck", 0.80
        ));
        eat.put("Boar", Map.of(
                "Mouse", 0.50, "Caterpillar", 0.90
        ));
        eat.put("Duck", Map.of(
                "Caterpillar", 0.90
        ));
        eat.put("Horse", Map.of("Plant", 1.0));
        eat.put("Deer", Map.of("Plant", 1.0));
        eat.put("Rabbit", Map.of("Plant", 1.0));
        eat.put("Mouse", Map.of("Caterpillar", 0.90, "Plant", 1.0));
        eat.put("Goat", Map.of("Plant", 1.0));
        eat.put("Sheep", Map.of("Plant", 1.0));
        eat.put("Buffalo", Map.of("Plant", 1.0));
        eat.put("Caterpillar", Map.of("Plant", 1.0));
        eat.put("Boar", Map.of("Plant", 1.0, "Mouse", 0.50, "Caterpillar", 0.90));
        eat.put("Duck", Map.of("Plant", 1.0, "Caterpillar", 0.90));
        EAT_PROBABILITIES = Collections.unmodifiableMap(eat);
    }

    public static final Map<String, SpeciesInfo> SPECIES;
    static {
        Map<String, SpeciesInfo> map = new LinkedHashMap<>();
        map.put("Wolf",        new SpeciesInfo(50.0,   30,   3,    8.0));
        map.put("Boa",         new SpeciesInfo(15.0,   30,   1,    3.0));
        map.put("Fox",         new SpeciesInfo(8.0,    30,   2,    2.0));
        map.put("Bear",        new SpeciesInfo(500.0,  5,    2,   80.0));
        map.put("Eagle",       new SpeciesInfo(6.0,    20,   3,    1.0));
        map.put("Horse",       new SpeciesInfo(400.0,  20,   4,   60.0));
        map.put("Deer",        new SpeciesInfo(300.0,  20,   4,   50.0));
        map.put("Rabbit",      new SpeciesInfo(2.0,    150,  2,    0.45));
        map.put("Mouse",       new SpeciesInfo(0.05,   500,  1,    0.01));
        map.put("Goat",        new SpeciesInfo(60.0,   140,  3,   10.0));
        map.put("Sheep",       new SpeciesInfo(70.0,   140,  3,   15.0));
        map.put("Boar",        new SpeciesInfo(400.0,  50,   2,   50.0));
        map.put("Buffalo",     new SpeciesInfo(700.0,  10,   3,  100.0));
        map.put("Duck",        new SpeciesInfo(1.0,    200,  4,    0.15));
        map.put("Caterpillar", new SpeciesInfo(0.01,   1000, 0,    0.0));
        map.put("Plant",       new SpeciesInfo(1.0,    200,  0,    0.0));
        SPECIES = Collections.unmodifiableMap(map);
    }
}
