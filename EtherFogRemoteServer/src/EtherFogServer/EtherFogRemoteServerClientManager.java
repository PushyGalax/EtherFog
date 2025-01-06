package EtherFogServer;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface pour la gestion des clients dans EtherFogRemoteServer.
 * Cette interface définit une méthode permettant de gérer les couleurs des personnages en les enregistrant dans une base de données.
 */
public interface EtherFogRemoteServerClientManager {

    /**
     * Met à jour la couleur associée à un personnage dans la base de données.
     *
     * @param name Le nom du personnage pour lequel la couleur doit être mise à jour.
     * @param col  La nouvelle couleur du personnage, représentée sous forme de chaîne 
     *             (par exemple, un code RGB ou un nom de couleur).
     * @throws SQLException Si une erreur liée à la base de données survient lors de la mise à jour.
     */
    public void setCouleurDB(String name, String col);
}
