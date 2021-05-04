package solution;

import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;

import java.util.ArrayList;
import java.util.List;

public abstract class Tournee {
    protected List<Demande> demandes;
    protected Entrepot entrepot;
    protected int day;
    protected int distance;

    public Tournee() {
        this.demandes = new ArrayList<>();
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

    public List<Demande> getDemandes() {
        return demandes;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public abstract int evalCost();

    public abstract boolean addDemand(Demande demand);
}
