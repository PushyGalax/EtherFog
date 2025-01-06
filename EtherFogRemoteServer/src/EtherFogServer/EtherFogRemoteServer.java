// Server Code : Implements all the server functionality

package EtherFogServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe principale du serveur EtherFog.
 * Elle implémente les interfaces de contrôle et de gestion des clients.
 */
public class EtherFogRemoteServer implements EtherFogRemoteServerController, EtherFogRemoteServerClientManager {
    /** Port par défaut si aucun n'est spécifié */
    public static int DEFAULT_WORKING_PORT = 20000;

    /** Indique si le mode debug est activé */
    public static boolean DEBUG = true;
    
    /** Gestionnaire de la base de données */
    private EtherFogRemoteDatabaseManager db;

    /** Gestionnaire pour la communication série avec l'EtherFog */
    private EtherFogSerialManager EtherFog;

    /**
     * Constructeur principal pour initialiser le serveur.
     * Initialise la base de données et son schéma.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    public EtherFogRemoteServer() throws IOException {
        try {
            this.db = new EtherFogRemoteDatabaseManager("com.mckoi.JDBCDriver", "jdbc:mckoi://127.0.0.1/", "admin", "admin");
            this.db.createDatabaseSchema();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lance le serveur sur le port par défaut.
     *
     * @throws SQLException           si une erreur SQL se produit.
     * @throws ClassNotFoundException si une classe n'est pas trouvée.
     */
    public void serverRun() throws ClassNotFoundException, SQLException {
        this.serverRun(DEFAULT_WORKING_PORT);
    }

    /**
     * Lance le serveur sur un port spécifique.
     *
     * @param port Port sur lequel écouter les connexions.
     * @throws SQLException           si une erreur SQL se produit.
     * @throws ClassNotFoundException si une classe n'est pas trouvée.
     */
    public void serverRun(int port) throws ClassNotFoundException, SQLException {
        ServerSocket srvSocket = null;
        boolean running = true;
        try {
            DEBUG("Écoute sur le port " + port);
            srvSocket = new ServerSocket(port);
            EtherFog = new EtherFogSerialManager();
            EtherFog.connect("COM3"); // Connexion au port série
            while (running) {
                DEBUG("En attente de connexion...");
                Socket service = srvSocket.accept(); // Accepter une connexion
                DEBUG("Serveur connecté avec " + service.getInetAddress().getHostAddress() + ":" + service.getPort());
                (new EtherFogRemoteServerThread(service, this, this)).start(); // Lancer un nouveau thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche un message en mode debug.
     *
     * @param s Le message à afficher.
     */
    public static void DEBUG(String s) {
        if (EtherFogRemoteServer.DEBUG) System.out.println(s);
    }

    /** Méthodes implémentées de l'interface EtherFogRemoteServerController */

    @Override
    public String getColor(String m) {
        return EtherFog.getSerial(m);
    }

    @Override
    public void setCouleur(String col) {
        EtherFog.sendSerial(col);
    }

    @Override
    public void setCouleurDB(String name, String col) {
        try {
            this.db.insertOrUpdatePlayer(name, col);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String randomNumber(int r) {
        EtherFog.sendSerialn("RANDOM_NUMBER");
        return "" + (Math.round(Math.random() * (r - 1)) + 1);
    }

    @Override
    public void castSpell(String spell) {
        try {
            this.EtherFog.sendSerialn(this.db.getSpellColor(spell));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String listCharacter() {
        try {
            DEBUG(this.db.listAllPlayers().toString());
            ArrayList<String> perso = (ArrayList<String>) this.db.listAllPlayers();
            StringBuilder result = new StringBuilder();
            for (String elem : perso) {
                result.append(elem).append(" ");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setCharacterColor(String name) {
        try {
            this.EtherFog.sendSerialn(this.db.getPlayerColor(name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String listSpell() {
        try {
            DEBUG(this.db.listAllPlayers().toString());
            ArrayList<String> spell = (ArrayList<String>) this.db.listAllSpell();
            StringBuilder result = new StringBuilder();
            for (String elem : spell) {
                result.append(elem).append(";");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
