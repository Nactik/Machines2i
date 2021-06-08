package io;

import instance.model.Demande;
import instance.model.Technicien;
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
import java.util.stream.Collectors;

public class SolutionWriter {

    private Solution solution;
    private String nameSolveur;

    public SolutionWriter(Solution solution, String nameSolveur){
        this.solution = solution;
        this.nameSolveur = nameSolveur;
    }

    /**
     * @return void pour l'instant
     */
    public void writeSolution() throws FileNotFoundException, UnsupportedEncodingException {
        String name = "Solution-" + solution.getInstance().getName()+"-"+this.nameSolveur;
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
            pw.println("DAY = " + key);

            List<TourneeCamion> tourneeTruck = new LinkedList<TourneeCamion>();
            List<TourneeTechnicien> tourneeTechnician = new LinkedList<TourneeTechnicien>();
            for(Tournee t : value){
                if(t instanceof TourneeCamion){
                    tourneeTruck.add((TourneeCamion) t);
                } else {
                    tourneeTechnician.add((TourneeTechnicien) t);
                }
            }
            pw.println("NUMBER_OF_TRUCKS = " + tourneeTruck.size());
            if(tourneeTruck.size() > 0){
                int truckId = 1;
                for(TourneeCamion t : tourneeTruck){
                    pw.println((truckId++) + " " + getDemandeSequence(t));
                }
            }

            pw.println("NUMBER_OF_TECHNICIANS = " + tourneeTechnician.size());
            if(tourneeTechnician.size() > 0){
                for(TourneeTechnicien t: tourneeTechnician){
                    pw.println(t.getTechnician().getId() + " " + getDemandeSequence(t));
                }
            }
        });

        pw.close();
    }

    private String getDemandeSequence(Tournee t){
        return  t.getDemandes().stream()
                .map(d -> String.valueOf(d.getId()))
                .collect(Collectors.joining(" "));
    }
}
