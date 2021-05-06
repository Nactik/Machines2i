package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collector;

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
     * @param demand la demande à traiter
     */
    public boolean addDemandNewTourneeTruck(Demande demand) {
        TourneeCamion tourneeCamion = new TourneeCamion(this.instance, demand.getFirstDay()); // TODO: changer, fonctionne pour une nouvelle tournée uniquement
        if(!tourneeCamion.addDemand(demand) || !tourneeCamion.check())
            return false;

        System.out.println("Tournée Camion OK");
        demand.setDeliveryDay(demand.getFirstDay());

        this.addTourneeToMap(tourneeCamion, demand.getFirstDay());
        return true;
    }

    /**
     * Ajoute une demande a une nouvelle tournée technicien
     * @param demand la demande a ajouter
     */
    public boolean addDemandNewTourneeTech(Demande demand){
        if(demand == null) return false;

        int deliveryDay = demand.getFirstDay();
        int installationDay = deliveryDay++;
        Technicien tech = null;

        for(Technicien t : this.instance.getTechnicians().values()){
            if(t.getMachines().contains(demand.getIdMachine())
                && t.isAvailable(demand, installationDay)){
                tech = t;
                break;
            }
        }

        if(tech == null) return false;

        TourneeTechnicien tourneeTech = new TourneeTechnicien(this.instance, tech, deliveryDay);

        if(!tourneeTech.addDemand(demand) || !tourneeTech.getTechnician().check())
            return false;

        System.out.println("Tournée Tech OK");
        demand.setInstallationDay(installationDay);
        this.idleMachineCost += this.evalIdleCost(demand);
        this.addTourneeToMap(tourneeTech, deliveryDay);
        return true;
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

    /**
     * Calcul le temps d'inactivité des machines d'une demande
     * @param demand la demande
     * @return le cout du temps d'inactivité
     */
    private int evalIdleCost(Demande demand){
        int idleDay = demand.getInstallationDay() - demand.getDeliveryDay() -1; //-1 car on veut les jours inactifs
        return idleDay*demand.getNbMachines()*this.instance.getMachines().get(demand.getIdMachine()-1).getPenality(); //marche seulement si la liste est dans le meme ordre
    }

    /**
     * Calcul la distance des camions totale dans la solution
     * @return la distance
     */
    private int checkTruckDistance(){
        int truckDistance = 0;

        for(Map.Entry<Integer, LinkedList<Tournee>> entry : this.days.entrySet()){
            LinkedList<Tournee> tournees = entry.getValue();

            for(Tournee t : tournees){
                if(t instanceof TourneeCamion)
                    truckDistance += t.getDistance();
            }
        }
        return truckDistance;
    }

    /**
     * Calcul la distance des techniciens totale dans la solution
     * @return la distance
     */
    private int checkTechnicianDistance(){
        int technicianDistance = 0;

        for(Map.Entry<Integer, LinkedList<Tournee>> entry : this.days.entrySet()){
            LinkedList<Tournee> tournees = entry.getValue();

            for(Tournee t : tournees){
                if(t instanceof TourneeTechnicien)
                    technicianDistance += t.getDistance();
            }
        }
        return technicianDistance;
    }

    /**
     * Calcule le cout complet de la solution
     * @return Rien pour l'instant
     */
    public void evalCost(){

    }

    /**
     * Check globale de la solution
     * @return true si tout est ok, false sinon
     */
    public boolean check(){
        // vérifier les distances camion OK
        // vérifier les distances techniciens OK
        // vérifier les jours camion
        // vérifier les jours tech
        // vérifier le nb de camions utilisés
        // vérifier le nb de tech utilisés
        // vérifier le idle machine

        return checkDistance();
    }

    /**
     * Vérifie les distances calculées dans la solution
     * @return true si les distances sont ok, false sinon
     */
    private boolean checkDistance(){
        return checkTruckDistance() == this.truckDistance && checkTechnicianDistance() == this.technicianDistance;
    }

    public Instance getInstance() {
        return instance;
    }

    public int getTruckDistance() {
        return truckDistance;
    }

    public int getNumberOfTruckDays() {
        return numberOfTruckDays;
    }

    public int getNumberOfTruckUsed() {
        return numberOfTruckUsed;
    }

    public int getTechnicianDistance() {
        return technicianDistance;
    }

    public int getNumberOfTechnicianDays() {
        return numberOfTechnicianDays;
    }

    public int getNumberOfTechnicianUsed() {
        return numberOfTechnicianUsed;
    }

    public int getIdleMachineCost() {
        return idleMachineCost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public HashMap<Integer, LinkedList<Tournee>> getDays() {
        return days;
    }

    @Override
    public String toString() {
        String string = "Solution{" +
                "\ninstance: " + instance +
                ",\n\ttruckDistance: " + truckDistance +
                ",\n\tnumberOfTruckDays: " + numberOfTruckDays +
                ",\n\tnumberOfTruckUsed: " + numberOfTruckUsed +
                ",\n\ttechnicianDistance: " + technicianDistance +
                ",\n\tnumberOfTechnicianDays: " + numberOfTechnicianDays +
                ",\n\tnumberOfTechnicianUsed: " + numberOfTechnicianUsed +
                ",\n\tidleMachineCost: " + idleMachineCost +
                ",\n\ttotalCost: " + totalCost +
                "\n\tdays: ";
        for (Map.Entry<Integer, LinkedList<Tournee>> entry : days.entrySet()) {
            string += "\n\t\t day : " + entry.getKey() + " = " + entry.getValue().toString();
        }
        string += '}';
        return string;
    }
}
