package entity;

import config.Species;
import entity.island.Location;
import util.Random;

import java.util.List;
import java.util.Map;

public abstract class Animal implements Eatable {
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

    public void move(Location newLocation) {
        if (newLocation == null || !isAlive() || newLocation == this.location) {
            return;
        }

        if (newLocation.addAnimal(this)) {
            if (this.location != null) {
                this.location.getAnimals().remove(this);
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

            if (food instanceof Plant) {
                location.getPlants().remove(food);
            } else if (food instanceof Animal) {
                location.getAnimals().remove(food);
            }

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

        List<Animal> animals = location.getAnimals();
        for (Animal potentialFood : new java.util.ArrayList<>(animals)) {
            if (potentialFood != this && potentialFood.isAlive()) {
                if (probabilities.containsKey(potentialFood.getSpecies())) {
                    if (eat(potentialFood)) {
                        ateSomething = true;
                        if (satiety < 0.3 && satiety < 1.0) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        if (satiety < 1.0 && probabilities.containsKey(Species.PLANT)) {
            List<Plant> plants = location.getPlants();
            for (Plant plant : new java.util.ArrayList<>(plants)) {
                if (plant.isAlive() && eat(plant)) {
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

        Species mySpecies = getSpecies();
        long sameTypeCount = location.getAnimals().stream()
                .filter(animal -> animal.isAlive() && animal.getSpecies() == mySpecies)
                .count();

        if (sameTypeCount < 2) {
            return null;
        }

        double reproductionChance = 0.3 * satiety;
        if (Random.nextDouble() < reproductionChance) {
            try {
                Class<? extends Animal> animalClass = this.getClass();
                Animal offspring = animalClass.getDeclaredConstructor(Location.class).newInstance(location);
                satiety *= 0.7;
                return offspring;
            } catch (Exception e) {
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
        satiety -= (foodRequired / 1000.0) * 0.05;
        if (satiety < 0) {
            satiety = 0.0;
        }

        if (satiety <= 0) {
            die();
        }
    }

    public void die() {
        this.alive = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public double getWeight() {
        return baseWeight;
    }

    public Location getLocation() {
        return location;
    }

    public Double getSatiety() {
        return satiety;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void incrementAge() {
        if (age == null) {
            age = 1;
        } else {
            age++;
        }
    }
}
