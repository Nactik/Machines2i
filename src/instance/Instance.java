package instance;

import instance.reseau.Client;
import instance.reseau.Entrepot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.LinkedHashMap;
public class Instance {
    private String name;
    private int nbDay;
    private int truckCapacity;
    private int distMaxTruck;
    private int truckDistCost;
    private int truckDayCost;
    private int truckCost;
    private int techDayCost;
    private int techDistCost;
    private int techCost;
    private Entrepot entrepot;
    private Map<Integer,Client> clients;


    public Instance(String name, int nbDay, int truckCapacity, int distMaxTruck, int truckDistCost,
                    int truckDayCost, int truckCost, int techDayCost, int techDistCost, int techCost,Entrepot entrepot) {
        this.name = name;
        this.nbDay = nbDay;
        this.truckCapacity = truckCapacity;
        this.distMaxTruck = distMaxTruck;
        this.truckDistCost = truckDistCost;
        this.truckDayCost = truckDayCost;
        this.truckCost = truckCost;
        this.techDayCost = techDayCost;
        this.techDistCost = techDistCost;
        this.techCost = techCost;
        this.entrepot = entrepot;
        this.clients = new LinkedHashMap<>();
    }

    public boolean addClient(Client clientToAdd){
        if(clientToAdd == null) return false;
        if(!clientToAdd.addRoute(this.entrepot)) return false;

        this.entrepot.addRoute(clientToAdd);

        this.clients.put(clientToAdd.getId(),clientToAdd);
        for(Client client: this.clients.values()) {
            if(!clientToAdd.addRoute(client)) return false;
            client.addRoute(clientToAdd);
        }

        return true;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "name='" + name + '\'' +
                ", nbDay=" + nbDay +
                ", truckCapacity=" + truckCapacity +
                ", distMaxTruck=" + distMaxTruck +
                ", truckDistCost=" + truckDistCost +
                ", truckDayCost=" + truckDayCost +
                ", truckCost=" + truckCost +
                ", techDayCost=" + techDayCost +
                ", techDistCost=" + techDistCost +
                ", techCost=" + techCost +
                ", entrepot=" + entrepot +
                ", clients=" + clients +
                '}';
    }
}
