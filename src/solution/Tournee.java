package solution;

import instance.model.Demande;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.List;

public abstract class Tournee {
    protected int id;
    protected List<Client> clients;
    protected int cost;
    protected List<Demande> demandes;

    public Tournee() {
        this.clients = new ArrayList<>();
    }

    public Tournee(int id, List<Client> clients, int cost) {
        this.id = id;
        this.clients = clients;
        this.cost = cost;
    }

    public abstract int evalCost();

    public List<Demande> getDemandes() {
        return demandes;
    }
}
