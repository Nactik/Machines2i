package solveur;

import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import solution.Solution;
import solution.Tournee;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            }
        }
        for(Demande demande : listeDemande){
           // solution.NewTourneeCamion(demande);
        }
        return solution;
    }
}
