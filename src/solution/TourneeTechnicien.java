package solution;

import instance.model.Technicien;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.List;

public class TourneeTechnicien extends Tournee {
    private List<Technicien> technicians;

    public TourneeTechnicien() {
        super();
        this.technicians = new ArrayList<>();
    }

    public TourneeTechnicien(int id, List<Client> clients, int cost, List<Technicien> technicians) {
        super(id, clients, cost);
        this.technicians = technicians;
    }

    @Override
    public int evalCost() {
        return 0;
    }
}
