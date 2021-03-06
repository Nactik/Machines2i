/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import instance.Instance;
import instance.model.Demande;
import instance.model.Technicien;
import instance.model.Machine;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Cette classe permet de lire une instance
 */
public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;
    
    /**
     * Constructeur par donnee du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance
     * @throws ReaderException lorsque le fichier n'est pas au bon format ou 
     * ne peut pas etre ouvert.
     */
    public InstanceReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith(".txt")) {
            throw new FormatFileException("txt", "txt");
        }
        this.instanceFile = new File(inputPath);
    }
    
    /**
     * Methode principale pour lire le fichier d'instance.
     * @return l'instance lue
     * @throws ReaderException lorsque les donnees dans le fichier d'instance 
     * sont manquantes ou au mauvais format.
     */
    public Instance readInstance() throws ReaderException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            String dataSet = lireDataset(br);
            String nom = lireNom(br);

            int nbDays = lireLabel(br, "DAYS =");
            int truckCapacity = lireLabel(br,"TRUCK_CAPACITY =");
            int truckMaxDistance = lireLabel(br,"TRUCK_MAX_DISTANCE =");

            int truckDistanceCost = lireLabel(br,"TRUCK_DISTANCE_COST =");
            int truckDayCost = lireLabel(br,"TRUCK_DAY_COST =");
            int truckCost = lireLabel(br,"TRUCK_COST =");

            int techDistCost =  lireLabel(br,"TECHNICIAN_DISTANCE_COST =");
            int techDayCost =  lireLabel(br,"TECHNICIAN_DAY_COST =");
            int techCost =  lireLabel(br,"TECHNICIAN_COST =");

            waitLine(br,"MACHINES =");
            List<Machine> machines = lireMachines(br);

            waitLine(br,"LOCATIONS =");
            Map<Integer, Point> points = lirePoints(br);

            waitLine(br,"REQUESTS =");
            List<Client> clients = lireDemandes(br, points);

            Entrepot entrepot = lireEntrepot(points);
            waitLine(br,"TECHNICIANS =");

            List<Technicien> technicians = lireTechnicians(br, points);

            Instance instance = new Instance(dataSet,nom, nbDays, truckCapacity, truckMaxDistance, truckDistanceCost, truckDayCost,
                    truckCost, techDayCost, techDistCost, techCost, entrepot, machines);

            // on ajoute les clients
            for(Client c : clients) {
                instance.addClient(c);
            }

            // puis les techniciens
            for(Technicien t: technicians){
                instance.addTechnician(t);
            }

            br.close();
            f.close();
            return instance;

        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }

    /**
     * Permet d'atteindre la ligne contenant le label d??sir??
     * @param br lecteur courant du fichier d'instance
     * @param label le label ?? rechercher
     * @throws IOException
     */
    private void waitLine(BufferedReader br,String label) throws IOException {
        String line = null;
        line = br.readLine().trim();
        while(!line.contains(label)){
            line = br.readLine().trim();
        }
    }
    /**
     * Lecture du dataset de l'instance.
     * La ligne dans le fichier doit commencer par "NAME ="
     * @param br lecteur courant du fichier d'instance
     * @return le dataset de l'instance
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
     * Lecture du nom de l'instance.
     * La ligne dans le fichier doit commencer par "NAME ="
     * @param br lecteur courant du fichier d'instance
     * @return le nom de l'instance
     * @throws IOException
     */
    private String lireNom(BufferedReader br) throws IOException {
        String ligne = br.readLine().trim();
        while(!ligne.contains("NAME =")) {
            ligne = br.readLine().trim();
        }
        ligne = ligne.replace("NAME =", "");
        ligne = ligne.replace(" ", "");
        return ligne;
    }

    /**
     * Lis la valeur associ??e au label pass?? en param??tre
     * @param br lecteur courant du fichier d'instance
     * @param label le label d??sir??
     * @return la valeur associ??e au label
     * @throws IOException
     */
    private int lireLabel(BufferedReader br, String label) throws IOException{
        String ligne = br.readLine().trim();
        while(!ligne.contains(label)) {
            ligne = br.readLine().trim();
        }
        ligne = ligne.replace(label, "");
        ligne = ligne.replace(" ", "");
        return Integer.parseInt(ligne);
    }

    /**
     * Lecture des coordonees des points.
     * Chaque ligne contient : id, abscisse, ordonnee.
     * @param br lecteur courant du fichier d'instance
     * @return tous les points de l'instance, avec des ids uniques
     * @throws IOException
     */
    private Map<Integer,Point> lirePoints(BufferedReader br) throws IOException {
        Map<Integer, Point> points = new LinkedHashMap<>();
        String ligne = br.readLine().trim();

        while(!ligne.isEmpty()) {
            Point p = lireUnPoint(ligne);
            points.put(p.getId(), p);
            ligne = br.readLine().trim();
        }
        return points;
    }

    /**
     * Lecture d'un point de coordonn??es sur une ligne.
     * @param ligne ligne du fichier d'instance contenant un point avec : id,
     * abscisse, ordonnee
     * @return un point
     * @throws NumberFormatException
     */
    private Point lireUnPoint(String ligne) throws NumberFormatException {
        ligne = ligne.strip();
        String[] values = ligne.split("\\s+");
        int id = Integer.parseInt(values[0]);
        int x = Integer.parseInt(values[1]);
        int y = Integer.parseInt(values[2]);

        return new Point(id,x,y);
    }

    /**
     * Lecture des demandes des clients.
     * Cette methode doit etre appelee juste apres la methode lirePoints.
     * Seuls les clients avec une demande strictement positive sont renvoyes.
      * @param br le lecteur courant du fichier d'instance
      * @param points l'ensemble des points (lus avec la methode lirePoints)
      * @return l'ensemble des clients avec une demande strictement positive
      * @throws IOException
      */
     private List<Client> lireDemandes(BufferedReader br, Map<Integer, Point> points)
             throws IOException {
         Map<Integer, Client> clients = new LinkedHashMap<>();
         //List<Client> clients = new ArrayList<>();
         String ligne = br.readLine().trim();
         while (!ligne.isEmpty()) {
             Client c = lireUneDemande(ligne, points, clients);
             if (c != null) {
                 clients.put(c.getId(), c);
             }
             ligne = br.readLine().trim();
         }
         return new ArrayList<>(clients.values());
     }

    /**
     * Lecture d'un client avec ses informations associ??es.
     * @param ligne ligne du fichier de texte dans laquelle on a l'id du client
     * @param points tous les points de l'instance
     * @return un client avec demande positive, null si la demande est negative
     * ou nulle
     * @throws NumberFormatException
     */
    private Client lireUneDemande(String ligne, Map<Integer, Point> points, Map<Integer, Client> clients)
            throws NumberFormatException {

        String[] values = ligne.split("\\s+");
        int idDemand = Integer.parseInt(values[0]);
        int idClient = Integer.parseInt(values[1]);
        int firstDay = Integer.parseInt(values[2]);
        int lastDay = Integer.parseInt(values[3]);
        int idMachine = Integer.parseInt(values[4]);
        int nbMachine = Integer.parseInt(values[5]);
        Point p = points.get(idClient);

        Client c = clients.get(idClient);
        if (c == null)
            c = new Client(p.getId(), p.getX(), p.getY());

        c.addDemande(new Demande(idDemand, firstDay, lastDay, idMachine, nbMachine, c));
        return c;
    }

    /**
     * Lecture de l'entrepot
     * @param points l'ensemble des points enregistr??s
     * @return la localisation de l'entrpot
     */
    private Entrepot lireEntrepot(Map<Integer, Point> points) {
        Point p = points.get(1);
        return new Entrepot(p.getId(), p.getX(), p.getY());
    }

    /**
     * R??cup??re la liste des machines et leurs informations associ??es
     * @param br le lecteur courant du fichier d'instance
     * @return l'ensemble des machines
     * @throws IOException
     */
    private List<Machine> lireMachines(BufferedReader br) throws IOException {
        List<Machine> machines = new ArrayList();
        String ligne = br.readLine().trim();
        while(!ligne.isEmpty()) {
            Machine m = lireUneMachine(ligne);
            machines.add(m);
            ligne  = br.readLine().trim();
        }
        return machines;
    }

    /**
     * Lecture de une machine
     * @param ligne la ligne a traiter
     * @return la machine
     */
    private Machine lireUneMachine(String ligne) {
        String[] values = ligne.split("\\s+");
        int typeId = Integer.parseInt(values[0]);
        int size = Integer.parseInt(values[1]);
        int penality = Integer.parseInt(values[2]);
        return new Machine(typeId, size, penality);
    }

    /**
     * Lecture des techniciens et de leurs informations associ??s
     * @param br le lecteur courant du fichier d'instance
     * @param points l'ensemble des points d??j?? trait??s
     * @return l'ensemble des techniciens
     * @throws IOException
     */
    private List<Technicien> lireTechnicians(BufferedReader br, Map<Integer, Point> points)
            throws IOException {
        List<Technicien> technicians = new ArrayList<>();
        String ligne = br.readLine();
        while(ligne != null && !ligne.isEmpty()) {
            Technicien t = lireUnTechnician(ligne, points);
            if(t != null) {
                technicians.add(t);
            }
            ligne = br.readLine();
        }
        return technicians;
    }

    /**
     * Lecture d'un technicien
     * @param ligne la ligne a traiter
     * @param points l'ensemble des points d??j?? trait??s
     * @return l'ensemble des techniciens
     * @throws NumberFormatException
     */
    private Technicien lireUnTechnician(String ligne, Map<Integer, Point> points)
            throws NumberFormatException {
        String[] values = ligne.trim().split("\\s+");
        int idTechnicien = Integer.parseInt(values[0]);
        Point domicile = points.get(Integer.parseInt(values[1]));
        int distanceMax = Integer.parseInt(values[2]);
        int demandeMax = Integer.parseInt(values[3]);
        Map<Integer,Boolean> canInstall = new LinkedHashMap<>();

        for (int i = 4; i < values.length; i++) {
            canInstall.put(i-3, Integer.parseInt(values[i]) != 0);
        }

        Technicien technicien = new Technicien(idTechnicien, domicile, distanceMax, demandeMax, canInstall);

        return technicien;
    }


    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("exemple/testInstance.txt");
            Instance instance = reader.readInstance();
            System.out.println(instance);
            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
