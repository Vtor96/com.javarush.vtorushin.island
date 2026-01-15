package entity;

import config.Settings;
import config.SpeciesInfo;
import entity.island.Location;
import util.Random;

import java.util.List;
import java.util.Map;

public abstract class Animal implements Eatable {
    protected Location location;
    protected double weight;
    public int age;
    protected boolean alive = true;
    protected double satiety;

    public Animal(Location location) {
        this.location = location;
        this.age = 0;
        try {
            SpeciesInfo speciesInfo = Settings.SPECIES.get(getType());
            if (speciesInfo != null) {
                this.weight = speciesInfo.weight;
            } else {
                this.weight = 1.0;
            }
        } catch (Exception e) {
            this.weight = 1.0;
        }
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
        try {
            SpeciesInfo speciesInfo = Settings.SPECIES.get(getType());
            if (speciesInfo != null) {
                return speciesInfo.weight;
            }
            return 1.0;
        } catch (Exception e) {
            return 1.0;
        }
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

        try {
            SpeciesInfo speciesInfo = Settings.SPECIES.get(getType());
            if (speciesInfo == null) {
                return;
            }

            int maxSpeed = speciesInfo.maxSpeed;
            if (maxSpeed == 0) {
                return;
            }

            int range = 2 * maxSpeed + 1;
            if (range <= 0) {
                return;
            }

            int dx = Random.nextInt(range) - maxSpeed;
            int dy = Random.nextInt(range) - maxSpeed;

            int newX = currentLocation.getX() + dx;
            int newY = currentLocation.getY() + dy;
            Location newLocation = currentLocation.getIsland().getLocation(newX, newY);

            if (newLocation != null) {
                move(newLocation);
            }
        } catch (Exception e) {
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

        try {
            if (Random.nextDouble() < probability) {
                // Увеличиваем насыщение
                double foodWeight = food.getWeight();
                SpeciesInfo speciesInfo = Settings.SPECIES.get(animalType);
                if (speciesInfo == null) {
                    return false;
                }

                double foodRequired = speciesInfo.foodRequired;
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
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean tryToEat() {
        if (!isAlive() || location == null) {
            return false;
        }

        if (satiety >= 1.0) {
            return false;
        }

        try {
            String animalType = getType();
            Map<String, Double> probabilities = Settings.EAT_PROBABILITIES.get(animalType);
            if (probabilities == null) {
                return false;
            }

            boolean ateSomething = false;

            List<Animal> animals = location.getAnimals();
            for (Animal potentialFood : new java.util.ArrayList<>(animals)) {
                if (potentialFood != this && potentialFood.isAlive()) {
                    String foodType = potentialFood.getType();
                    if (probabilities.containsKey(foodType)) {
                        if (eat(potentialFood)) {
                            ateSomething = true;
                            if (satiety < 0.3 && satiety < 1.0) {
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            if (satiety < 1.0) {
                List<Plant> plants = location.getPlants();
                if (probabilities.containsKey("Plant")) {
                    for (Plant plant : new java.util.ArrayList<>(plants)) {
                        if (plant.isAlive()) {
                            if (eat(plant)) {
                                ateSomething = true;
                                if (satiety >= 0.8) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            return ateSomething;
        } catch (Exception e) {
            return false;
        }
    }

    public Animal reproduce() {
        if (!isAlive() || location == null || satiety < 0.3) {
            return null;
        }

        String myType = getType();
        int sameTypeCount = 0;

        for (Animal animal : location.getAnimals()) {
            if (animal.getType().equals(myType) && animal.isAlive()) {
                sameTypeCount++;
            }
        }

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

        try {
            SpeciesInfo speciesInfo = Settings.SPECIES.get(getType());
            if (speciesInfo == null) {
                return;
            }

            double foodRequired = speciesInfo.foodRequired;
            satiety -= (foodRequired / 1000.0) * 0.05;
            if (satiety < 0) {
                satiety = 0;
            }

            if (satiety <= 0) {
                die();
            }
        } catch (Exception e) {
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
