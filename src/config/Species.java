package config;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Species {
    WOLF("Wolf", "🐺", 50.0, 30, 3, 8.0),
    BOA("Boa", "🐍", 15.0, 30, 1, 3.0),
    FOX("Fox", "🦊", 8.0, 30, 2, 2.0),
    BEAR("Bear", "🐻", 500.0, 5, 2, 80.0),
    EAGLE("Eagle", "🦅", 6.0, 20, 3, 1.0),

    HORSE("Horse", "🐎", 400.0, 20, 4, 60.0),
    DEER("Deer", "🦌", 300.0, 20, 4, 50.0),
    RABBIT("Rabbit", "🐇", 2.0, 150, 2, 0.45),
    MOUSE("Mouse", "🐁", 0.05, 500, 1, 0.01),
    GOAT("Goat", "🐐", 60.0, 140, 3, 10.0),
    SHEEP("Sheep", "🐑", 70.0, 140, 3, 15.0),
    BOAR("Boar", "🐗", 400.0, 50, 2, 50.0),
    BUFFALO("Buffalo", "🐃", 700.0, 10, 3, 100.0),
    DUCK("Duck", "🦆", 1.0, 200, 4, 0.15),
    CATERPILLAR("Caterpillar", "🐛", 0.01, 1000, 0, 0.0),

    PLANT("Plant", "🌱", 1.0, 200, 0, 0.0);

    private final String displayName;
    private final String emoji;
    private final double weight;
    private final int maxCount;
    private final int maxSpeed;
    private final double foodRequired;

    private static final Map<String, Species> BY_NAME = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(Species::getDisplayName, Function.identity()));

    private static final Map<Species, Map<Species, Double>> DIET = buildDiet();
    private static final EnumSet<Species> HERBIVORES = EnumSet.of(
            HORSE, DEER, RABBIT, MOUSE, GOAT, SHEEP, BOAR, BUFFALO, DUCK, CATERPILLAR
    );
    private static final EnumSet<Species> CARNIVORES = EnumSet.of(WOLF, BOA, FOX, BEAR, EAGLE);

    Species(String displayName, String emoji, double weight, int maxCount, int maxSpeed, double foodRequired) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.weight = weight;
        this.maxCount = maxCount;
        this.maxSpeed = maxSpeed;
        this.foodRequired = foodRequired;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public double getFoodRequired() {
        return foodRequired;
    }

    public boolean isHerbivore() {
        return HERBIVORES.contains(this);
    }

    public boolean isCarnivore() {
        return CARNIVORES.contains(this);
    }

    public boolean isPlant() {
        return this == PLANT;
    }

    public Map<Species, Double> getDiet() {
        return DIET.getOrDefault(this, Map.of());
    }

    public static Species byName(String displayName) {
        return BY_NAME.get(displayName);
    }

    public static EnumSet<Species> herbivores() {
        return HERBIVORES.clone();
    }

    public static EnumSet<Species> carnivores() {
        return CARNIVORES.clone();
    }

    public static EnumSet<Species> animals() {
        return EnumSet.complementOf(EnumSet.of(PLANT));
    }

    public static Map<Species, Map<Species, Double>> diet() {
        return DIET;
    }

    private static Map<Species, Map<Species, Double>> buildDiet() {
        EnumMap<Species, Map<Species, Double>> eat = new EnumMap<>(Species.class);

        eat.put(WOLF, Map.of(
                HORSE, 0.10, DEER, 0.15, RABBIT, 0.60, MOUSE, 0.80,
                GOAT, 0.60, SHEEP, 0.70, BOAR, 0.15, BUFFALO, 0.10, DUCK, 0.40
        ));
        eat.put(BOA, Map.of(
                FOX, 0.15, RABBIT, 0.20, MOUSE, 0.40, DUCK, 0.10
        ));
        eat.put(FOX, Map.of(
                RABBIT, 0.70, MOUSE, 0.90, DUCK, 0.60, CATERPILLAR, 0.40
        ));
        eat.put(BEAR, Map.of(
                BOA, 0.80, HORSE, 0.40, DEER, 0.80, RABBIT, 0.80,
                MOUSE, 0.90, GOAT, 0.70, SHEEP, 0.70, BOAR, 0.50,
                BUFFALO, 0.20, DUCK, 0.10
        ));
        eat.put(EAGLE, Map.of(
                FOX, 0.10, RABBIT, 0.90, MOUSE, 0.90, DUCK, 0.80
        ));

        eat.put(HORSE, Map.of(PLANT, 1.0));
        eat.put(DEER, Map.of(PLANT, 1.0));
        eat.put(RABBIT, Map.of(PLANT, 1.0));
        eat.put(GOAT, Map.of(PLANT, 1.0));
        eat.put(SHEEP, Map.of(PLANT, 1.0));
        eat.put(BUFFALO, Map.of(PLANT, 1.0));
        eat.put(CATERPILLAR, Map.of(PLANT, 1.0));

        eat.put(MOUSE, Map.of(CATERPILLAR, 0.90, PLANT, 1.0));
        eat.put(BOAR, Map.of(PLANT, 1.0, MOUSE, 0.50, CATERPILLAR, 0.90));
        eat.put(DUCK, Map.of(PLANT, 1.0, CATERPILLAR, 0.90));

        return Collections.unmodifiableMap(eat);
    }
}
