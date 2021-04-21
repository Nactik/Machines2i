package instance.reseau;

import instance.model.Demande;

import java.util.List;
import java.util.Objects;

public class Client extends Point{
    private List<Demande> demandes;

    public Client(Integer id, int x, int y, List<Demande> demandes) {
        super(id, x, y);
        this.demandes = demandes;
    }
    public Client(Integer id, int x, int y) {
        super(id, x, y);
    }
    
    public boolean addDemande(Demande demandeToAdd){
        demandes.add(demandeToAdd);
        return true;
    }
    public boolean deleteDemande(Demande demandeToDelete){
        demandes.remove(demandeToDelete);
        return true;
    }
    public List<Demande> getDemandes() {
        return demandes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return this.getId() == client.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Client {" +
                "demande=" + demandes + " " +
                super.toString() +
                " }";
    }
}
