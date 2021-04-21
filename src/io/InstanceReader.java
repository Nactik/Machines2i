/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

// TO CHECK : import des classes Instance, Client, Depot et Point
import instance.Instance;
import instance.model.Demande;
import instance.reseau.Client;
import instance.reseau.Entrepot;
import instance.reseau.Point;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;
import org.w3c.dom.ls.LSOutput;

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
 * Cette classe permet de lire une instance pour les TPs du cours de LE4-SI
 * POO pour l'optimisation.
 * 
 * Les instances sont fournies sur moodle au format ".vrp".
 * 
 * Pour que le lecteur d'instance fonctionne correctement, il faut que les 
 * signatures des constructeurs des classes Depot, Client, et Instances, ainsi 
 * que la methode ajouterClient de la classe Instance soient bien conformes a 
 * la description dans le sujet du TP.
 * Des commentaires annotes avec 'TO CHECK' vous permettent de facilement reperer
 * dans cette classe les lignes que vous devez verifier et modifier si besoin. 
 * 
 * @author Maxime Ogier
 */
public class InstanceReader {
    /**
     * Le fichier contenant l'instance.
     */
    private File instanceFile;
    
    /**
     * Constructeur par donnee du chemin du fichier d'instance.
     * @param inputPath le chemin du fichier d'instance, qui doit se terminer 
     * par l'extension du fichier (.xml).
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
            String nom = lireNom(br); //OK
            int nbDays = readDays(br); //OK
            int capacite = lireCapacite(br);
            Map<Integer, Point> points = lirePoints(br); //OK
            List<Client> clients = lireDemandes(br, points);
            Depot depot = lireDepot(br, points);
            // TO CHECK : constructeur de la classe Instance
            Instance instance = new Instance(nom, capacite, depot);
            for(Client c : clients) {
                // TO CHECK : ajout d'un client dans la classe Instance
                instance.ajouterClient(c);
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
     * Lecture du nom de l'instance.
     * La ligne dans le fichier doit commencer par "NAME :"
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
     * Lecture du nb de jours de l'instance.
     * La ligne dans le fichier doit commencer par "DAYS ="
     * @param br lecteur courant du fichier d'instance
     * @return le nombre de jours
     * @throws IOException
     */
    private int readDays(BufferedReader br) throws IOException{
        String line = br.readLine();
        while(!line.contains("DAYS ="))
            line = br.readLine();

        line = line.replace(" ", "");
        line = line.replace("DAYS =", "");

        return Integer.parseInt(line);
    }

