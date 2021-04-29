package solution;

import instance.model.Demande;
import instance.model.Technicien;
import instance.reseau.Client;

import java.util.List;

public class TourneeTechnicien extends Tournee {
    private Technicien technician;

    public TourneeTechnicien() {
        super();
    }

    public TourneeTechnicien(int id, List<Client> clients, int cost, Technicien technician) {
        super(id, clients, cost);
        this.technician = technician;
    }

    @Override
    public int evalCost() {
        return 0;
    }

    public Technicien getTechnician() {
        return technician;
    }

}
