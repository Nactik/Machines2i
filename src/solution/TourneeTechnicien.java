package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;

public class TourneeTechnicien extends Tournee {

    private Technicien technician;

    public TourneeTechnicien() {
        super();
    }

    public TourneeTechnicien(Instance instance, Technicien technician, int day){
        super(instance.getEntrepot(), day);
        this.technician = technician;
        technician.addNewTournee(day,this);
    }

    @Override
    public boolean addDemand(Demande demand){
        int deliveryDay = demand.getFirstDay();
        int installationDay = deliveryDay + 1;

        if(!this.technician.isAvailable(demand, installationDay))
            return false;

        // TODO: changer, fonctionne pour une nouvelle tournée uniquement
        this.distance = this.entrepot.getDistTo(demand.getClient());
        this.demandes.add(demand);

        return true;
    }

    public int getDistance(){
        return distance;
    }

    @Override
    public int evalCost() {
        return 0;
    }

    public Technicien getTechnician() {
        return technician;
    }
}