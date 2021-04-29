package solution;

import instance.Instance;
import instance.model.Camion;
import instance.model.Demande;
import instance.model.Machine;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TourneeCamion extends Tournee{
    private Camion camion;
    private int distance;
    private int capacity;
    private int maxCapacity;
    private int maxDistance;
    private List<Machine> listeMachine;

    public TourneeCamion() {
        super();
        this.demandes = new LinkedList<>();
        this.listeMachine = new LinkedList<>();
    }
    public TourneeCamion(Instance instance){
        distance = 0;
        capacity = instance.getTruckCapacity();
        listeMachine = instance.getMachines();
        this.demandes = new LinkedList<>();
    }
    public TourneeCamion(int id, List<Client> clients, int cost, List<Demande> demandes) {
        super(id, clients, cost);
        this.demandes = demandes;
    }
    /*public boolean possAjout(Demande demande){
        if (this.capacity + listeMachine.get(0));
    }*/
    public boolean ajouterDemandeClient(Demande demande){
        if(demande == null){
            return false;
        }
        /*if(!possAjout(demande)) {
            return false;
        }*/
        return true;
    }

    public Camion getCamion() {
        return camion;
    }

    @Override
    public int evalCost() {
        return 0;
    }
}
