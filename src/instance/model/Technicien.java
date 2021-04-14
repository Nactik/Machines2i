package instance.model;

import instance.reseau.Point;

public class Technicien {
    private int id;
    private Point localisation;
    private int distance;
    private final int maxDistance;
    private final int maxDemand;
    //attribut map

    public Technicien(int id, int distanceMax, int maxDemand) {
        this.maxDistance = distanceMax;
        this.maxDemand = maxDemand;
    }

    public Technicien(int id, Point localisation, int distance, int maxDistance, int maxDemand) {
        this(id, maxDistance, maxDemand);
        this.localisation = localisation;
        this.distance = distance;
    }
}
