//    Serial manager program : Communicate with the microship

package EtherFogServer;

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Gestionnaire pour la communication série avec un périphérique Arduino.
 * Cette classe permet de se connecter à un port série, envoyer des commandes,
 * recevoir des réponses, et fermer la connexion.
 */
public class EtherFogSerialManager {
    // Référence au port série utilisé pour communiquer avec l'Arduino.
    SerialPort arduinoPort;

    /**
     * Connecte le gestionnaire à un port série spécifique.
     *
     * @param Port Nom du port série (par exemple, "COM3" sur Windows ou "/dev/ttyUSB0" sur Linux).
     */
    public void connect(String Port) {
        arduinoPort = SerialPort.getCommPort(Port); // Configuration du port série
        arduinoPort.setBaudRate(9600); // Débit en bauds (bits par seconde)

        // Tentative d'ouverture du port série
        if (!arduinoPort.openPort()) {
            System.out.println("Impossible d'ouvrir le port série.");
            return;
        }

        System.out.println("Port série ouvert avec succès.");
    }

    /**
     * Envoie une commande série à l'Arduino et attend une réponse.
     *
     * @param colors Données à envoyer au périphérique sous forme de chaîne.
     */
    public void sendSerial(String colors) {
        try {
            // Envoi des données à l'Arduino
            arduinoPort.getOutputStream().write((colors + "\n").getBytes());
            arduinoPort.getOutputStream().flush();

            System.out.println("Couleurs envoyées : " + colors);

            // Attente et lecture de la réponse
            arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 0); // Timeout personnalisé
            byte[] buffer = new byte[1024];
            int bytesRead = arduinoPort.getInputStream().read(buffer);

            if (bytesRead > 0) {
                String response = new String(buffer, 0, bytesRead);
                System.out.println("Réponse de l'Arduino : " + response);
            } else {
                System.out.println("Aucune réponse reçue de l'Arduino.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie une commande série sans attendre de réponse.
     *
     * @param colors Données à envoyer au périphérique sous forme de chaîne.
     */
    public void sendSerialn(String colors) {
        try {
            // Envoi des données à l'Arduino
            arduinoPort.getOutputStream().write((colors + "\n").getBytes());
            arduinoPort.getOutputStream().flush();

            System.out.println("Couleurs envoyées : " + colors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envoie une commande série et retourne la réponse de l'Arduino.
     *
     * @param message Message à envoyer.
     * @return La réponse reçue du périphérique, ou une chaîne vide si aucune réponse n'est reçue.
     */
    public String getSerial(String message) {
        try {
            // Envoi du message à l'Arduino
            arduinoPort.getOutputStream().write((message + "\n").getBytes());
            arduinoPort.getOutputStream().flush();

            System.out.println("Message envoyé : " + message);

            // Attente et lecture de la réponse
            arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 0); // Timeout personnalisé
            byte[] buffer = new byte[1024];
            int bytesRead = arduinoPort.getInputStream().read(buffer);

            if (bytesRead > 0) {
                String response = new String(buffer, 0, bytesRead);
                System.out.println("Réponse de l'Arduino : " + response);
                return response;
            } else {
                System.out.println("Aucune réponse reçue de l'Arduino.");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Ferme la connexion au port série.
     */
    public void close() {
        arduinoPort.closePort();
        System.out.println("Port série fermé.");
    }

    /**
     * Génère une chaîne de caractères représentant une couleur RGB aléatoire pour 14 LEDs.
     *
     * @return Une chaîne contenant 42 valeurs RGB (3 par LED, 14 LEDs).
     */
    public static String randomColor() {
        StringBuilder color = new StringBuilder();
        for (int i = 0; i < 3 * 14; i++) {
            color.append(Math.round(Math.random() * 255));
            if (i < 3 * 14 - 1) {
                color.append(" ");
            }
        }
        return color.toString();
    }
}

