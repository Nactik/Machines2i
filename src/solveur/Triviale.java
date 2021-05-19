package solveur;

import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import solution.Solution;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Triviale implements Solveur{
    @Override
    public String getNom() {
        return "Solution triviale";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution  = new Solution(instance);
        for(Map.Entry<Integer,Client> entry : instance.getClients().entrySet()){
            for (Demande demande : entry.getValue().getDemandes()){
                solution.addDemandNewTourneeTruck(demande);
                solution.addDemandTourneeTech(demande);
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        InstanceReader reader;
        try {
            reader = new InstanceReader("instances/ORTEC-early-easy/VSC2019_ORTEC_early_03_easy.txt");
            //reader = new InstanceReader("exemple/testInstance.txt");
            Instance instance =  reader.readInstance();
            Triviale triviale = new Triviale();
            System.out.println(triviale.getNom());

            Solution solution = triviale.solve(instance);
            System.out.println(solution.toString());
            if(solution.check()) System.out.println("Solution OK");
            else System.out.println("Solution NOK");

            SolutionWriter io = new SolutionWriter(solution);
            io.writeSolution();
        } catch (ReaderException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
