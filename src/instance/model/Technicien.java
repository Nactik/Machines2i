package instance.model;

import instance.reseau.Point;
import solution.TourneeTechnicien;


import java.util.*;

public class Technicien {
    private int id;
    private Point domicile;
    private final int maxDistance;
    private final int maxDemand;
    private List<Integer> machines;
    private Map<Integer, TourneeTechnicien> tourneePerDay;

    public Technicien(int id, Point domicile, int distanceMax, int maxDemand, Map<Integer,Boolean> canInstallMachine) {
        this.id = id;
        this.maxDistance = distanceMax;
        this.maxDemand = maxDemand;
        this.domicile = domicile;
        this.machines = new ArrayList<>();
        for(Map.Entry<Integer, Boolean> entry :canInstallMachine.entrySet()){
            if(entry.getValue())
                this.machines.add(entry.getKey());
        }
        this.tourneePerDay = new HashMap<>();
    }

    /**
     * Ajoute une nouvbelle tournée au technicien
     * @param day le jour de la tournée
     * @param t la tournée à ajouter
     * @return true si ok, false sinon
     */
    public boolean addNewTournee(int day, TourneeTechnicien t){
        if(t == null) return false;
        this.tourneePerDay.put(day, t);
        return true;
    }

    /**
     * Vérifie si le technicien est dispo pour un jour et une demande donnée
     * @param demand la demande a traiter
     * @param day le jour durant lequel la demande doit etre traiter
     * @return true si libre, false sinon
     */
    public boolean isAvailable(Demande demand, int day){
        if(demand == null || day < 0) return false;
        TourneeTechnicien t = this.tourneePerDay.get(day);
        //TODO : fonctionne que pour une insertion a la fin
        //TODO : vérifier les jours de repos
        if(t != null)
            return this.canHandleDemand(t, demand);
        //TODO : moche et a changer, mais gère le cas ou on crée une nouvelle tournée, et que la distance est directement trop élevée...
        else if(this.domicile.getDistTo(demand.getClient()) + demand.getClient().getDistTo(this.getDomicile()) > this.maxDistance)
            return false;
        return true;
    }

    /**
     * Vérifie si les demandes et les distances du techniciens sont ok pour traiter une demande donnée
     * @param tournee la tournée dans laquelle a insérer la demande
     * @param demand la demande a traiter
     * @return true si dispo, false sinon
     */
    private boolean canHandleDemand(TourneeTechnicien tournee, Demande demand){
        int nbMachinesTotal = 0;

        for(Demande d : tournee.getDemandes()){
            nbMachinesTotal += d.getNbMachines();
        }

        if(nbMachinesTotal + demand.getNbMachines() > this.maxDemand)
            return false;

        //TODO : fonctionne que pour une insertion a la fin
        if(tournee.getDistance() + tournee.deltaDistInsertion(tournee.getDemandes().size(), demand) > this.maxDistance)
            return false;

        return true;
    }

    /**
     * Check gloable du technicien
     * @return true si le technicien est correct, false sinon
     */
    public boolean check() {
        return this.checkDemand() && this.checkDist();
    }

    /**
     * Vérifie si le nombre de demande à correctement été calculée pour chaque tournée de chaque jour
     * @return true si ok, false sinon
     */
    private boolean checkDemand() {
        for(Map.Entry<Integer, TourneeTechnicien> entry : this.tourneePerDay.entrySet()){
            if(entry.getValue().getDemandes().size() > this.maxDemand)
                return false;
        }
        return true;
    }

    /**
     * Vérifie si la distance à correctement été calculée pour chaque tournée de chaque jour
     * @return true si ok, false sinon
     */
    private boolean checkDist() {
        for(Map.Entry<Integer, TourneeTechnicien> entry : this.tourneePerDay.entrySet()){
            TourneeTechnicien t = entry.getValue();
            int distTournee = t.checkDist(this.domicile);

            if(distTournee != t.getDistance() || distTournee > this.maxDistance)
                return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getMachines() {
        return machines;
    }

    public Point getDomicile() {
        return domicile;
    }

    public TourneeTechnicien getTourneeOnDay(int day){
        return this.tourneePerDay.get(day);
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
                ", localisation: " + domicile +
                ", maxDistance: " + maxDistance +
                ", maxDemand: " + maxDemand +
                ", machines: " + machines +
                '}';
    }
}
