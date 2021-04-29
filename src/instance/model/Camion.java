package instance.model;

import instance.reseau.Point;

import java.util.Objects;

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


    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Camion camion = (Camion) o;
        return id == camion.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
