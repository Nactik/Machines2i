package instance.model;

import instance.reseau.Client;

import java.awt.*;
import java.util.Objects;

public class Demande {
    private int id;
    private Client client;
    private final int nbMachines;
    private final int firstDay;
    private final int lastDay;

    public Demande(int id, int nbMachines, int firstDay, int lastDay, Client client) {
        this.id = id;
        this.nbMachines = nbMachines;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.client = client;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Demande)) return false;
        Demande demande = (Demande) o;
        return id == demande.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
