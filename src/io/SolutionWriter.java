package io;

import solution.Solution;
import solution.Tournee;
import solution.TourneeCamion;
import solution.TourneeTechnicien;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class SolutionWriter {

    private Solution solution;

    public SolutionWriter(Solution solution){
        this.solution = solution;
    }

    /**
     * @return void pour l'instant
     */
    public void writeSolution() throws FileNotFoundException, UnsupportedEncodingException {
        String name = "solution-" + solution.getInstance().getName();
        PrintWriter pw = new PrintWriter("solution/"+ name + ".txt","UTF-8");

        //Début de l'écriture
        pw.println("DATASET = " + this.solution.getInstance().getDataset());
        pw.println("NAME = " + this.solution.getInstance().getName());
        pw.println("TRUCK_DISTANCE = " + this.solution.getTruckDistance());
        pw.println("NUMBER_OF_TRUCK_DAYS = " + this.solution.getNumberOfTruckDays());
        pw.println("NUMBER_OF_TRUCKS_USED = " + this.solution.getNumberOfTruckUsed());
        pw.println("TECHNICIAN_DISTANCE = " + this.solution.getTechnicianDistance());
        pw.println("NUMBER_OF_TECHNICIAN_DAYS = " + this.solution.getNumberOfTechnicianDays());
        pw.println("NUMBER_OF_TECHNICIANS_USED = " + this.solution.getNumberOfTechnicianUsed());
        pw.println("IDLE_MACHINE_COSTS = " + this.solution.getIdleMachineCost());
        pw.println("TOTAL_COST = " + this.solution.getTotalCost());

        //key = day, value= List<Tournee>
        this.solution.getDays().forEach((key,value) -> {
            pw.println("DAYS = " + key);

            List<TourneeCamion> tourneeTruck = new LinkedList<TourneeCamion>();
            List<TourneeTechnicien> tourneeTechnician = new LinkedList<TourneeTechnician>();
            for(Tournee t : value){
                if(t instanceof TourneeCamion){
                    tourneeTruck.add((TourneeCamion) t);
                } else {
                    tourneeTechnician.add((TourneeTechnician) t);
                }
            }
            pw.println("NUMBER_OF_TRUCKS = " + tourneeTruck.size());
            if(tourneeTruck.size() > 0){
                for()
            }
            pw.println("NUMBER_OF_TECHNICIANS = " + nbTechnicians);
            if(tourneeTechnician.size() > 0){
                for()
            }
        });

    }

    /**
     * DATASET = VeRoLog solver challenge 2019 OK
     * NAME = testInstance OK
     * TRUCK_DISTANCE = 394 OK
     * NUMBER_OF_TRUCK_DAYS = 3 OK
     * NUMBER_OF_TRUCKS_USED = 1 OK
     * TECHNICIAN_DISTANCE = 312 OK
     * NUMBER_OF_TECHNICIAN_DAYS = 5 OK
     * NUMBER_OF_TECHNICIANS_USED = 3 OK
     * IDLE_MACHINE_COSTS = 400 OK
     * TOTAL_COST = 601706 OK
     * DAY = 1
     * NUMBER_OF_TRUCKS = 0
     * NUMBER_OF_TECHNICIANS = 0
     * DAY = 2
     * NUMBER_OF_TRUCKS = 1
     * 1 5 3
     * NUMBER_OF_TECHNICIANS = 0
     * DAY = 3
     * NUMBER_OF_TRUCKS = 1
     * 1 6 7 0 1 2
     * NUMBER_OF_TECHNICIANS = 1
     * 1 5
     * DAY = 4
     * NUMBER_OF_TRUCKS = 0
     * NUMBER_OF_TECHNICIANS = 3
     * 1 3 2
     * 2 1
     * 4 7
     * DAY = 5
     * NUMBER_OF_TRUCKS = 1
     * 1 4
     * NUMBER_OF_TECHNICIANS = 0
     * DAY = 6
     * NUMBER_OF_TRUCKS = 0
     * NUMBER_OF_TECHNICIANS = 1
     * 4 6 4
     * DAY = 7
     * NUMBER_OF_TRUCKS = 0
     * NUMBER_OF_TECHNICIANS = 0
     */


}
