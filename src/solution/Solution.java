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
     * Ajoute une demande à une nouvelle tournée de camion le premier jour de la dispo de la commande
     * @param demand la demande à traiter
     */
    public boolean addDemandNewTourneeTruck(Demande demand) {
        TourneeCamion tourneeCamion = new TourneeCamion(this.instance, demand.getFirstDay()); // TODO: changer, fonctionne pour une nouvelle tournée uniquement

        if(!tourneeCamion.addDemand(demand))
            return false;

        demand.setDeliveryDay(demand.getFirstDay());
        this.truckDistance += tourneeCamion.getDistance();
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
        int installationDay = deliveryDay+1; //on installe le jour suivant
        demand.setInstallationDay(installationDay);

        //récupèr un tech dispo
        Technicien tech = getAvailableTech(demand, installationDay);
        if(tech == null){
            //demande ne peut etre traité dans le cas triviale
            int idleDays = this.instance.getNbDay() - demand.getDeliveryDay();
            this.idleMachineCost += idleDays*demand.getNbMachines()*this.instance.getMachines().get(demand.getIdMachine()-1).getPenality();
            return false;
        }


        //récupère la tournee du jour, ou bien une nouvelle si nulle
        TourneeTechnicien tourneeTech = tech.getTourneeOnDay(installationDay);
        if(tourneeTech == null){
            tourneeTech = new TourneeTechnicien(this.instance, tech, deliveryDay);
        }

        this.technicianDistance -= tourneeTech.getDistance(); //si nouvelle tournée, ca ne change rien car distance=0
        if(!tourneeTech.addDemand(demand))
            return false;
        this.technicianDistance += tourneeTech.getDistance();
        this.idleMachineCost += this.evalIdleCost(demand);
        this.addTourneeToMap(tourneeTech, installationDay);
        return true;
    }

    /**
     * Récupère un technicien disponible
     * @param demand la demande a assigné au technicien
     * @param installationDay le jour d'installation de la demande
     * @return le technicien ou null si aucun est dispo
     */
    private Technicien getAvailableTech(Demande demand, int installationDay){
        for(Technicien t : this.instance.getTechnicians().values()){
            if(t.getMachines().contains(demand.getIdMachine())
                    && t.isAvailable(demand, installationDay)){
                return t;
            }
        }
        return null;
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
        LinkedList<Tournee> tourneesOnDay = days.get(jour);
        tourneesOnDay.remove(tournee);
        tourneesOnDay.add(tournee);
    }

    /**
     * Calcul le temps d'inactivité des machines d'une demande
     * @param demand la demande
     * @return le cout du temps d'inactivité
     */
    private int evalIdleCost(Demande demand){
        int idleDays = demand.getInstallationDay() - demand.getDeliveryDay() -1; //-1 car on veut les jours inactifs

        if(idleDays < 0 || demand.getInstallationDay() == -1 || demand.getInstallationDay() <= demand.getDeliveryDay())
            return Integer.MAX_VALUE;

        return idleDays*demand.getNbMachines()*this.instance.getMachines().get(demand.getIdMachine()-1).getPenality(); //marche seulement si la liste est dans le meme ordre
    }

    /**
     * calcul la variable NUMBER_OF_TRUCKS_USED,le nombre maximal de camion utilisés dans une
     * journée.
     * @return la valeure de NUMBER_OF_TRUCKS_USED
     */
    public int evalNbTruckUsed(){
        int maxNbTruck = 0;
        int nbTruck = 0;
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){ //On parcourt chaque tournée de chaque jour
                if(t instanceof TourneeCamion){ //Si c'est une tournée camion, on incrémente le compteur
                    nbTruck+=1;
                }
            }
            if(nbTruck> maxNbTruck){ //Si le compteur est supérieur à la valeur max actuelle, on remplace
                maxNbTruck=nbTruck;
            }
            nbTruck=0;
        }
        this.numberOfTruckUsed = maxNbTruck;
        return maxNbTruck;
    }

    /**
     * calcul la variable NUMBER_OF_TRUCK_DAYS, le nombre total de journées camion utilisées.
     * @return la valeur de NUMBER_OF_TRUCK_DAYS
     */
    public int evalNbTruckDays(){
        int nbTruckDays =0;
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){
                if(t instanceof TourneeCamion){
                    nbTruckDays+=1;
                }
            }
        }
        this.numberOfTruckDays=nbTruckDays;
        return nbTruckDays;
    }
    /**
     * calcul la variable NUMBER_OF_TRUCKS_USED,: le nombre total de techniciens utilisés.
     * journée.
     * @return la valeure de NUMBER_OF_TRUCKS_USED
     */
    public int evalNbTechUsed(){
        LinkedList<Technicien> listeTech = new LinkedList<Technicien>();
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){ //On parcourt chaque tournée de chaque jour
                if(t instanceof TourneeTechnicien){ //Si c'est une tournée Technicien
                    if (!listeTech.contains(((TourneeTechnicien) t).getTechnician())){
                        //Si le technicien est déjà dans la liste, on ne l'ajoute pas
                        listeTech.add(((TourneeTechnicien) t).getTechnician());
                    }
                }
            }
        }
        this.numberOfTechnicianUsed = listeTech.size();
        return listeTech.size();
    }

    /**
     * calcul la variable NUMBER_OF_TECHNICIAN_DAYS, : le nombre total de journées technicien utilisées.
     * @return la valeur de NUMBER_OF_TECHNICIAN_DAYS
     */
    public int evalNbTechDays(){
        int nbTechDays =0;
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){
                if(t instanceof TourneeTechnicien){ //On compte toutes les tournées technicien dans la
                    nbTechDays+=1;
                }
            }
        }
        this.numberOfTechnicianDays=nbTechDays;
        return nbTechDays;
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
        // appeler tout les checks des tournées OK
        // vérifier les distances camion OK
        // vérifier les distances techniciens OK
        // vérifier les jours camion
        // vérifier les jours tech
        // vérifier le nb de camions utilisés
        // vérifier le nb de tech utilisés
        // vérifier le idle machine OK
        // vérifier le cout total de la solution

        for(Map.Entry<Integer, LinkedList<Tournee>> entry : this.days.entrySet()){
            LinkedList<Tournee> tournees = entry.getValue();
            for(Tournee t : tournees){
                if(!t.check())
                    return false;
            }
        }
        if(this.checkTruckDistance() != this.truckDistance)
           return false;
       if(this.checkTechnicianDistance() != this.technicianDistance)
            return false;
        if(this.checkIdleCost() != this.idleMachineCost)
            return false;

        return true;
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
     * Vérifie le cout des pénalités d'inactivité des machines
     * @return le cout d'inactivité total
     */
    private int checkIdleCost(){
        int idleCost = 0;
        for(Map.Entry<Integer, LinkedList<Tournee>> entry : this.days.entrySet()){
            LinkedList<Tournee> tournees = entry.getValue();

            for(Tournee t : tournees){
                if(t instanceof TourneeTechnicien){
                    for(Demande d: t.getDemandes()){
                        idleCost += evalIdleCost(d);
                    }
                }
            }
        }

        return idleCost;
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
