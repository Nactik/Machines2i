package instance.model;

import instance.reseau.Client;

import java.awt.*;
import java.util.Objects;

public class Demande {
    private int id;
    private final int idMachine;
    private final int nbMachines;
    private final int firstDay;
    private final int lastDay;

    public Demande(int id, int firstDay, int lastDay, int idMachine,int nbMachines) {
        this.id = id;
        this.idMachine = idMachine;
        this.nbMachines = nbMachines;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
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
