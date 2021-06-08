package io;

import instance.Instance;
import instance.model.Demande;
import instance.model.Machine;
import instance.model.Technicien;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import solution.Solution;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette classe permet de lire une solution
 */
public class SolutionReader {

    /**
     * Le fichier contenant la solution.
     */
    private File solutionFile;

    /**
     * Constructeur par donnee du chemin du fichier de solution.
     * @param inputPath le chemin du fichier de solution
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou
     * ne peut pas etre ouvert.
     */
    public SolutionReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith(".txt")) {
            throw new FormatFileException("txt", "txt");
        }
        this.solutionFile = new File(inputPath);
    }

    /**
     * Methode principale pour lire le fichier de solution.
     * @return la solution lue
     * @throws ReaderException lorsque les donnees dans le fichier de solution
     * sont manquantes ou au mauvais format.
     */
    public Solution readSolution() throws ReaderException {
        try{
            FileReader f = new FileReader(this.solutionFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            String dataSet = lireDataset(br);
            String nom = lireNom(br);

            long truckDist = lireLabel(br, "TRUCK_DISTANCE =");
            long numberOfTruckDays = lireLabel(br,"NUMBER_OF_TRUCK_DAYS =");
            long numberOfTruckUse = lireLabel(br,"NUMBER_OF_TRUCKS_USED =");

            long technicianDistance = lireLabel(br,"TECHNICIAN_DISTANCE =");
            long numberOfTechDays = lireLabel(br,"NUMBER_OF_TECHNICIAN_DAYS =");
            long numberOfTechUse = lireLabel(br,"NUMBER_OF_TECHNICIANS_USED =");

            long idleMachineCost =  lireLabel(br,"IDLE_MACHINE_COSTS =");
            long totalCost =  lireLabel(br,"TOTAL_COST =");

            Instance instance = new Instance(dataSet,nom);
            Solution solution = new Solution(instance,
                    (int) truckDist,
                    (int) numberOfTruckDays,
                    (int) numberOfTruckUse,
                    (int) technicianDistance,
                    (int) numberOfTechDays,
                    (int) numberOfTechUse,
                    (int) idleMachineCost,
                    (int) totalCost);

            br.close();
            f.close();
            return solution;

        } catch (FileNotFoundException ex) {
            throw new FileExistException(solutionFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }
    
    /**
     * Lecture du dataset de la solution.
     * La ligne dans le fichier doit commencer par "NAME ="
     * @param br lecteur courant du fichier de solution
     * @return le dataset de la solution
     * @throws IOException
     */
    private String lireDataset(BufferedReader br) throws IOException {
        String ligne = br.readLine().trim();
        while(!ligne.contains("DATASET =")) {
            ligne = br.readLine().trim();
        }
        ligne = ligne.replace("DATASET = ", "");
        return ligne;
    }
    /**
     * Lecture du nom de la solution.
     * La ligne dans le fichier doit commencer par "NAME ="
     * @param br lecteur courant du fichier de solution
     * @return le nom de la solution
     * @throws IOException
     */
    private String lireNom(BufferedReader br) throws IOException {
        String ligne = br.readLine().trim();
        while(!ligne.contains("NAME =")) {
            ligne = br.readLine().trim();
        }
        ligne = ligne.replace(" ", "");
        ligne = ligne.replace("NAME=", "");
        return ligne;
    }

    /**
     * Lis la valeur associée au label passé en paramètre
     * @param br lecteur courant du fichier de solution
     * @param label le label désiré
     * @return la valeur associée au label
     * @throws IOException
     */
    private long lireLabel(BufferedReader br, String label) throws IOException{
        String ligne = br.readLine().trim();
        while(!ligne.contains(label)) {
            ligne = br.readLine().trim();
        }
        ligne = ligne.replace(label, "");
        ligne = ligne.replace(" ", "");
        return Integer.parseInt(ligne);
    }


    public static void main(String[] args) {
        try {
            SolutionReader reader = new SolutionReader("exemple/testInstance_sol.txt");
            Solution solution = reader.readSolution();
            System.out.println(solution.getTotalCost());
            System.out.println("solution lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
