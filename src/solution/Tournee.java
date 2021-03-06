package solution;

import instance.model.Demande;
import instance.model.Technicien;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;
import operateur.FusionTournees;
import operateur.Operateur;

import java.util.LinkedList;
import java.util.List;

public abstract class Tournee {

    protected List<Demande> demandes;
    protected Entrepot entrepot;
    protected int day;
    protected int distance;

    public Tournee() {
        this.demandes = new LinkedList<>();
    }

    public Tournee(Entrepot entrepot, int day){
        this();
        this.entrepot = entrepot;
        this.day = day;
    }

    /**
     * Vérifie si la distance à correctement été calculée
     * @param startingPoint correspond au point de départ de chaque tournée. Pour un camion, il s'agit de l'entrepot,
     *                      pour un technicien, il s'agit de son domicile
     * @return la dist de la tournée
     */
    public int checkDist(Point startingPoint) {
        if(this.demandes.isEmpty()) return 0;

        int checkDist = 0;

        checkDist += startingPoint.getDistTo(this.demandes.get(0).getClient());
        for(int i=0; i<this.demandes.size()-1; i++){
            checkDist += this.demandes.get(i).getClient().getDistTo(this.demandes.get(i+1).getClient());
        }
        checkDist += this.demandes.get(this.demandes.size()-1).getClient().getDistTo(startingPoint);

        return checkDist;
    }

    /**
     * Vérifie si la position à laquelle insérée la demande est correcte
     * @param position à laquelle inserer la demande
     * @return true si ok, false sinon
     */
    protected boolean isPositionInsertionValide(int position) {
        if (position<0)
            return false;
        if (position>demandes.size())
            return false;
        return true;
    }

    /**
     * Calcul le cout de l'insertion de la demande à une position donnée
     * @param position à laquelle insérée la demande
     * @param demand la demande à insérer
     * @return le cout
     */
    public int deltaDistInsertion(int position, Demande demand) {
        if (!isPositionInsertionValide(position) || demand == null){
            return Integer.MAX_VALUE;
        }

        Client c = demand.getClient();
        Point prec = this.getPrec(position);
        Point current = this.getCurrent(position);

        //si les routes existent pas
        if(prec.getDistTo(c) == Integer.MAX_VALUE || c.getDistTo(current) == Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        //si prec == current -> il n'y a que le depot
        if(prec.equals(current))
            return prec.getDistTo(c) + c.getDistTo(current);

        //sinon on ajoute la route entre le prec et la demande, la demande et le futur suivant (current) et on suppr le prec vers le current
        return prec.getDistTo(c) + c.getDistTo(current) - prec.getDistTo(current);
    }

    /**
     * Maj le cout total de la tournée en fonction de la demnade passé en param
     * @param demand la demand a traiter
     * @return true si ok, false sinon
     */
    protected boolean majDistTotal(Demande demand) {
        //TODO : fonctionne que pour une insertion a la fin
        int distTemp = this.deltaDistInsertion(this.demandes.size(), demand);
        if(distTemp == Integer.MAX_VALUE)
            return false;
        this.distance += distTemp;
        return true;
    }


    /**
     * Réalise la fusion de deux tournées
     * @param infos les infos de la tournées a fusionner
     * @return true si ok, false sinon
     */
    public boolean doFusion(FusionTournees infos){
        if(infos == null) return false;

        if(this instanceof TourneeCamion){
            ((TourneeCamion) this).majCap(infos.getaFusionner().getDemCap());
            infos.getaFusionner().getDemandes().forEach(d -> d.setDeliveryDay(this.getDay()));
        } else {
            //TourneeTech donc maj les jours d'installation des demandes
            infos.getaFusionner().getDemandes().forEach(d -> d.setInstallationDay(this.getDay()));
            int day = infos.getaFusionner().getDay();
            Technicien technicien = ((TourneeTechnicien) infos.getaFusionner()).getTechnician();
            technicien.getTourneePerDay().remove(day);
        }

        this.demandes.addAll(infos.getaFusionner().getDemandes());
        this.distance += infos.getaFusionner().getDistance() + infos.getDeltaDist();

        return check();
    }

    /**
     * Donne le delta distance après une potientielle fusion
     * @param aFusionner les infos de la tournée a fusionner
     * @return true si ok, false sinon
     */
    public int deltaDistFusion(Tournee aFusionner){
        Point first = aFusionner.getCurrent(0);
        Point last = this.getCurrent(this.demandes.size()-1);

        int deltaDistFusion =  -last.getDistTo(this.getStartingPoint()) + last.getDistTo(first) - aFusionner.getStartingPoint().getDistTo(first);

        if(this.distance + deltaDistFusion + aFusionner.getDistance() > this.getMaxDist())
            return Integer.MAX_VALUE;

        return deltaDistFusion;
    }

    public List<Demande> getDemandes() {
        return demandes;
    }

    public int getDistance() {
        return distance;
    }

    public int getDay() {
        return day;
    }

    protected abstract Point getPrec(int position);

    protected abstract Point getCurrent(int position);

    public abstract boolean addDemand(Demande demand);

    public abstract int getMaxDemCap();

    public abstract int getDemCap();

    public abstract int getMaxDist();

    public abstract Point getStartingPoint();

    public abstract boolean check();
}
