package solution;

import instance.Instance;
import instance.model.Camion;
import instance.model.Demande;
import instance.model.Machine;
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

    /**
     * Ajoute une demande à la tournée
     * @param demand la demande à ajouter à la tournée
     * @return true si OK, false sinon
     */
    public boolean ajouteDemandeClient(Demande demand){
        if(demand == null){
            return false;
        }
        if(!this.isInsertionValide(this.demandes.size(), demand)) {
            return false;
        }

        if(!this.majDistTotal(demand)) return false; //maj la distance
        this.capacity += this.getMachineSizeById(demand.getIdMachine())*demand.getNbMachines(); //maj la capacite
        this.demandes.add(demand); //maj les demandes

        return true;
    }

    /**
     * Maj le cout total de la tournée en fonction de la demnade passé en param
     * @param demand la demand a traiter
     * @return true si ok, false sinon
     */
    private boolean majDistTotal(Demande demand) {
        int distTemp = deltaDistInsertion(this.demandes.size(), demand);
        if(distTemp == Integer.MAX_VALUE)
            return false;
        this.distance += distTemp;
        return true;
    }

    /**
     * Calcul le cout de l'insertion de la demande à une position donnée
     * @param position à laquelle insérée la demande
     * @param demand la demande à insérer
     * @return le cout
     */
    private int deltaDistInsertion(int position, Demande demand) {
        if (!isPositionInsertionValide(position) || demand == null){
            return Integer.MAX_VALUE;
        }

        Point prec = this.getPrec(position);
        Point current = this.getCurrent(position);

        //si les routes existent pas
        if(prec.getCostTo(demand) == Integer.MAX_VALUE || demand.getCostTo(current) == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        //si prec == current -> il n'y a que le depot
        if(prec.equals(current))
            return prec.getCostTo(demand) + demand.getCostTo(prec);

        //sinon on ajoute la route entre le prec et la demande, la demande et le futur suivant (current) et on suppr le prec vers le current
        return prec.getCostTo(demand) + demand.getCostTo(current) - prec.getCostTo(current);
    }

    /**
     * Récupère la taille de la machine grace a son type (son id)
     * @param id ou type de la machine
     * @return la taille de la machine
     */
    private int getMachineSizeById(int id){
        return listeMachine.get(id-1).getSize();
    }

    /**
     * Donne la localisation de la demande à la position avant de celle donnée.
     * Si la position est égale à 0, on renvoie l'entrepot
     * @param position position à laquelle récupéré la localisation de la demande précédente
     * @return la précédente localisation de la demande ou l'entrepot
     */
    private Point getPrec(int position) {
        if (position == 0 || this.demandes.size() == 0){
            return entrepot;
        }
        return demandes.get(position-1);
    }

    /**
     * Donne la localisation de la demande à la position donnée.
     * Si la position est égale à la taille de la liste des demandes,
     * on renvoie l'entrepot
     * @param position position à laquelle récupéré la localisation de la demande
     * @return la localisation de la demande ou l'entrepot
     */
    private Point getCurrent(int position) {
        if(position == this.demandes.size()){
            return this.entrepot;
        }
        return demandes.get(position);
    }

    /**
     * Vérifie si la position à laquelle insérée la demande est correcte
     * @param position à laquelle inserer la demande
     * @return true si ok, false sinon
     */
    private boolean isPositionInsertionValide(int position) {
        if (position<0)
            return false;
        if (position>demandes.size())
            return false;
        if (capacity >= maxCapacity)
            return false;
        return true;
    }

    /**
     * Vérifie si l'ajout est possible dans la tournée en cours
     * @param position la position à laquelle ajouter la demande
     * @param demand la demande à ajouter à la tournée
     * @return
     */
    public boolean isInsertionValide(int position, Demande demand){
        if(this.capacity + (getMachineSizeById(demand.getIdMachine())*demand.getNbMachines()) > this.maxCapacity)
            return false;
        if(this.distance + this.deltaDistInsertion(position, demand) > this.maxDistance)
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
