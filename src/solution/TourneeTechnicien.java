package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;
import instance.reseau.Point;

public class TourneeTechnicien extends Tournee {

    private Technicien technician;

    public TourneeTechnicien() {
        super();
    }

    public TourneeTechnicien(Instance instance, Technicien technician, int day){
        super(instance.getEntrepot(), day);
        this.technician = technician;
        technician.addNewTournee(day,this);
    }

    /**
     * Ajoute une demande au technicien
     * @param demand la demande a traiter
     * @return true si ok, false sinon
     */
    @Override
    public boolean addDemand(Demande demand){
        if(demand == null){
            return false;
        }

        if(!this.majDistTotal(demand)) //maj la distance
            return false;

        this.demandes.add(demand);

        return this.check();
    }

    /**
     * Appel le check du technicien de la tournée pour vérifier si tout est ok
     * @return true si tout est ok, false sinon
     */
    @Override
    public boolean check() {
        return this.technician.check();
    }

    /**
     * Récupère le client précédent a une position donnée dans la liste
     * Si la liste est vide ou que la position est 0, on renvoie le domicile du technician
     * @param position la position
     * @return le point correspondant à la position
     */
    @Override
    protected Point getPrec(int position) {
        if (position == 0 || this.demandes.size() == 0){
            return this.technician.getDomicile();
        }
        return this.demandes.get(position-1).getClient();
    }

    /**
     * Récupère la position du client a une position du donnée dans la liste
     * Retourne le domicile du technicien si on demande une position égale a la taille de la liste
     * @param position la position
     * @return le point correspondant au client
     */
    @Override
    protected Point getCurrent(int position) {
        if(position == this.demandes.size()){
            return this.technician.getDomicile();
        }
        return demandes.get(position).getClient();
    }

    @Override
    public int getMaxDemCap() {
        return this.technician.getMaxDemand();
    }

    @Override
    public int getDemCap() {
        return this.demandes.size();
    }

    @Override
    public String toString() {
        return "TourneeTechnicien{" +
                "demandes=" + demandes +
                ", entrepot=" + entrepot +
                ", day=" + day +
                ", distance=" + distance +
                ", technician=" + technician +
                '}';
    }

    public int getDistance(){
        return distance;
    }

    public Technicien getTechnician() {
        return technician;
    }

    @Override
    public int getMaxDist() {
        return this.getTechnician().getMaxDistance();
    }
}