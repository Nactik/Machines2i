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

    public List<Demande> getDemandes() {
        return demandes;
    }

    public Entrepot getEntrepot() {
        return entrepot;
    }

    public abstract int evalCost();
    public abstract boolean addDemand(Demande demand);
}
