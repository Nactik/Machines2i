package instance.model;

import instance.reseau.Point;
import solution.TourneeTechnicien;


import java.util.*;

public class Technicien {
    private int id;
    private Point localisation;
    private final int maxDistance;
    private final int maxDemand;
    private List<Integer> machines;
    private Map<Integer, TourneeTechnicien> tourneesPerDay;

    public Technicien(int id, Point localisation, int distanceMax, int maxDemand, Map<Integer,Boolean> canInstallMachine) {
        this.id = id;
        this.maxDistance = distanceMax;
        this.maxDemand = maxDemand;
        this.localisation = localisation;
        this.machines = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> entry :canInstallMachine.entrySet()){
            if(entry.getValue())
                this.machines.add(entry.getKey());
        }
        this.tourneesPerDay = new HashMap<>();
    }

    public boolean addNewTournee(int day, TourneeTechnicien t){
        if(t == null) return false;

        this.tourneesPerDay.put(day, t);
        return true;
    }

    /**
     * Vérifie si le technicien est dispo pour un jour et une demande donnée
     * @param demand la demande a traiter
     * @param day le jour durant lequel la demande doit etre traiter
     * @return true si libre, false sinon
     */
    public boolean isAvailable(Demande demand, int day){
        TourneeTechnicien t = this.tourneesPerDay.get(day);
        if(t != null)
            return this.canHandleDemand(t,demand);
        return true;
    }

    /**
     * Vérifie si les demandes et les distances du techniciens sont ok pour traiter une demande donnée
     * @param tournee la tournée dans laquelle a insérer la demande
     * @param demand la demande a traiter
     * @return true si dispo, false sinon
     */
    private boolean canHandleDemand(TourneeTechnicien tournee,Demande demand){
        int nbMachinesTotal = 0;

        for(Demande d : tournee.getDemandes()){
            nbMachinesTotal += d.getNbMachines();
        }

        // TODO: changer, fonctionne pour une nouvelle tournée uniquement
        if(nbMachinesTotal + demand.getNbMachines() > this.maxDemand
            || tournee.getDistance() + tournee.getEntrepot().getDistTo(demand.getClient())  > this.maxDistance)
            return false;

        return true;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getMachines() {
        return machines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technicien that = (Technicien) o;
        return id == that.id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Technicien {" +
                "id: " + id +
                ", localisation: " + localisation +
                ", maxDistance: " + maxDistance +
                ", maxDemand: " + maxDemand +
                ", machines: " + machines +
                '}';
    }


}
