// Main program : Start EtherFog Remote Controller

package EtherFogServer;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe principale pour lancer le serveur EtherFogRemoteServer.
 * Ce programme initialise et démarre un serveur distant.
 */
public class Main {

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Les arguments de ligne de commande (non utilisés dans cette version).
     * @throws ClassNotFoundException Si une classe requise n'est pas trouvée dans le chemin de classe.
     * @throws SQLException           En cas de problème avec la base de données.
     * @throws IOException            En cas de problème d'entrée/sortie.
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

        // Affichage des informations de copyright
        System.out.println("===========================================================");
        System.out.println("Projet RT2 SAE 3.3 - EtherFog Remote Controller");
        System.out.println("Développé par Bermond Michel");
        System.out.println("Université : IUT Valence - Université Grenoble Alpes (UGA)");
        System.out.println("===========================================================");

        // Création d'une instance du serveur EtherFogRemoteServer
        EtherFogRemoteServer server = new EtherFogRemoteServer();

        // Démarrage du serveur
        server.serverRun();
    }
}
