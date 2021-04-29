package solution;

import instance.Instance;
import instance.model.Demande;

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
        instance = null;
        truckDistance = 0;
        numberOfTruckDays = 0;
        numberOfTruckUsed = 0;
        technicianDistance = 0;
        numberOfTechnicianDays = 0;
        numberOfTechnicianUsed = 0;
        idleMachineCost = 0;
        totalCost = 0;
        days = new HashMap<Integer,LinkedList<Tournee>>();
    }
    public Solution(Instance instance) {
        super();
        this.instance = instance;
    }
    public Solution(Solution solution){
        this.instance = solution.instance;
        truckDistance = solution.truckDistance;
        numberOfTruckDays = solution.numberOfTruckDays;
        numberOfTruckUsed = solution.numberOfTruckUsed;
        technicianDistance = solution.technicianDistance;
        numberOfTechnicianDays = solution.numberOfTechnicianDays;
        numberOfTechnicianUsed = solution.numberOfTechnicianUsed;
        idleMachineCost = solution.idleMachineCost;
        totalCost = solution.totalCost;
        days = mapCopy(solution.days);
    }
    private HashMap<Integer,LinkedList<Tournee>> mapCopy(HashMap<Integer,LinkedList<Tournee>> toCopy){
        HashMap<Integer,LinkedList<Tournee>> copy = new HashMap<Integer,LinkedList<Tournee>>();
        for(Map.Entry<Integer,LinkedList<Tournee>> entry : toCopy.entrySet()){
            copy.put(entry.getKey(),new LinkedList<Tournee>(entry.getValue()));
        }
        return copy;
    }

    public void addClientNewTourneeCamion(Demande demande) {

    }
}
