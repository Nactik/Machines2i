package solveur;

import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import operateur.FusionTournees;
import solution.Solution;
import solution.TypeTournee;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClarkeAndWright implements Solveur {

    @Override
    public String getNom() {
        return "ClarkeAndWright";
    }

    @Override
    public Solution solve(Instance instance) {
        Solution solution  = new Solution(instance);
        boolean fusion = true;

        for(Map.Entry<Integer, Client> entry : instance.getClients().entrySet()){
            for (Demande demande : entry.getValue().getDemandes()){
                solution.addDemandNewTourneeTruck(demande);
            }
        }

        while(fusion){
            FusionTournees ft = solution.getMeilleureFusion(TypeTournee.TOURNEE_TRUCK);
            if(!ft.isMouvementRealisable() || !ft.isMouvementAmeliorant() || !solution.doFusion(ft))
                fusion = false;
        }

        for(Map.Entry<Integer, Client> entry : instance.getClients().entrySet()){
            for (Demande demande : entry.getValue().getDemandes()){
                solution.addDemandTourneeTech(demande);
            }
        }

        fusion = true;
        while(fusion){
            FusionTournees ft = solution.getMeilleureFusion(TypeTournee.TOURNEE_TECH);
            if(!ft.isMouvementRealisable() || !ft.isMouvementAmeliorant() || !solution.doFusion(ft))
                fusion = false;
        }

        return solution;
    }

    public static void main(String[] args) {
        InstanceReader reader;
        try {
            //reader = new InstanceReader("exemple/testInstance.txt");
            //reader = new InstanceReader("instances/ORTEC-early-easy/VSC2019_ORTEC_early_24_easy.txt");
            reader = new InstanceReader("instances/ORTEC-early-easy/VSC2019_ORTEC_early_19_easy.txt");
            Instance instance =  reader.readInstance();
            ClarkeAndWright caw = new ClarkeAndWright();
            System.out.println(caw.getNom());

            Solution solution = caw.solve(instance);
            System.out.println(solution.toString());

            if(solution.check()) System.out.println("Solution OK");
            else System.out.println("Solution NOK");

            SolutionWriter io = new SolutionWriter(solution, caw.getNom());
            io.writeSolution();
        } catch (ReaderException ex) {
            Logger.getLogger(Instance.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
