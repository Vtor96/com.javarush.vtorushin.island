package entity;

import entity.island.Location;
import util.Settings;

import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Animal implements Eatable {
    protected Location location;
    protected double weight;
    public int age;
    protected boolean alive = true;
    protected double satiety;
    protected static final Random random = new Random();

    public Animal(Location location) {
        this.location = location;
        this.age = 0;
        this.weight = Settings.SPECIES.get(getType()).weight;
        this.satiety = 1.0;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public double getWeight() {
        return Settings.SPECIES.get(getType()).weight;
    }

    public abstract String getType();

    public void move(Location newLocation) {
        if (newLocation == null || !isAlive()) {
            return;
        }

        if (newLocation == this.location) {
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

        int maxSpeed = Settings.SPECIES.get(getType()).maxSpeed;
        if (maxSpeed == 0) {
            return;
        }

        int dx = random.nextInt(2 * maxSpeed + 1) - maxSpeed;
        int dy = random.nextInt(2 * maxSpeed + 1) - maxSpeed;

        int newX = currentLocation.getX() + dx;
        int newY = currentLocation.getY() + dy;
        Location newLocation = currentLocation.getIsland().getLocation(newX, newY);

        if (newLocation != null) {
            move(newLocation);
        }
    }

    public boolean eat(Eatable food) {
        if (food == null || !food.isAlive() || food == this) {
            return false;
        }

        String animalType = getType();
        String foodType = food.getType();

        Map<String, Double> probabilities = Settings.EAT_PROBABILITIES.get(animalType);
        if (probabilities == null) {
            return false;
        }

        Double probability = probabilities.get(foodType);
        if (probability == null || probability <= 0) {
            return false;
        }

        if (random.nextDouble() < probability) {
            double foodWeight = food.getWeight();
            double foodRequired = Settings.SPECIES.get(animalType).foodRequired;
            satiety = Math.min(1.0, satiety + (foodWeight / foodRequired));

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
        if (!isAlive() || location == null) {
            return false;
        }

        if (satiety >= 1.0) {
            return false; // Уже сыт
        }

        String animalType = getType();
        Map<String, Double> probabilities = Settings.EAT_PROBABILITIES.get(animalType);
        if (probabilities == null) {
            return false;
        }

        List<Animal> animals = location.getAnimals();
        for (Animal potentialFood : new java.util.ArrayList<>(animals)) {
            if (potentialFood != this && potentialFood.isAlive()) {
                String foodType = potentialFood.getType();
                if (probabilities.containsKey(foodType)) {
                    if (eat(potentialFood)) {
                        return true;
                    }
                }
            }
        }

        List<Plant> plants = location.getPlants();
        if (probabilities.containsKey("Plant")) {
            for (Plant plant : new java.util.ArrayList<>(plants)) {
                if (plant.isAlive()) {
                    if (eat(plant)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Animal reproduce() {
        if (!isAlive() || location == null || satiety < 0.5) {
            return null;
        }

        String myType = getType();
        int sameTypeCount = 0;

        for (Animal animal : location.getAnimals()) {
            if (animal != this && animal.getType().equals(myType) && animal.isAlive()) {
                sameTypeCount++;
            }
        }

        if (sameTypeCount < 1) {
            return null;
        }

        if (random.nextDouble() < 0.5) {
            try {
                Class<? extends Animal> animalClass = this.getClass();
                Animal offspring = animalClass.getDeclaredConstructor(Location.class).newInstance(location);

                satiety *= 0.5;

                return offspring;
            } catch (Exception e) {
                return null;
            }
        }

        return null;
    }

    public void decreaseSatiety() {
        if (isAlive()) {
            double foodRequired = Settings.SPECIES.get(getType()).foodRequired;
            satiety -= (foodRequired / 100.0) * 0.1;
            if (satiety < 0) {
                satiety = 0;
            }

            if (satiety <= 0) {
                die();
            }
        }
    }

    public void die() {
        this.alive = false;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public Location getLocation() {
        return location;
    }

    public double getSatiety() {
        return satiety;
    }
}
