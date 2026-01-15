package config;

public class SpeciesInfo {
    public double weight;
    public int maxCount;
    public int maxSpeed;
    public double foodRequired;

    public SpeciesInfo(double w, int mc, int spd, double req) {
        weight = w;
        maxCount = mc;
        maxSpeed = spd;
        foodRequired = req;
    }
}
