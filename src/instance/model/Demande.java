package instance.model;

import java.awt.*;
import java.util.Objects;

public class Demande {
    private int id;
    //private Client client;
    private int nbMachines;
    private int firstDay;
    private int lastDay;

    public Demande(int id, int nbMachines, int firstDay, int lastDay) {
        this.id = id;
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
