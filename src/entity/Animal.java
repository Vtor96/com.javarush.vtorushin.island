package entity;

import config.Species;
import entity.island.Location;
import util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Animal implements Eatable {
    private static final double EFFICIENCY = 0.5;

    private final Species species;
    private Location location;
    private final double baseWeight;
    private Integer age = 0;
    private boolean alive = true;
    private Double satiety;

    protected Animal(Location location, Species species) {
        this.location = location;
        this.species = species;
        this.baseWeight = species.getWeight();
        this.satiety = 1.0;
    }

    @Override
    public Species getSpecies() {
        return species;
    }

    public String getType() {
        return getSpecies().getDisplayName();
    }

    public String getSymbol() {
        return getSpecies().getEmoji();
    }

    public Location getLocation() {
        if (location == null) {
            throw new IllegalStateException("Животное не размещено на острове");
        }
        return location;
    }

    public void move(Location newLocation) {
        if (newLocation == null || !isAlive() || newLocation == this.location) {
            return;
        }

        if (newLocation.addAnimal(this)) {
            if (this.location != null) {
                this.location.removeAnimal(this);
            }
            this.location = newLocation;
        }
    }

    public void chooseDirectionAndMove(Location currentLocation) {
        if (!isAlive() || currentLocation == null) {
            return;
        }

        int maxSpeed = getSpecies().getMaxSpeed();
        if (maxSpeed <= 0) {
            return;
        }

        int range = 2 * maxSpeed + 1;
        int deltaX = Random.nextInt(range) - maxSpeed;
        int deltaY = Random.nextInt(range) - maxSpeed;

        int newCoordX = currentLocation.getCoordX() + deltaX;
        int newCoordY = currentLocation.getCoordY() + deltaY;
        Location newLocation = currentLocation.getIsland().getLocation(newCoordX, newCoordY);

        if (newLocation != null) {
            move(newLocation);
        }
    }

    public boolean eat(Eatable food) {
        if (food == null || !food.isAlive() || food == this) {
            return false;
        }

        Map<Species, Double> probabilities = getSpecies().getDiet();
        Double probability = probabilities.get(food.getSpecies());
        if (probability == null || probability <= 0) {
            return false;
        }

        if (Random.nextDouble() < probability) {
            double foodWeight = food.getWeight();
            double foodRequired = getSpecies().getFoodRequired();
            if (foodRequired > 0) {
                satiety = Math.min(1.0, satiety + (foodWeight / foodRequired));
            } else {
                satiety = Math.min(1.0, satiety + 0.1);
            }

            food.die();

            return true;
        }
        return false;
    }

    public boolean tryToEat() {
        if (!isAlive() || location == null || satiety >= 1.0) {
            return false;
        }

        Map<Species, Double> probabilities = getSpecies().getDiet();
        boolean ateSomething = false;

        List<Animal> animals = location.getLivingAnimals();
        for (Animal potentialFood : animals) {
            if (potentialFood == null || !potentialFood.isAlive() || potentialFood == this) {
                continue;
            }

            Species foodSpecies = potentialFood.getSpecies();
            if (probabilities.containsKey(foodSpecies)) {
                if (eat(potentialFood)) {
                    ateSomething = true;
                    if (satiety >= 0.3) {
                        break;
                    }
                }
            }
        }

        if (satiety < 1.0 && probabilities.containsKey(Species.PLANT)) {
            List<Plant> plants = location.getLivingPlants();
            for (Plant plant : plants) {
                if (plant != null && plant.isAlive() && eat(plant)) {
                    ateSomething = true;
                    if (satiety >= 0.8) {
                        break;
                    }
                }
            }
        }

        return ateSomething;
    }

    public Animal reproduce() {
        if (!isAlive() || location == null || satiety < 0.3) {
            return null;
        }

        if (location.isOvercrowded()) {
            return null;
        }

        Species mySpecies = getSpecies();
        int sameTypeCount = 0;

        for (Animal animal : location.getAnimals()) {
            if (animal.isAlive() && animal.getSpecies() == mySpecies) {
                sameTypeCount++;
                if (sameTypeCount >= mySpecies.getMaxCount()) {
                    return null;
                }
            }
        }

        double reproductionChance = 0.3 * satiety;
        if (Random.nextDouble() < reproductionChance) {
            try {
                Class<? extends Animal> animalClass = this.getClass();
                Animal offspring = animalClass.getDeclaredConstructor(Location.class).newInstance(location);
                satiety *= 0.7;
                return offspring;
            } catch (Exception e) {
                System.err.println("Ошибка при создании потомства для " + mySpecies.getDisplayName() + ": " +
                        e.getClass().getSimpleName() + " - " + e.getMessage());
                return null;
            }
        }

        return null;
    }

    public void decreaseSatiety() {
        if (!isAlive()) {
            return;
        }

        double foodRequired = getSpecies().getFoodRequired();
        double hungerRate = (foodRequired / 1000.0) * 0.15;
        satiety -= hungerRate;
        satiety = Math.max(0.0, Math.min(1.0, satiety));

        if (satiety <= 0) {
            die();
        } else if (satiety < 0.1) {
            if (Random.nextDouble() < 0.4) {
                die();
            }
        } else if (satiety < 0.3) {
            if (Random.nextDouble() < 0.1) {
                die();
            }
        }
    }

    public void die() {
        this.alive = false;
        if (location != null) {
            location.removeAnimal(this);
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public double getWeight() {
        return baseWeight;
    }

    public Double getSatiety() {
        return satiety;
    }

    public Integer getAge() {
        return age;
    }

    public void incrementAge() {
        if (age == null) {
            age = 1;
        } else {
            age++;
        }
    }
}
