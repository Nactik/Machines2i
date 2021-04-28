package instance.model;

import instance.reseau.Point;

import java.util.Map;
import java.util.Objects;

public class Technicien {
    private int id;
    private Point localisation;
    private int distance;
    private final int maxDistance;
    private final int maxDemand;
    private Map<Integer,Boolean> canInstallMachine;

    public Technicien(int id, Point localisation, int distanceMax, int maxDemand,Map<Integer,Boolean> canInstallMachine) {
        this.id = id;
        this.maxDistance = distanceMax;
        this.maxDemand = maxDemand;
        this.localisation = localisation;
        this.canInstallMachine = canInstallMachine;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technicien that = (Technicien) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Technicien {" +
                "id: " + id +
                ", localisation: " + localisation +
                ", distance: " + distance +
                ", maxDistance: " + maxDistance +
                ", maxDemand: " + maxDemand +
                ", canInstallMachine: " + canInstallMachine +
                '}';
    }


}
