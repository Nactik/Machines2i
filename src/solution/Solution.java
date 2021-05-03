package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Solution {
    private Instance instance;
    private int truckDistance;
    private int numberOfTruckDays;
    private int numberOfTruckUsed;
    private int technicianDistance;
    private int numberOfTechnicianDays;
    private int numberOfTechnicianUsed;
    private int idleMachineCost;
    private int totalCost;
    private HashMap<Integer, LinkedList<Tournee>> days;

    private Solution(){
        this.instance = null;
        this.truckDistance = 0;
        this.numberOfTruckDays = 0;
        this.numberOfTruckUsed = 0;
        this.technicianDistance = 0;
        this.numberOfTechnicianDays = 0;
        this.numberOfTechnicianUsed = 0;
        this.idleMachineCost = 0;
        this.totalCost = 0;
        this.days = new HashMap<>();
    }

    public Solution(Instance instance) {
        this();
        this.instance = instance;
    }

    public Solution(Solution solution){
        this.instance = solution.instance;
        this.truckDistance = solution.truckDistance;
        this.numberOfTruckDays = solution.numberOfTruckDays;
        this.numberOfTruckUsed = solution.numberOfTruckUsed;
        this.technicianDistance = solution.technicianDistance;
        this.numberOfTechnicianDays = solution.numberOfTechnicianDays;
        this.numberOfTechnicianUsed = solution.numberOfTechnicianUsed;
        this.idleMachineCost = solution.idleMachineCost;
        this.totalCost = solution.totalCost;
        this.days = new HashMap<>(solution.days);
    }

    /**
     * Ajoute une demande à une nouvelle tournée de camion
     * @param demand
     */
    public void ajoutDemandNewTournee(Demande demand) {
        TourneeCamion tourneeCamion = new TourneeCamion(this.instance);
        tourneeCamion.ajouteDemandeClient(demand);
        this.addTourneeToMap(tourneeCamion, demand.getFirstDay());
    }

    /**
     * Ajoute une demande a une nouvelle tournée technicien
     * @param demand la demande a ajouter
     */
    public boolean addDemandNewTourneeTech(Demande demand){
        if(demand == null) return false;

        Technicien tech = null;

//        for(HashMap.Entry<Integer, Technicien> entry : instance.getTechnicians().entrySet()){
//
//        }

        TourneeTechnicien tourneeTech = new TourneeTechnicien();
        if(tourneeTech.addDemand(demand)){
            this.addTourneeToMap(tourneeTech, demand.getFirstDay();
            return true;
        }
        return false;
    }

    /**
     * Ajoute une tournée a la solution, en fonction du jour d'éxectution de celle-ci
     * @param tournee la tournée à ajouter
     * @param jour le jour de la tournée
     */
    private void addTourneeToMap(Tournee tournee, int jour){
        if (!days.containsKey(jour)){
            days.put(jour, new LinkedList<>());
        }
        days.get(jour).add(tournee);
    }

    @Override
    public String toString() {
        String string= "Solution{" +
                "instance=" + instance +
                ", truckDistance=" + truckDistance +
                ", numberOfTruckDays=" + numberOfTruckDays +
                ", numberOfTruckUsed=" + numberOfTruckUsed +
                ", technicianDistance=" + technicianDistance +
                ", numberOfTechnicianDays=" + numberOfTechnicianDays +
                ", numberOfTechnicianUsed=" + numberOfTechnicianUsed +
                ", idleMachineCost=" + idleMachineCost +
                ", totalCost=" + totalCost +
                "\n\t days :";
                for(Map.Entry<Integer,LinkedList<Tournee>> entry : days.entrySet()){
                    string += "\n\t\t day : "+entry.getKey()+" = "+entry.getValue().toString();
                }
                string+='}';
        return string;
    }
}
