package solution;

import instance.Instance;
import instance.model.Camion;
import instance.model.Demande;
import instance.model.Machine;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;

import java.util.ArrayList;
import java.util.List;

public class TourneeCamion extends Tournee{

    private Camion camion;
    private int distance;
    private int capacity;
    private int maxCapacity;
    private int maxDistance;
    private List<Machine> listeMachine;
    private Entrepot entrepot;

    public TourneeCamion() {
        super();
        this.demandes = new ArrayList<>();
    }

    public TourneeCamion(Instance instance){
        super();
        this.demandes = new ArrayList<>();
        distance = 0;
        capacity = 0;
        listeMachine = instance.getMachines();
        entrepot = instance.getEntrepot();
        maxCapacity = instance.getTruckCapacity();
        maxDistance = instance.getDistMaxTruck();

    }
    public TourneeCamion(int id, List<Client> clients, int cost, List<Demande> demandes) {
        super(id, clients, cost);
        this.demandes = demandes;
    }
    public boolean possAjout(Demande demande){
        if(this.capacity + (getMachineSizeById(demande.getIdMachine())*demande.getNbMachines()) > this.maxCapacity){
            return false;
        }
        // TO DO :
        // Ajouter la vérification de distance
        return true;
    }
    public boolean ajouterDemandeClient(Demande demande){
        if(demande == null){
            return false;
        }
        if(!possAjout(demande)) {
            return false;
        }
        ajouterCoutTotal(demande);
        capacity += getMachineSizeById(demande.getIdMachine())*demande.getNbMachines();
        demandes.add(demande);
        System.out.println("yoyoy "+this.demandes);
        return true;

    }

    private boolean ajouterCoutTotal(Demande demande) {
        int distTemp = deltaDistInsertion(this.demandes.size(), demande);
        if(distTemp == Integer.MAX_VALUE)
            return false;
        distance += distTemp;
        return true;
    }

    private int deltaDistInsertion(int position, Demande demande) {
        int distInsertion = 0;
        if (!isPositionInsertionValide(position) || demande == null){
            return Integer.MAX_VALUE;
        }
        if (!demandes.isEmpty()){
            distInsertion-=getPrec(position).getCostTo(getCurrent(position));
        }
        distInsertion+=getPrec(position).getCostTo(getCurrent(position));
        distInsertion+=demande.getCostTo(getCurrent(position));
        return distInsertion;
    }
    private int getMachineSizeById(int id){
        return listeMachine.get(id-1).getSize();
    }
    private Point getCurrent(int position) {
        if(position == demandes.size()){
            return entrepot;
        }
        return demandes.get(position);
    }

    private Point getPrec(int position) {
        if (position == 0){
            return entrepot;
        }
        return demandes.get(position-1);
    }

    private boolean isPositionInsertionValide(int position) {
        if (position<0)
            return false;
        if (position>demandes.size())
            return false;
        if (capacity == maxCapacity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TourneeCamion{" +
                "demandes=" + demandes +
                ", cost=" + cost +
                ", camion=" + camion +
                ", distance=" + distance +
                ", capacity=" + capacity +
                ", maxCapacity=" + maxCapacity +
                ", maxDistance=" + maxDistance +
                ", entrepot=" + entrepot +
                '}';
    }

    @Override
    public int evalCost() {
        return 0;
    }
}
