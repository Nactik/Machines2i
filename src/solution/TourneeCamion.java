package solution;

import instance.model.Camion;
import instance.reseau.Client;

import java.util.ArrayList;
import java.util.List;

public class TourneeCamion extends Tournee{
    private List<Camion> trucks;

    public TourneeCamion() {
        super();
        this.trucks = new ArrayList<>();
    }

    public TourneeCamion(int id, List<Client> clients, int cost, List<Camion> trucks) {
        super(id, clients, cost);
        this.trucks = trucks;
    }

    @Override
    public int evalCost() {
        return 0;
    }
}
