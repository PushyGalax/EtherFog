FRENCH :

PROJET :
RT2 SAE 3.3 - EtherFog Remote Controller
Développé par Bermond Michel
Université : IUT Valence - Université Grenoble Alpes (UGA)

Projet de la Situation d'apprentissage et d'évaluation du semestre 3 du BUT Réseaux et Télécommunication pour l'IUT de Valence. Celui ci consiste en la réalisation d'un objet connecté, ici l'EtherFog, qui est un objet de décoration pour le jeu de rôle sur table.

DISCLAIMER :
Il s'agit d'un projet universitaire à but éducatif, il s'agit d'un projet Work In Progress. Il y aura surement des mises à jour afin de le finaliser, de porter le projet jusqu'à sa fin et son utilisations optimale. Je reste ouvert à tout retour sur des bugs trouvé ou des possibilité d'optimisation.

DESCRIPTION : 
Il y a trois grand code, l'application mobile, le serveur et le microcontrolleur type Arduino. 
	Ici, l'application mobile sert de télécommande pour l'objet connecté. Le choix du développement d'application mobile sous Android Studio a pour but de m'apprendre le développement mobile et de me perfectionner dans ce monde que je ne connais pas. Cette application à pour objectif de contrôler la couleur des leds ou l'entrée dans les bases de données. Nous retrouvons en difficulté les échanges réseaux entre l'application et le serveur du à la découverte du développement mobile. 
	Ensuite nous avons le serveur en java. Son objectif est de faire le lien entre l'application mobile et l'Arduino. Le choix de java est pour mon éducation et le plus gros défie à était de communiquer entre le serveur et l'Arduino pour encore une fois la découverte de la communication entre le serveur et l'arduino. 
	Enfin nous retrouvons le code Arduino dont son objectif est de contrôler les leds. Il a comme communication de modifier les couleurs des leds et de transmettre l'état des leds. En difficulté nous avons le fait que c'était la première fois que je codais en Arduino. 

COMPOSANT :
Arduino Uno
Brumisateur à ultrason
Ruban de minimum 14 leds WS2812B

INSTALLATION ET EXECUTION :
Dans un premier temps, nous devons lancer le code Arduino. Il faut qu'il y est le module FastLED d'installer. Une fois le module installer et l'Arduino lancé, nous allons devoir lancer le serveur. Il est important que le serveur soit l'appareil sur lequel est branché l'Arduino. Sur le serveur, il faut avoir une base de donné mckoi avec en login:mot de passe -> admin:admin Les identifiants sont modifiable dans le fichier EtherFogRemoteServer.java ligne 37. Après avoir lancer la BDD mckoi via la commande "java -jar ./mckoidb.jar" nous devons choisir le port de l'Arduino toujours dans le fichier EtherFogRemoteServer.java à la ligne 68. Le serveur necessite aussi le module jSerialComm. Vous trouverez tous les fichier java des modules dans le repertoire module. Il faut aussi penser au port du serveur pour qu'il soit accessible en local ou à distance en fonction de vos préférence, le port est modifiable dans le fichier Main.java à la ligne 35 en entrant le port en paramètre de server.serverRun(port);. Enfin, une fois que la console affiche "En attente de connexion..." cela signifie que le serveur est en fonctionnement, il ne reste plus qu'à lancer l'application mobile. Une fois l'application installé, nous avons juste à la lancer et nous retrouvons à son démarrage l'entrée de l'adresse IP et le port puis enfin nous choisissons notre rôle. Pour l'installation, vous trouverez un apk dans le chemin d'accès suivant : ".\EtherFogRemoteController\app\release\app-release.apk"

MISE EN SITUATION :
Si nous choisissons l'option joueur, nous pouvons paramètrer notre couleur de joueur ou lors d'un combat lancer un sort via la liste des sorts disponnible.
Si nous choisissons l'option maitre de jeu, nous pouvons choisir la couleur de chacune des leds pour donner une ambiance, choisir un joueur pour mettre les leds à la couleur du joueur pour signifier qu'il s'agit de son tour de jeu, ou enfin générer un nombre aléatoire dans une plage que nous choisissons et lancer l'annimation.

PISTE D'AMELIORATION :
Ajouter des sécurités
Finir le système de vérification des requètes
Ampêcher le joueur de lancer un sort si ce n'est pas son tour
Ajouter plus de sorts
Permettre au maître de jeu de faire des presets de couleur pour des ambiances

____________________________________________________________________________________

ENGLISH :

PROJECT:
RT2 SAE 3.3 - EtherFog Remote Controller
Developed by Bermond Michel
University: IUT Valence - Université Grenoble Alpes (UGA)

Project of the Learning and Assessment Situation of semester 3 of the BUT Networks and Telecommunications for the IUT of Valence. This consists of the creation of a connected object, here the EtherFog, which is a decorative object for the tabletop role-playing game.

DISCLAIMER:
This is a university project for educational purposes, it is a Work In Progress project. There will surely be updates in order to finalize it, to bring the project to its end and its optimal uses. I remain open to any feedback on bugs found or optimization possibilities.

DESCRIPTION:
There are three major codes, the mobile application, the server and the Arduino type microcontroller.

Here, the mobile application serves as a remote control for the connected object. The choice of mobile application development under Android Studio is intended to teach me mobile development and to improve myself in this world that I do not know. This application aims to control the color of the LEDs or the entry into the databases. We find in difficulty the network exchanges between the application and the server due to the discovery of mobile development.
Then we have the server in Java. Its objective is to make the link between the mobile application and the Arduino. The choice of Java is for my education and the biggest challenge was to communicate between the server and the Arduino for once again the discovery of the communication between the server and the Arduino.
Finally we find the Arduino code whose objective is to control the LEDs. Its communication is to modify the colors of the LEDs and to transmit the state of the LEDs. In difficulty we have the fact that it was the first time that I coded in Arduino.

COMPONENT:
Arduino Uno
Ultrasonic fogger
Strip of at least 14 WS2812B LEDs

INSTALLATION AND EXECUTION:
First, we must launch the Arduino code. The FastLED module must be installed. Once the module is installed and the Arduino launched, we will have to launch the server. It is important that the server is the device on which the Arduino is connected. On the server, you must have a mckoi database with login: password -> admin: admin The identifiers can be modified in the EtherFogRemoteServer.java file line 37. After launching the mckoi database via the command "java -jar ./mckoidb.jar" we must choose the Arduino port still in the EtherFogRemoteServer.java file on line 68. The server also requires the jSerialComm module. You will find all the java files of the modules in the module directory. You must also think about the server port so that it is accessible locally or remotely depending on your preferences, the port can be modified in the Main.java file on line 35 by entering the port as a parameter of server.serverRun(port);. Finally, once the console displays "Waiting for connection..." this means that the server is running, all that remains is to launch the mobile application. Once the application is installed, we just have to launch it and we find at its startup the entry of the IP address and the port then finally we choose our role. For the installation, you will find an apk in the following path: ".\EtherFogRemoteController\app\release\app-release.apk"

SITUATION:
If we choose the player option, we can set our player color or during a fight cast a spell via the list of available spells.
If we choose the game master option, we can choose the color of each of the LEDs to give an atmosphere, choose a player to set the LEDs to the player's color to indicate that it is his turn, or finally generate a random number in a range that we choose and launch the animation.

TRACK FOR IMPROVEMENT:
Add security
Finish the request verification system
Prevent the player from casting a spell if it is not his turn
Add more spells
Allow the game master to make color presets for atmospheres