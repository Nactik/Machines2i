package instance.model;

import instance.reseau.Client;
import instance.reseau.Point;


import java.util.Objects;

public class Demande {
    private final int id;
    private final int idMachine;
    private final int nbMachines;
    private final int firstDay;
    private final int lastDay;


    public Demande(int id,int firstDay, int lastDay, int idMachine,int nbMachines) {
        this.id = id;
        this.idMachine = idMachine;
        this.nbMachines = nbMachines;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public int getId() {
        return id;
    }

    public int getIdMachine() {
        return idMachine;
    }

    public int getFirstDay() {
        return firstDay;
    }

    public int getLastDay() {
        return lastDay;
    }

    public int getNbMachines() {
        return nbMachines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Demande)) return false;
        Demande demande = (Demande) o;
        return this.getId() == demande.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Demande {" +
                "id: " + this.getId()+
                ", idMachine: " + idMachine +
                ", nbMachines: " + nbMachines +
                ", firstDay: " + firstDay +
                ", lastDay: " + lastDay +
                '}';
    }
}
