package instance;

import instance.model.Machine;
import instance.model.Technicien;
import instance.reseau.Client;
import instance.reseau.Entrepot;

import java.util.*;

public class Instance {
    private String dataset;
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
    private Map<Integer, Technicien> technicians;
    private List<Machine> machines;


    public  Instance(String dataset,String name){
        this.dataset = dataset;
        this.name = name;
        this.nbDay = -1;
        this.truckCapacity = -1;
        this.distMaxTruck = -1;
        this.truckDistCost = -1;
        this.truckDayCost = -1;
        this.truckCost = -1;
        this.techDayCost = -1;
        this.techDistCost = -1;
        this.techCost = -1;
        this.entrepot = new Entrepot(0,0,0);
        this.clients = new LinkedHashMap<>();
        this.technicians = new LinkedHashMap<>();
        this.machines = new LinkedList<Machine>();
    }

    public Instance(String dataset,String name, int nbDay, int truckCapacity, int distMaxTruck, int truckDistCost,
                    int truckDayCost, int truckCost, int techDayCost, int techDistCost, int techCost, Entrepot entrepot, List<Machine> machines) {
        this.dataset = dataset;
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
        this.technicians = new LinkedHashMap<>();
        this.machines = machines;
    }

    /**
     * Ajoute un client ?? l'instance
     * @param clientToAdd le client a traiter
     * @return true si ok, false sinon
     */
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

    /**
     * Ajoute un technicien ?? l'instance et cr??e les routes entre le domicile et les clients
     * @param techToAdd le technicien ?? traiter
     * @return true si ok, false sinon
     */
    public boolean addTechnician(Technicien techToAdd){
        if(techToAdd == null) return false;

        this.technicians.put(techToAdd.getId(), techToAdd);
        for(Client c: this.clients.values()) {
            if(!techToAdd.getDomicile().addRoute(c)) return false;
            c.addRoute(techToAdd.getDomicile());
        }

        return true;
    }


    public Map<Integer, Technicien> getTechnicians() {
        return technicians;
    }

    public Map<Integer, Client> getClients() {
        return clients;
    }

    public String getName() {
        return name;
    }

    public String getDataset() {
        return dataset;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public int getDistMaxTruck() {
        return distMaxTruck;
    }

    public int getTruckDistCost() {
        return truckDistCost;
    }

    public int getTruckDayCost() {
        return truckDayCost;
    }

    public int getTruckCost() {
        return truckCost;
    }

    public int getNbDay() {
        return nbDay;
    }

    public int getTechDayCost() {
        return techDayCost;
    }

    public int getTechDistCost() {
        return techDistCost;
    }

    public int getTechCost() {
        return techCost;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    @Override
    public String toString() {
        return "Instance {" +
                "\n\tdataSet: " + dataset +
                ",\n\tname: " + name +
                ",\n\tnbDay: " + nbDay +
                ",\n\ttruckCapacity: " + truckCapacity +
                ",\n\tdistMaxTruck: " + distMaxTruck +
                ",\n\ttruckDistCost: " + truckDistCost +
                ",\n\ttruckDayCost: " + truckDayCost +
                ",\n\ttruckCost: " + truckCost +
                ",\n\ttechDayCost: " + techDayCost +
                ",\n\ttechDistCost: " + techDistCost +
                ",\n\ttechCost: " + techCost +
                ",\n\tentrepot: " + entrepot.toString() +
                ",\n\tclients: " + clients.toString() +
                ",\n\ttechniciens: " + technicians.toString() +
                ",\n\tmachines: " + machines.toString() +
                "\n}";
    }
}
