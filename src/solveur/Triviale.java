package solveur;

import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import io.InstanceReader;
import io.exception.ReaderException;
import solution.Solution;

import java.util.LinkedList;
import java.util.List;
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
        List<Demande> listeDemande = new LinkedList<>();
        for(Map.Entry<Integer,Client> entry : instance.getClients().entrySet()){
            for (Demande demande : entry.getValue().getDemandes()){
                listeDemande.add(demande);
                solution.addDemandNewTourneeTruck(demande);
            }
        }
        return solution;
    }

    public static void main(String[] args) {
        InstanceReader reader;
        try {
            reader = new InstanceReader("exemple/testInstance.txt");
            Instance instance =  reader.readInstance();
            Triviale triviale = new Triviale();
            System.out.println(triviale.getNom());

            Solution solution = triviale.solve(instance);
            System.out.println(instance.getClients().size());
            System.out.println(solution.toString());
        } catch (ReaderException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
