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
        String instanceName = inputPath;
        this.instanceFile = new File(instanceName);
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

            // TO CHECK : constructeur de la classe Instance
            Instance instance = new Instance(nom, nbDays, truckCapacity, truckMaxDistance, truckDistanceCost, truckDayCost,
                    truckCost, techDayCost, techDistCost, techCost, entrepot);

            for(Client c : clients) {
                // TO CHECK : ajout d'un client dans la classe Instance
                instance.addClient(c);
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
     * Permet d'atteindre la ligne contenant le label désiré
     * @param br lecteur courant du fichier d'instance
     * @param label le label à rechercher
     * @throws IOException
     */
    private void waitLine(BufferedReader br,String label) throws IOException {
        String line = null;
        line = br.readLine();
        while(!line.contains(label)){
            line = br.readLine();
        }
    }

    /**
     * Lecture du nom de l'instance.
     * La ligne dans le fichier doit commencer par "NAME ="
     * @param br lecteur courant du fichier d'instance
     * @return le nom de l'instance
     * @throws IOException 
     */
    private String lireNom(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("NAME =")) {
            ligne = br.readLine();
        }
        ligne = ligne.replace(" ", "");
        ligne = ligne.replace("NAME =", "");
        return ligne;
    }

    /**
     * Lis la valeur associée au label passé en paramètre
     * @param br lecteur courant du fichier d'instance
     * @param label le label désiré
     * @return la valeur associée au label
     * @throws IOException
     */
    private int lireLabel(BufferedReader br, String label) throws IOException{
        String ligne = br.readLine();
        while(!ligne.contains(label)) {
            ligne = br.readLine();
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
        String ligne = br.readLine();

        while(!ligne.isEmpty()) {
            Point p = lireUnPoint(ligne);
            points.put(p.getId(), p);
            ligne = br.readLine();
        }
        return points;
    }

    /**
     * Lecture d'un point de coordonnées sur une ligne.
     * @param ligne ligne du fichier d'instance contenant un point avec : id,
     * abscisse, ordonnee
     * @return un point
     * @throws NumberFormatException
     */
    private Point lireUnPoint(String ligne) throws NumberFormatException {
        ligne = ligne.strip();
        String[] values = ligne.split(" ");
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
         String ligne = br.readLine();
         while (!ligne.isEmpty()) {
             Client c = lireUneDemande(ligne, points, clients);
             if (c != null) {
                 clients.put(c.getId(), c);
             }
             ligne = br.readLine();
         }
         return new ArrayList<>(clients.values());
     }
    
    /**
     * Lecture d'un client avec ses informations associées.
     * @param ligne ligne du fichier de texte dans laquelle on a l'id du client
     * @param points tous les points de l'instance
     * @return un client avec demande positive, null si la demande est negative
     * ou nulle
     * @throws NumberFormatException
     */
    private Client lireUneDemande(String ligne, Map<Integer, Point> points, Map<Integer, Client> clients)
            throws NumberFormatException {

        String[] values = ligne.split(" ");
        int idDemand = Integer.parseInt(values[0]);
        int idClient = Integer.parseInt(values[1]);
        int firstDay = Integer.parseInt(values[2]);
        int lastDay = Integer.parseInt(values[3]);
        int idMachine = Integer.parseInt(values[4]);
        int nbMachine = Integer.parseInt(values[5]);

        Demande demand = new Demande(idDemand, firstDay, lastDay, idMachine, nbMachine);

        Client c = clients.get(idClient);
        if(c != null) {
            c.addDemande(demand);
            return null;
        }

        Point p = points.get(idClient);
        c = new Client(p.getId(), p.getX(), p.getY());
        c.addDemande(demand);
        return c;
    }

    /**
     * Lecture de l'entrepot
     * @param points l'ensemble des points enregistrés
     * @return la localisation de l'entrpot
     */
    private Entrepot lireEntrepot(Map<Integer, Point> points) {
        Point p = points.get(1);
        return new Entrepot(p.getId(), p.getX(), p.getY());
    }

    /**
     * Récupére la liste des machines et leurs informations associées
     * @param br le lecteur courant du fichier d'instance
     * @return l'ensemble des machines
     * @throws IOException
     */
    private List<Machine> lireMachines(BufferedReader br) throws IOException {
        List<Machine> machines = new ArrayList();
        String ligne = br.readLine();
        while(ligne.isEmpty()) {
            Machine m = lireUneMachine(ligne);
            machines.add(m);
            ligne  = br.readLine();
        }
        return machines;
    }

    /**
     * Lecture de une machine
     * @param ligne la ligne a traiter
     * @return la machine
     */
    private Machine lireUneMachine(String ligne) {
        String[] values = ligne.split(" ");
        int typeId = Integer.parseInt(values[0]);
        int size = Integer.parseInt(values[1]);
        int penality = Integer.parseInt(values[2]);
        return new Machine(typeId, size, penality);
    }

    /**
     * Lecture des techniciens et de leurs informations associés
     * @param br le lecteur courant du fichier d'instance
     * @param points l'ensemble des points déjà traités
     * @return l'ensemble des techniciens
     * @throws IOException
     */
    private List<Technicien> lireTechnicians(BufferedReader br, Map<Integer, Point> points)
            throws IOException {
        List<Technicien> technicians = new ArrayList<>();
        String ligne = br.readLine();
        while(ligne!=null) {
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
     * @param points l'ensemble des points déjà traités
     * @return l'ensemble des techniciens
     * @throws NumberFormatException
     */
    private Technicien lireUnTechnician(String ligne, Map<Integer, Point> points)
            throws NumberFormatException {
        String[] values = ligne.split(" ");
        int idTechnicien = Integer.parseInt(values[0]);
        Point localisation = points.get(Integer.parseInt(values[1]));
        int distanceMax = Integer.parseInt(values[2]);
        int demandeMax = Integer.parseInt(values[3]);
        Map<Integer,Integer> canInstall = new LinkedHashMap<>();

        for (int i = 4; i < values.length; i++) {
            canInstall.put(i-3,Integer.parseInt(values[i]));
        }

        Technicien technicien = new Technicien(idTechnicien, localisation, distanceMax, demandeMax,canInstall);

        return technicien;
    }


    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("exemple/testInstance.txt");
            System.out.println(reader.readInstance().toString());
            System.out.println("Instance lue avec success !");
            Instance instance = reader.readInstance();
            System.out.println(instance);
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
