#include <FastLED.h>

// Définir le nombre de LEDs et le pin de données
#define NUM_LEDS 14
#define DATA_PIN 5

// Création d'un tableau pour les LEDs
CRGB leds[NUM_LEDS];
CRGB targetLeds[NUM_LEDS];
CRGB savedState[NUM_LEDS]; // Sauvegarder l'état des LEDs

void setup() {
  // Initialisation des LEDs
  FastLED.addLeds<WS2812B, DATA_PIN, GRB>(leds, NUM_LEDS);
  FastLED.clear(); // Assure que toutes les LEDs sont éteintes au démarrage
  FastLED.show();

  // Initialiser la communication série
  Serial.begin(9600);
}

void smoothTransition() {
  bool changed = false;
  for (int i = 0; i < NUM_LEDS; i++) {
    if (leds[i].r != targetLeds[i].r) {
      leds[i].r += (leds[i].r < targetLeds[i].r) ? 1 : -1;
      changed = true;
    }
    if (leds[i].g != targetLeds[i].g) {
      leds[i].g += (leds[i].g < targetLeds[i].g) ? 1 : -1;
      changed = true;
    }
    if (leds[i].b != targetLeds[i].b) {
      leds[i].b += (leds[i].b < targetLeds[i].b) ? 1 : -1;
      changed = true;
    }
  }
  if (changed) {
    FastLED.show();
    delay(10); // Ajustez pour contrôler la vitesse de transition
  }
}

void smoothTransitionAnimation() {
  bool changed = false;
  for (int i = 0; i < NUM_LEDS; i++) {
    if (leds[i].r != targetLeds[i].r) {
      leds[i].r += (leds[i].r < targetLeds[i].r) ? 1 : -1;
      changed = true;
    }
    if (leds[i].g != targetLeds[i].g) {
      leds[i].g += (leds[i].g < targetLeds[i].g) ? 1 : -1;
      changed = true;
    }
    if (leds[i].b != targetLeds[i].b) {
      leds[i].b += (leds[i].b < targetLeds[i].b) ? 1 : -1;
      changed = true;
    }
  }
  if (changed) {
    FastLED.show();
    delay(1); // Ajustez pour contrôler la vitesse de transition
  }
}

String getLedColors() {
  String colorData = "";
  for (int i = 0; i < NUM_LEDS; i++) {
    colorData += String(leds[i].r) + " " + String(leds[i].g) + " " + String(leds[i].b);
    if (i < NUM_LEDS - 1) {
      colorData += " ";
    }
  }
  return colorData;
}

void saveLedState() {
  for (int i = 0; i < NUM_LEDS; i++) {
    savedState[i] = leds[i];
  }
}

void restoreLedState() {
  for (int i = 0; i < NUM_LEDS; i++) {
    targetLeds[i] = savedState[i];
  }
}

void spinningAnimationWithFlash() {
  // Animation de rotation des couleurs des LEDs
  for (int round = 0; round < NUM_LEDS; round++) {
    CRGB lastColor = leds[NUM_LEDS - 1]; // Sauvegarde la dernière LED
    for (int i = NUM_LEDS - 1; i > 0; i--) {
      targetLeds[i] = leds[i - 1]; // Décale les couleurs vers la droite
    }
    targetLeds[0] = lastColor; // Replace la dernière couleur au début

    // Transition douce pour cette étape
    while (memcmp(leds, targetLeds, sizeof(leds)) != 0) {
      smoothTransitionAnimation();
    }
  }

  // Effet de flash
  saveLedState();
  for (int i = 0; i < NUM_LEDS; i++) {
    targetLeds[i] = CRGB::White;
  }
  while (memcmp(leds, targetLeds, sizeof(leds)) != 0) {
    smoothTransitionAnimation();
  }
  delay(100);

  restoreLedState();
  while (memcmp(leds, targetLeds, sizeof(leds)) != 0) {
    smoothTransitionAnimation();
  }
}


void loop() {
  // Vérifier si des données sont disponibles sur la liaison série
  if (Serial.available() > 0) {
    String input = Serial.readStringUntil('\n'); // Lire la ligne jusqu'au saut de ligne
    input.trim(); // Supprimer les espaces inutiles

    if (input == "GET_COLORS") {
      String currentColors = getLedColors();
      Serial.println(currentColors);
      return;
    }

    if (input == "RANDOM_NUMBER") {
      spinningAnimationWithFlash();
      return;
    }

    int values[NUM_LEDS * 3]; // Tableau pour stocker les valeurs RGB
    int index = 0;
    char* token = strtok(input.c_str(), " ");

    // Parcourir et extraire les valeurs RGB pour chaque LED
    while (token != NULL && index < NUM_LEDS * 3) {
      values[index++] = atoi(token);
      token = strtok(NULL, " ");
    }

    if (index == NUM_LEDS * 3) { // Vérifier si toutes les valeurs nécessaires ont été fournies
      for (int i = 0; i < NUM_LEDS; i++) {
        targetLeds[i] = CRGB(values[i * 3], values[i * 3 + 1], values[i * 3 + 2]);
      }
      //Serial.println("Nouvelle couleur cible définie.");
    } else {
      //Serial.println("Format invalide. Entrez exactement " + String(NUM_LEDS * 3) + " valeurs séparées par des espaces.");
    }
  }

  // Transition douce vers la nouvelle couleur
  smoothTransition();
}
