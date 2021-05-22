package instance.model;

import instance.reseau.Point;
import solution.TourneeTechnicien;


import java.util.*;
import java.util.stream.Collector;

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
        boolean needRest = false;
        if(t == null)
            needRest = this.doesNeedRest(day); //on check car il n'a jamais trvaillé le joir la
        return canHandleDemand(t, demand) && !needRest;
    }

    /**
     * Vérifie si les demandes et les distances du techniciens sont ok pour traiter une demande donnée
     * @param tournee la tournée dans laquelle a insérer la demande
     * @param demand la demande a traiter
     * @return true si dispo, false sinon
     */
    private boolean canHandleDemand(TourneeTechnicien tournee, Demande demand){
        if(tournee == null) //cas ou la demande est directement trop élevé
            return !(this.domicile.getDistTo(demand.getClient()) + demand.getClient().getDistTo(this.getDomicile()) > this.maxDistance);
        else{
            if(tournee.getDemandes().size() + 1 > this.maxDemand)
                return false;

            //TODO : fonctionne que pour une insertion a la fin
            if(tournee.getDistance() + tournee.deltaDistInsertion(tournee.getDemandes().size(), demand) > this.maxDistance)
                return false;
        }

        return true;
    }

    /**
     * Permet de savoir si le technicien a besoin de repos ou non
     * @return true si il a besoin de repos, false sinon
     */
    private boolean doesNeedRest(int installationDay){
        Set<Integer> daysWorkedKey = this.tourneePerDay.keySet(); //on récupère les jours travaillés
        LinkedList<Integer> daysWorked = new LinkedList<>(daysWorkedKey); //stockage dans list pour acceder

        daysWorked.add(installationDay); //on ajoute le possible nouveau jour pour test
        Collections.sort(daysWorked); //on la trie (important pour le formattage

        //on récupère les jours travaillés ou non, notés 1 et 0
        LinkedList<Integer> techPlanning = this.getPlannigFromDaysWorked(daysWorked);

        if(techPlanning.size() <= 5)
            return false;


        for(int i=0; i<techPlanning.size()-5; i++){
            int consecutiveDaysWorked = 0, checkRestDays = 0;

            for(int j=i; j<i+5; j++){
                consecutiveDaysWorked += techPlanning.get(j);
            }

            if(consecutiveDaysWorked == 5){
                for(int j=i+5; j<i+7; j++){
                    if(j >= techPlanning.size() || techPlanning.get(j) == 0)
                        checkRestDays++;
                }
            }

            if(consecutiveDaysWorked == 5 && checkRestDays != 2)
                return true;
        }

        return false;
    }

    /**
     * Formate une liste de numéros jours travaillés en une liste de 0 et de 1 en fonction des index
     * @param daysWorkedSorted  liste de numéros jours travaillés
     * @return liste de 0 et de 1 en fonction des index
     */
    private LinkedList<Integer> getPlannigFromDaysWorked(LinkedList<Integer> daysWorkedSorted){
        LinkedList<Integer> formatedDaysWorkedList = new LinkedList<>();
        int maxNumDayWorked = daysWorkedSorted.getLast(); //On récupère la dernière valeur, qui doit etre le jour max travaillé (logiquement)

        //On crée une liste. L'index correspond au numéro du jour, la valeur correspnd a si il est travaillé ou non
        //1 travaillé, 0 pas travaillé
        for(int i = 1; i <= maxNumDayWorked; i++){
            if(daysWorkedSorted.contains(i))
                formatedDaysWorkedList.add(1);
            else
                formatedDaysWorkedList.add(0);
        }

        return formatedDaysWorkedList;
    }

    /**
     * Permet de savoir si le technicien a déjà été utilisé dans sa vie (déjà employé)
     * @return true si le technicien est déjà employé, false sinon
     */
    public boolean isEmployed(){
        return this.tourneePerDay.size() != 0;
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

    public int getMaxDemand() {
        return this.maxDemand;
    }

    public int getMaxDistance() {
        return maxDistance;
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

    public static void main(String[] args) {
//        int[] workedDaysArray = new int[] {1, 2, 3, 5, 7};
//        LinkedList<Integer> workedDaysList = new LinkedList<>();
//        LinkedList<Integer> formatedWorkedDaysList;
//
//        for(int d: workedDaysArray){
//            workedDaysList.add(d);
//        }
//
////        System.out.println(workedDaysList);
////
////        formatedWorkedDaysList = Technicien.getPlannigFromDaysWorked(workedDaysList);
////        System.out.println(formatedWorkedDaysList);
////
////        workedDaysList.add(4);
////        Collections.sort(workedDaysList);
////        formatedWorkedDaysList = Technicien.getPlannigFromDaysWorked(workedDaysList);
////
////        System.out.println(formatedWorkedDaysList);
//
//        Technicien.doesNeedRest(workedDaysList, 4);
    }
}
