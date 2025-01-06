// Server Thread : Is the instance for every client

package EtherFogServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Classe représentant un thread serveur dédié à chaque client.
 * Cette classe gère les interactions entre le client et le serveur.
 */
public class EtherFogRemoteServerThread extends Thread {
    /** Retour chariot pour compatibilité entre les systèmes WINDOWS et LINUX */
    public static String RC;

    /** Socket de communication avec le client */
    private Socket service;

    /** Gestionnaire des interactions avec la base de données pour le client */
    private EtherFogRemoteServerClientManager clientmanager;

    /** Contrôleur principal pour gérer les actions dans le jeu */
    private EtherFogRemoteServerController etherfogcontroller;

    /** Flux d'entrée pour recevoir les messages du client */
    private BufferedReader in;

    /** Flux de sortie pour envoyer des messages au client */
    private PrintStream out;

    /**
     * Constructeur du thread serveur.
     *
     * @param service             Le socket de communication avec le client.
     * @param clientmanager       Le gestionnaire des interactions client/serveur.
     * @param etherfogcontroller  Le contrôleur principal du jeu.
     */
    public EtherFogRemoteServerThread(Socket service, EtherFogRemoteServerClientManager clientmanager, EtherFogRemoteServerController etherfogcontroller) {
        this.service = service;
        this.clientmanager = clientmanager;
        this.etherfogcontroller = etherfogcontroller;
    }

    /**
     * Envoie un message au client.
     *
     * @param msg Le message à envoyer.
     */
    public void send(String msg) {
        this.out.println(msg);
    }

    /**
     * Point d'entrée principal du thread pour gérer les interactions client.
     */
    @Override
    public void run() {
        boolean fini = false;
        try {
            if (EtherFogRemoteServer.DEBUG) {
                System.out.println("Serveur thread connecté avec " + service.getInetAddress().getHostAddress() + ":" + service.getPort());
            }

            this.in = new BufferedReader(new InputStreamReader(service.getInputStream()));
            this.out = new PrintStream(service.getOutputStream());

            while (!fini) {
                // Lecture de la requête client
                String requete = in.readLine();
                if (requete != null) {
                    String[] arrayRequete = requete.split(" ");
                    System.out.println("Requête reçue : " + requete);

                    boolean validate = true; // Validation simplifiée pour l'exemple
                    // Reste a implémenter dans une version future : vérification de la véracité des requètes selon le modèle suit :
//                  boolean validate = false;										//test de si la requète est juste
//					this.send(requete + "	Vérification");
//
//					String requeteValidation = in.readLine();
//					System.out.println(requeteValidation + "  Validation ");
//					if (requeteValidation.equals("1")) {
//						validate = true;
//					}

                    if (validate) {
                        if (arrayRequete[0].equals("0")) { // Actions du maître de jeu
                            System.out.println("State = 0 (Maître de jeu)");
                            switch (arrayRequete[1]) {
                                case "0": // Définir une couleur
                                    handleSetColor(arrayRequete);
                                    break;
                                case "1": // Générer un nombre aléatoire
                                    out.println(this.etherfogcontroller.randomNumber(Integer.parseInt(arrayRequete[2])));
                                    break;
                                case "2": // Définir la couleur d'un personnage
                                    this.etherfogcontroller.setCharacterColor(arrayRequete[2]);
                                    break;
                                case "3": // Lister les personnages
                                    out.println(this.etherfogcontroller.listCharacter());
                                    break;
                            }
                        } else if (arrayRequete[0].equals("1")) { // Actions des joueurs
                            System.out.println("State = 1 (Joueur)");
                            switch (arrayRequete[1]) {
                                case "0": // Définir la couleur du joueur
                                    handlePlayerSetColor(arrayRequete);
                                    break;
                                case "1": // Lancer un sort
                                    handleCastSpell(arrayRequete);
                                    break;
                                case "2": // Lister les sorts
                                    out.println(this.etherfogcontroller.listSpell());
                                    break;
                            }
                        } else if (arrayRequete[0].equals("8")) { // Obtenir les couleurs
                            this.send(this.etherfogcontroller.getColor("GET_COLORS"));
                        } else {
                            System.out.println("Commande non comprise : " + requete);
                            this.send("300");
                        }
                    } else {
                        System.out.println("Commande invalide");
                        this.send("404");
                    }
                }
            }

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère l'action de définir une couleur pour le maître de jeu.
     *
     * @param arrayRequete Les paramètres de la requête.
     */
    private void handleSetColor(String[] arrayRequete) {
        StringBuilder col = new StringBuilder();
        for (String elem : arrayRequete) {
            col.append(elem).append(" ");
        }
        col.deleteCharAt(col.length() - 1).delete(0, 4);
        System.out.println("Couleur définie : " + col.toString());
        this.etherfogcontroller.setCouleur(col.toString());
    }

    /**
     * Gère l'action de définir une couleur pour un joueur.
     *
     * @param arrayRequete Les paramètres de la requête.
     */
    private void handlePlayerSetColor(String[] arrayRequete) {
        StringBuilder col = new StringBuilder();
        for (int i = 3; i < arrayRequete.length; i++) {
            col.append(arrayRequete[i]).append(" ");
        }
        col.deleteCharAt(col.length() - 1);
        System.out.println("Couleur définie pour le joueur : " + col.toString());
        this.clientmanager.setCouleurDB(arrayRequete[2], col.toString());
    }

    /**
     * Gère l'action de lancer un sort.
     *
     * @param arrayRequete Les paramètres de la requête.
     */
    private void handleCastSpell(String[] arrayRequete) {
        StringBuilder spell = new StringBuilder();
        for (int i = 2; i < arrayRequete.length; i++) {
            spell.append(arrayRequete[i]).append(" ");
        }
        spell.deleteCharAt(spell.length() - 1);
        System.out.println("Sort lancé : " + spell.toString());
        this.etherfogcontroller.castSpell(spell.toString());
    }
}
