package solution;

import instance.Instance;
import instance.model.Camion;
import instance.model.Demande;
import instance.model.Machine;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.List;

public class TourneeCamion extends Tournee{
    private List<Demande> demandes;
    private Camion camion;
    private int distance;
    private int capacity;
    private int maxCapacity;
    private int maxDistance;
    private List<Machine> listeMachine;
    public TourneeCamion() {
        super();
        this.demandes = new ArrayList<>();
    }
    public TourneeCamion(Instance instance){
        distance = 0;
        capacity = instance.getTruckCapacity();
        listeMachine = instance.getMachines();

    }
    public TourneeCamion(int id, List<Client> clients, int cost, List<Demande> demandes) {
        super(id, clients, cost);
        this.demandes = demandes;
    }
    public boolean possAjout(Demande demande){
        if (this.capacity + listeMachine.get(0));
    }
    public boolean ajouterDemandeClient(Demande demande){
        if(demande == null){
            return false;
        }
        if(!possAjout(demande)) {
            return false;
        }
    }
    @Override
    public int evalCost() {
        return 0;
    }
}
