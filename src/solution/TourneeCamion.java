package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Machine;
import instance.reseau.Point;
import operateur.Operateur;

import java.util.LinkedList;
import java.util.List;

public class TourneeCamion extends Tournee{

    private int capacity;
    private int maxDistance;
    private int maxCapacity;
    private List<Machine> listeMachine;

    public TourneeCamion() {
        super();
        this.listeMachine = new LinkedList<>();
    }

    public TourneeCamion(Instance instance, int day){
        super(instance.getEntrepot(), day);
        distance = 0;
        capacity = 0;
        listeMachine = instance.getMachines();
        maxCapacity = instance.getTruckCapacity();
        maxDistance = instance.getDistMaxTruck();
    }

    /**
     * Ajoute une demande à la tournée
     * @param demand la demande à ajouter à la tournée
     * @return true si OK, false sinon
     */
    @Override
    public boolean addDemand(Demande demand){
        if(demand == null){
            return false;
        }
        if(!this.isInsertionValide(this.demandes.size(), demand)) { //ajout a la fin car solution triviale
            return false;
        }

        if(!this.majDistTotal(demand)) return false; //maj la distance
        this.capacity += this.getMachineSizeById(demand.getIdMachine())*demand.getNbMachines(); //maj la capacite
        this.demandes.add(demand); //maj les demandes

        return this.check();
    }

    /**
     * Récupère la taille de la machine grace a son type (son id)
     * @param id ou type de la machine
     * @return la taille de la machine
     */
    private int getMachineSizeById(int id){
        //marche seulement si la liste est dans le meme ordre
        return listeMachine.get(id-1).getSize();
    }

    /**
     * Donne la localisation de la demande à la position avant de celle donnée.
     * Si la position est égale à 0, on renvoie l'entrepot
     * @param position position à laquelle récupéré la localisation de la demande précédente
     * @return la précédente localisation de la demande ou l'entrepot
     */
    @Override
    protected Point getPrec(int position) {
        if (position == 0 || this.demandes.size() == 0){
            return entrepot;
        }
        return this.demandes.get(position-1).getClient();
    }

    /**
     * Donne la localisation de la demande à la position donnée.
     * Si la position est égale à la taille de la liste des demandes,
     * on renvoie l'entrepot
     * @param position position à laquelle récupéré la localisation de la demande
     * @return la localisation de la demande ou l'entrepot
     */
    @Override
    protected Point getCurrent(int position) {
        if(position == this.demandes.size()){
            return this.entrepot;
        }
        return demandes.get(position).getClient();
    }

    /**
     * Vérifie si l'ajout est possible dans la tournée en cours
     * @param position la position à laquelle ajouter la demande
     * @param demand la demande à ajouter à la tournée
     * @return true si ok, false sinon
     */
    public boolean isInsertionValide(int position, Demande demand){
        if(this.capacity + (getMachineSizeById(demand.getIdMachine())*demand.getNbMachines()) > this.maxCapacity)
            return false;
        if(this.distance + this.deltaDistInsertion(position, demand) > this.maxDistance)
            return false;
        return true;
    }

    /**
     * Maj la capacité du camion avec la nouvelle capacité
     * @param capacityToAdd la capacité a ajouter
     */
    public void majCap(int capacityToAdd){
        this.capacity += capacityToAdd;
    }

    /**
     * Check gloable de la tournée
     * @return true si la tournée est correcte, false sinon
     */
    @Override
    public boolean check() {
        int checkCapacity = checkCapacity();
        int checkDist = checkDist(this.entrepot);

        return checkCapacity == this.capacity && checkCapacity <= this.maxCapacity
                && checkDist == this.distance && checkDist <= this.maxDistance;
    }

    /**
     * Vérifie si la capacité à correctement été calculée
     * @return la capacité de la tournée
     */
    private int checkCapacity() {
        int checkCapacity = 0;

        for(Demande d: this.demandes){
            checkCapacity += getMachineSizeById(d.getIdMachine())*d.getNbMachines();
        }

        return checkCapacity;
    }

    @Override
    public int getMaxDemCap() {
        return this.maxCapacity;
    }

    @Override
    public int getDemCap() {
        return this.capacity;
    }

    @Override
    public String toString() {
        return "TourneeCamion{" +
                "demandes=" + demandes +
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
