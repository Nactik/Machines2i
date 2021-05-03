package solution;

import instance.model.Demande;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.List;

public abstract class Tournee {
    protected int id;
    protected List<Demande> demandes;
    protected List<Client> clients;
    protected int cost;

    public Tournee() {
        this.clients = new ArrayList<>();
        this.demandes = new ArrayList<>();
    }

    public Tournee(int id, List<Client> clients, int cost) {
        this.id = id;
        this.clients = clients;
        this.cost = cost;
    }

    public abstract int evalCost();
}
