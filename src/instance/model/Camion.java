package instance.model;

import instance.reseau.Point;

public class Camion {
    private int id;
    private int distance;
    private int capacity;
    private final int maxDistance;
    private final int dayCost;
    private final int maxCost;

    public Camion(int id, int maxDistance, int dayCost, int maxCost) {
        this.id = id;
        this.maxDistance = maxDistance;
        this.dayCost = dayCost;
        this.maxCost = maxCost;
    }

    public Camion(int id, int distance, int capacity, int maxDistance, int dayCost, int maxCost) {
        this(id, maxDistance, dayCost, maxCost);
        this.distance = distance;
        this.capacity = capacity;
    }
}
