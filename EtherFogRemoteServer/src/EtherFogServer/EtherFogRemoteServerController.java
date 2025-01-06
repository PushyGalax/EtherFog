// Gère les EtherFog

package EtherFogServer;

import java.util.ArrayList;

/**
 * Interface pour le contrôle du serveur distant EtherFogRemoteServer.
 * Cette interface définit les méthodes permettant de gérer les couleurs, 
 * les sorts, les animations, et d'autres fonctionnalités spécifiques liées à EtherFog.
 */
public interface EtherFogRemoteServerController {

    /**
     * Définit la couleur courante pour le serveur.
     *
     * @param col Une chaîne représentant la couleur (par exemple, un code RGB ou un nom de couleur).
     */
    public void setCouleur(String col);

    /**
     * Récupère la couleur courante définie sur l'EtherFog.
     *
     * @param m Une chaîne représentant le contexte ou un paramètre pour la demande.
     * @return Une chaîne représentant la couleur actuelle.
     */
    public String getColor(String m);

    /**
     * Génère un nombre aléatoire et démarre une animation.
     *
     * @param r Un entier représentant une limite pour le nombre aléatoire.
     * @return Une chaîne représentant le nombre aléatoire généré.
     */
    public String randomNumber(int r);

    /**
     * Envoie la couleur associée à un sort spécifique.
     *
     * @param spell Une chaîne représentant le sort.
     */
    public void castSpell(String spell);

    /**
     * Liste les personnages disponibles ou définis.
     *
     * @return Une chaîne contenant la liste des personnages.
     */
    public String listCharacter();

    /**
     * Liste les sorts disponibles ou définis.
     *
     * @return Une chaîne contenant la liste des sorts.
     */
    public String listSpell();

    /**
     * Définit une couleur spécifique pour un personnage donné.
     *
     * @param name Le nom du personnage pour lequel la couleur doit être définie.
     */
    public void setCharacterColor(String name);
}
