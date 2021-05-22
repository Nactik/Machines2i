package solution;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;
import instance.reseau.Client;
import operateur.FusionTournees;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Solution {
    private Instance instance;
    private long truckDistance;
    private long numberOfTruckDays;
    private long numberOfTruckUsed;
    private long technicianDistance;
    private long numberOfTechnicianDays;
    private long numberOfTechnicianUsed;
    private long idleMachineCost;
    private long totalCost;
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

        if(this.getNumberOfTourneeTruckToday(demand.getFirstDay()) >= this.numberOfTruckUsed){ // TODO: Attention, firstDay que pour une nouvelle tournee
            this.numberOfTruckUsed ++;
            this.totalCost += this.instance.getTruckCost();
        }

        demand.setDeliveryDay(demand.getFirstDay());

        this.numberOfTruckDays ++; //nouvelle tournée donc nouveau jour
        this.truckDistance += tourneeCamion.getDistance();
        this.totalCost += (long) tourneeCamion.getDistance()*this.instance.getTruckDistCost();
        this.totalCost += this.instance.getTruckDayCost(); //nouvelle tournée donc on paie un jour en plus
        this.addTourneeToMap(tourneeCamion, demand.getFirstDay());
        return true;
    }

    /**
     * Ajoute une demande a une nouvelle tournée technicien
     * @param demand la demande a ajouter
     */
    public boolean addDemandTourneeTech(Demande demand){
        int idleCost;
        if(demand == null) return false;

        int deliveryDay = demand.getFirstDay();
        int installationDay = deliveryDay+1; //on installe le jour suivant

        //récupère un tech dispo
        Technicien tech = getAvailableTech(demand, installationDay);
        if(tech == null){
            //demande ne peut etre traité dans le cas triviale
            idleCost = this.evalIdleCost(demand);
            this.idleMachineCost += idleCost;
            this.totalCost += idleCost;
            return false;
        }

        if(!tech.isEmployed()) {
            //si le technicien n'a jamais fais de tournée, c'est un nouveau tech, il faut donc le payer
            this.totalCost += this.instance.getTechCost();
            this.numberOfTechnicianUsed ++;
        }

        //récupère la tournee du jour, ou bien une nouvelle si nulle
        TourneeTechnicien tourneeTech = tech.getTourneeOnDay(installationDay);
        if(tourneeTech == null){
            tourneeTech = new TourneeTechnicien(this.instance, tech, installationDay);
            this.totalCost += this.instance.getTechDayCost(); //si nouvelle tournee => nouveau jour donc on paye un jour de plus
            this.numberOfTechnicianDays ++;
        }

        this.technicianDistance -= tourneeTech.getDistance(); //si nouvelle tournée, ca ne change rien car distance=0
        this.totalCost -= ((long)tourneeTech.getDistance() *this.instance.getTechDistCost());

        if(!tourneeTech.addDemand(demand))
            return false;

        this.technicianDistance += tourneeTech.getDistance();
        this.totalCost += ((long) tourneeTech.getDistance()*this.instance.getTechDistCost());

        demand.setInstallationDay(installationDay);
        idleCost = this.evalIdleCost(demand);
        this.idleMachineCost += idleCost;
        this.totalCost += idleCost;
        this.addTourneeToMap(tourneeTech, installationDay);

        return true;
    }

    private long getNumberOfTourneeTruckToday(int jour){
        if(this.days.get(jour) == null)
            return 0;

        return this.days.get(jour).stream()
                .filter(t -> t instanceof TourneeCamion)
                .count();
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
    private int evalIdleCost(Demande demand) {
        int idleDays;

        if (demand.getInstallationDay() == -1) { //machine pas installé
            idleDays = this.instance.getNbDay() - demand.getDeliveryDay();
            return idleDays * demand.getNbMachines() * this.instance.getMachines().get(demand.getIdMachine() - 1).getPenality();
        } else {
            idleDays = demand.getInstallationDay() - demand.getDeliveryDay() - 1; //-1 car on veut les jours inactifs

            if (idleDays < 0 || demand.getInstallationDay() == -1 || demand.getInstallationDay() <= demand.getDeliveryDay())
                return Integer.MAX_VALUE;

            //marche seulement si la liste est dans le meme ordre
            return idleDays * demand.getNbMachines() * this.instance.getMachines().get(demand.getIdMachine() - 1).getPenality();
        }
    }

    private int getMaxTourneeTruck(){
        return this.days.values().stream()
                .filter(l -> l.getFirst() instanceof TourneeCamion)
                .mapToInt(List::size).max().getAsInt();
    }

    /**
     * Réalise la fusion de deux tournées
     * @param infos les infos de la tournée a fusionner
     * @return true si ok, false sinon
     */
    public boolean doFusion(FusionTournees infos){
        if(!infos.doMouvementIfRealisable() || !infos.isMouvementAmeliorant()) return false;
        this.days.get(infos.getaFusionner().getDay()).remove(infos.getaFusionner());

        //TODO : faire une fonction générique qui maj tout
        this.truckDistance+= infos.getDeltaDist();
        this.numberOfTruckDays --;
        this.totalCost -= this.instance.getTruckDayCost();

        if(this.getMaxTourneeTruck() < this.numberOfTruckUsed){
            this.totalCost -= this.instance.getTruckCost();
            this.numberOfTruckUsed --;
        }


        long distCost = (infos.getaFusionner() instanceof TourneeCamion) ? this.instance.getTruckDistCost() : this.instance.getTechDistCost();

        //TODO : ICI ça marche pas, surement une erreur dans le getDeltaDist
        this.totalCost +=  ((long)infos.getDeltaDist() * distCost);


        return true;
    }


    /**
     * Récupère la meilleure fusion de tournée
     * @return le meilleur opérateur de fusion
     */
    public FusionTournees getMeilleureFusion() {
        if(this.days.isEmpty()) return new FusionTournees();

        FusionTournees best = new FusionTournees(), test;

        for(List<Tournee> list : this.days.values()){
            for (Tournee t: list) {
                test = this.getMeilleureFusion(t);
                if ((!best.isMouvementRealisable() && test.isMouvementRealisable()) || test.isMeilleur(best)) {
                    best = test;
                }
            }
        }

        return best;
    }

    /**
     * Récupère le meilleur opérateur de tournée pour une tournée donnée
     * @param mTournee la tournée
     * @return le meilleur opérateur
     */
    private FusionTournees getMeilleureFusion(Tournee mTournee){
        if(mTournee.getDemCap() == 0) return new FusionTournees();

        FusionTournees best = new FusionTournees(), test = new FusionTournees();

        for (Tournee t: this.days.get(mTournee.getDay())) {
            //filtre sur les tournee du même type que mTournee
            if(t.getClass().isInstance(mTournee)) {
                if(!mTournee.equals(t) && t.getMaxDemCap() != 0 && mTournee.getDemCap() + t.getDemCap() <= mTournee.getMaxDemCap()){
                    test = new FusionTournees(mTournee, t);
                }
                if ((!best.isMouvementRealisable() && test.isMouvementRealisable()) || test.isMeilleur(best)) {
                    best = test;
                }
            }
        }

        return best;
    }

    /**
     * Check globale de la solution
     * @return true si tout est ok, false sinon
     */
    public boolean check(){
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
        if(this.checkNbTechUsed() != this.numberOfTechnicianUsed)
            return false;
        if(this.checkNbTechDays() != this.numberOfTechnicianDays)
            return false;
        if(this.checkNbTruckUsed() != this.numberOfTruckUsed)
            return false;
        if(this.checkNbTruckDays() != this.numberOfTruckDays)
            return false;
        if(this.checkTotalCost() != this.totalCost)
            return false;

        return true;
    }

    /**
     * Calcul la distance des camions totale dans la solution
     * @return la distance
     */
    private long checkTruckDistance(){
        long truckDistance = 0;

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
    private long checkTechnicianDistance(){
        long technicianDistance = 0;

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
    private long checkIdleCost(){
        long idleCost = 0;

        for(Map.Entry<Integer, Client> entry : this.instance.getClients().entrySet()) {
            Client client = entry.getValue();
            for(Demande d: client.getDemandes()){
                idleCost += evalIdleCost(d);
            }
        }

        return idleCost;
    }

    /**
     * calcul la variable NUMBER_OF_TECHS_USED,: le nombre total de techniciens utilisés.
     * journée.
     * @return la valeure de NUMBER_OF_TECHS_USED
     */
    private long checkNbTechUsed(){
        LinkedList<Technicien> listeTech = new LinkedList<>();
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
        return listeTech.size();
    }

    /**
     * calcul la variable NUMBER_OF_TECHNICIAN_DAYS, : le nombre total de journées technicien utilisées.
     * @return la valeur de NUMBER_OF_TECHNICIAN_DAYS
     */
    private long checkNbTechDays(){
        long nbTechDays =0;
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){
                if(t instanceof TourneeTechnicien){ //On compte toutes les tournées technicien
                    nbTechDays+=1;
                }
            }
        }
        return nbTechDays;
    }

    /**
     * calcul la variable NUMBER_OF_TRUCKS_USED,le nombre maximal de camion utilisés dans une
     * journée.
     * @return la valeure de NUMBER_OF_TRUCKS_USED
     */
    private long checkNbTruckUsed(){
        long maxNbTruck = 0;
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
        return maxNbTruck;
    }

    /**
     * calcul la variable NUMBER_OF_TRUCK_DAYS, le nombre total de journées camion utilisées.
     * @return la valeur de NUMBER_OF_TRUCK_DAYS
     */
    private long checkNbTruckDays(){
        long nbTruckDays =0;
        for (LinkedList<Tournee> liste : this.days.values()) {
            for (Tournee t : liste){
                if(t instanceof TourneeCamion){
                    nbTruckDays+=1;
                }
            }
        }
        return nbTruckDays;
    }

    /**
     * Vérifie le cout total de la solution
     * @return la vérification du cout total de la solution
     */

    private long checkTotalCost(){
        long totalCost = 0;
        totalCost += this.checkTruckDistance()* this.instance.getTruckDistCost();
        totalCost += this.checkTechnicianDistance() * this.instance.getTechDistCost();
        totalCost += this.checkNbTechUsed() * this.instance.getTechCost();
        totalCost += this.checkNbTruckUsed() * this.instance.getTruckCost();
        totalCost += this.checkNbTechDays() * this.instance.getTechDayCost();
        totalCost += this.checkNbTruckDays() * this.instance.getTruckDayCost();
        totalCost += this.checkIdleCost();
        return totalCost;
    }

    public Instance getInstance() {
        return instance;
    }

    public long getTruckDistance() {
        return truckDistance;
    }

    public long getNumberOfTruckDays() {
        return numberOfTruckDays;
    }

    public long getNumberOfTruckUsed() {
        return numberOfTruckUsed;
    }

    public long getTechnicianDistance() {
        return technicianDistance;
    }

    public long getNumberOfTechnicianDays() {
        return numberOfTechnicianDays;
    }

    public long getNumberOfTechnicianUsed() {
        return numberOfTechnicianUsed;
    }

    public long getIdleMachineCost() {
        return idleMachineCost;
    }

    public long getTotalCost() {
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