    /**
     * Lecture des coordonees des points.
     * La section dans le fichier doit commencer par "NODE_COORD_SECTION"
     * puis chaque ligne contient : id, abscisse, ordonnee.
     * La section se termine par "DEMAND_SECTION"
     * @param br lecteur courant du fichier d'instance
     * @return tous les points de l'instance, avec des ids uniques
     * @throws IOException 
     */
    private Map<Integer,Point> lirePoints(BufferedReader br) throws IOException {
        Map<Integer, Point> points = new LinkedHashMap<>();
        String ligne = br.readLine();
        while(!ligne.contains("LOCATIONS =")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();

        while(!ligne.contains("REQUESTS =")) {
            Point p = lireUnPoint(ligne);
            points.put(p.getId(), p);
            ligne = br.readLine();
        }
        return points;
    }

    /**
     * Lecture d'un point sur une ligne.
     * @param ligne ligne du fichier d'instance contenant un point avec : id,
     * abscisse, ordonnee
     * @return un point (client avec demande de 0)
     * @throws IOException
     * @throws NumberFormatException
     */
    private Point lireUnPoint(String ligne) throws IOException, NumberFormatException {
        ligne = ligne.strip();
        String[] values = ligne.split(" |\t");
        int id = Integer.parseInt(values[0]);
        int x = Integer.parseInt(values[1]);
        int y = Integer.parseInt(values[2]);
        // TO CHECK : constructeur de la classe Client
        // ordre des paramètres : quantite, identifiant, abscisse, ordonnee
        return new Client(id,x,y,0);
    }
    
    /**
     * Lecture de la capacite de camions.
     * La ligne doit commencer par "CAPACITY :"
     * @param br le lecteur courant du fichier d'instance
     * @return la capacite des camions
     * @throws IOException 
     */
    private int lireCapacite(BufferedReader br) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("CAPACITY :")) {
            ligne = br.readLine();
        }
        ligne = ligne.replace(" ", "");
        ligne = ligne.replace("CAPACITY:", "");
        ligne = ligne.trim();
        int capacite = Integer.parseInt(ligne);
        return capacite;
    }
    
    /**
     * Lecture des demandes des clients.
     * Cette methode doit etre appelee juste apres la methode lirePoints.
     * La lecture se termine par la ligne "DEPOT_SECTION".
     * Seuls les clients avec une demande strictement positive sont renvoyes.
     * @param br le lecteur courant du fichier d'instance
     * @param points l'ensemble des points (lus avec la methode lirePoints)
     * @return l'ensemble des clients avec une demande strictement positive
     * @throws IOException 
     */
    private List<Client> lireDemandes(BufferedReader br, Map<Integer, Point> points) 
            throws IOException {
        List<Client> clients = new ArrayList<>();
        String ligne = br.readLine();
        while(!ligne.contains("REQUESTS = ")) {
            Client c = lireUneDemande(ligne, points);
            if(c != null) {
                clients.add(c);
            }
            ligne = br.readLine();
        }
        return clients;
    }
    
    /**
     * Lecture d'un client avec sa demande.
     * A partir de l'id du client, on recupere le point correspondant, et on cree
     * un client avec les ccaracteristiques du point et sa demande.
     * @param ligne ligne du fichier de texte dans laquelle on a l'id du client
     * et sa demande
     * @param points tous les points de l'instance
     * @return un client avec demande positive, null si la demande est negative
     * ou nulle
     * @throws IOException
     * @throws NumberFormatException
     */
    private Client lireUneDemande(String ligne, Map<Integer, Point> points)
            throws IOException, NumberFormatException {
        ligne = ligne.strip();
        String[] values = ligne.split(" |\t");
        int idDemande = Integer.parseInt(values[0]);
        int idClient = Integer.parseInt(values[1]);
        int firstDay = Integer.parseInt(values[2]);
        int lastDay = Integer.parseInt(values[3]);
        int idMachine = Integer.parseInt(values[4]);
        int nbMachine = Integer.parseInt(values[5]);

        Demande demande = new Demande(idDemande, idMachine, nbMachine, firstDay, lastDay);

        Point p = points.get(idClient);

        // TO CHECK : constructeur de la classe Client
        // ordre des paramètres : quantite, identifiant, abscisse, ordonnee
        return new Client(p.getId(), p.getX(), p.getY(), demande);
    }
    
    /**
     *
     * Lecture du depot.
     * Parmi les point de l'instance, on choisit celui dont l'id est celui du depot.
     * Cette methode doit etre appelee juste apres la methode lireDemandes
     * @param br le lecteur courant du fichier d'instance
     * @param points les points de l'instance (lus avec la methode lirePoints)
     * @return le depot de l'instance
     * @throws IOException 
     */
    private Entrepot lireDepot(BufferedReader br, Map<Integer, Point> points) throws IOException {
        Point p = points.get(1);
        // TO CHECK : constructeur de la classe Depot
        // ordre des paramètres : identifiant, abscisse, ordonnee
        return new Entrepot(p.getId(), p.getX(), p.getY());
    }

    private Client lireMachine(String ligne, Map<Integer, Point> points)
            throws IOException, NumberFormatException {
        ligne = ligne.strip();
        String[] values = ligne.split(" |\t");
        int idDemande = Integer.parseInt(values[0]);
        int idClient = Integer.parseInt(values[1]);
        int firstDay = Integer.parseInt(values[2]);
        int lastDay = Integer.parseInt(values[3]);
        int idMachine = Integer.parseInt(values[4]);
        int nbMachine = Integer.parseInt(values[5]);

        Point p = points.get(id);
        // TO CHECK : constructeur de la classe Client
        // ordre des paramètres : quantite, identifiant, abscisse, ordonnee
        return new Client(p.getId(), p.getAbscisse(), p.getOrdonnee(), q);
    }
    
    /**
     * Test de lecture d'une instance.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/A-n32-k5.vrp");
            System.out.println(reader.readInstance().toString());
            System.out.println("Instance lue avec success !");
        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
