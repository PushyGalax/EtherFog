// DB Manager : Utility for DB (mckoi)

package EtherFogServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de gestion de la base de données pour EtherFogRemoteServer.
 * Cette classe fournit des utilitaires pour interagir avec la base de données,
 * y compris la gestion des joueurs, des sorts, et la création du schéma initial.
 */
public class EtherFogRemoteDatabaseManager {
    private static Connection connection;

    /**
     * Constructeur de EtherFogRemoteDatabaseManager.
     * Initialise la connexion à la base de données en utilisant les paramètres fournis.
     *
     * @param driverClassName Le nom de la classe du driver JDBC.
     * @param databaseURL     L'URL de la base de données.
     * @param username        Le nom d'utilisateur pour la connexion.
     * @param password        Le mot de passe pour la connexion.
     * @throws ClassNotFoundException Si le driver JDBC n'est pas trouvé.
     * @throws SQLException           Si une erreur survient lors de la connexion.
     */
    public EtherFogRemoteDatabaseManager(String driverClassName, String databaseURL, String username, String password) throws ClassNotFoundException, SQLException {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.connection = null;
        try {
            this.connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les tables nécessaires dans la base de données si elles n'existent pas,
     * et insère les données initiales pour les sorts.
     *
     * @throws SQLException Si une erreur survient lors de l'exécution des requêtes SQL.
     * @throws IOException  Si une erreur survient lors de la lecture du fichier des sorts.
     */
    public void createDatabaseSchema() throws SQLException, IOException {
        try (Statement stmt = connection.createStatement()) {
            if (!tableExists("Player")) {
                stmt.executeUpdate("CREATE TABLE Player (" +
                        "pseudo VARCHAR(50) UNIQUE NOT NULL, " +
                        "color VARCHAR(15) NOT NULL)");
            }

            if (!tableExists("Spell")) {
                stmt.executeUpdate("CREATE TABLE Spell (" +
                        "name VARCHAR(50) UNIQUE NOT NULL, " +
                        "color VARCHAR(15) NOT NULL)");
            }
        }

        String insertQuery = "INSERT INTO Spell (name, color) VALUES (?, ?)";
        String checkQuery = "SELECT COUNT(*) FROM Spell WHERE name = ?";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             BufferedReader in = new BufferedReader(new FileReader("C:\\Users\\galax\\Desktop\\java\\EtherFogRemoteServer\\src\\EtherFogServer\\spell"))) {

            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 2) continue;

                String name = parts[0].trim();
                String color = parts[1].trim();

                checkStmt.setString(1, name);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        continue;
                    }
                }

                insertStmt.setString(1, name);
                insertStmt.setString(2, color);
                insertStmt.executeUpdate();
            }
        }
    }

    /**
     * Vérifie si une table existe dans la base de données.
     *
     * @param tableName Le nom de la table à vérifier.
     * @return True si la table existe, sinon False.
     * @throws SQLException Si une erreur survient lors de l'accès aux métadonnées.
     */
    private boolean tableExists(String tableName) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }

    /**
     * Insère ou met à jour la couleur d'un joueur dans la base de données.
     *
     * @param pseudo Le pseudo du joueur.
     * @param color  La couleur à associer au joueur.
     * @throws SQLException Si une erreur survient lors de la requête SQL.
     */
    public void insertOrUpdatePlayer(String pseudo, String color) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Player WHERE pseudo = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, pseudo);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    String updateQuery = "UPDATE Player SET color = ? WHERE pseudo = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, color);
                        updateStmt.setString(2, pseudo);
                        updateStmt.executeUpdate();
                    }
                } else {
                    String insertQuery = "INSERT INTO Player (pseudo, color) VALUES (?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, pseudo);
                        insertStmt.setString(2, color);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }

    /**
     * Récupère la couleur associée à un joueur donné.
     *
     * @param pseudo Le pseudo du joueur.
     * @return La couleur du joueur, ou null si le joueur n'est pas trouvé.
     * @throws SQLException Si une erreur survient lors de la requête SQL.
     */
    public String getPlayerColor(String pseudo) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT color FROM Player WHERE pseudo = ?")) {
            pstmt.setString(1, pseudo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("color");
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Liste tous les pseudos des joueurs enregistrés.
     *
     * @return Une liste contenant les pseudos des joueurs.
     * @throws SQLException Si une erreur survient lors de la requête SQL.
     */
    public List<String> listAllPlayers() throws SQLException {
        List<String> players = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT pseudo FROM Player")) {
            while (rs.next()) {
                players.add(rs.getString("pseudo"));
            }
        }
        return players;
    }

    /**
     * Récupère la couleur associée à un spell donné.
     *
     * @param spellName Le sort voulu.
     * @return La couleur du sort, ou null si le sort n'est pas trouvé.
     * @throws SQLException Si une erreur survient lors de la requête SQL.
     */
    public String getSpellColor(String spellName) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT color FROM Spell WHERE name = ?")) {
            pstmt.setString(1, spellName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("color");
                } else {
                    return null; // Spell non trouvé
                }
            }
        }
    }
    
    /**
     * Liste tous les sorts enregistrés.
     *
     * @return Une liste contenant les sorts.
     * @throws SQLException Si une erreur survient lors de la requête SQL.
     */
    public List<String> listAllSpell() throws SQLException {
        List<String> spell = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM Spell")) {
            while (rs.next()) {
                spell.add(rs.getString("name"));
            }
        }
        return spell;
    }
}
