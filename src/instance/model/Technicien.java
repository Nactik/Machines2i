package instance.model;

import instance.reseau.Point;

import java.util.Map;

public class Technicien {
    private int id;
    private Point localisation;
    private int distance;
    private final int maxDistance;
    private final int maxDemand;
    //Map<idMachine,CanInstall>
    private Map<Integer,Integer> canInstallMachine;

    public Technicien(int id, Point localisation, int distanceMax, int maxDemand,Map<Integer,Integer> canInstallMachine) {
        this.maxDistance = distanceMax;
        this.maxDemand = maxDemand;
        this.localisation = localisation;
        this.canInstallMachine = canInstallMachine;
    }

    public Technicien(int id, Point localisation, int distance, int maxDistance, int maxDemand,Map<Integer,Integer> canInstallMachine) {
        this(id, localisation, maxDistance, maxDemand,canInstallMachine);
        this.distance = distance;
    }
}
