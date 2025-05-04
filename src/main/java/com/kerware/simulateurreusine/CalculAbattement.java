package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;

public class CalculAbattement {

    // Taux d'abattement de 10%
    private static final double TAUX_ABATTEMENT = 0.10;

    // Limites de l'abattement (min et max)
    private static final int ABATTEMENT_MAX = 14171;
    private static final int ABATTEMENT_MIN = 495;

    // Méthode pour calculer l'abattement total
    public double calculer(DonneesFiscales donnees) {

        // Calcul de l'abattement pour le premier revenu
        int abt1 = Math.min( Math.max( (int) Math.round(donnees.revenu1 * TAUX_ABATTEMENT), ABATTEMENT_MIN ), ABATTEMENT_MAX );
        // Initialisation de l'abattement pour le deuxième revenu
        int abt2 = 0;

        // Si situation familiale est mariée ou pacsée, calcul de l'abattement pour le deuxième revenu
        if (donnees.situation == com.kerware.simulateur.SituationFamiliale.MARIE || donnees.situation == com.kerware.simulateur.SituationFamiliale.PACSE) {
            abt2 = Math.min( Math.max((int) Math.round( donnees.revenu2 * TAUX_ABATTEMENT ), ABATTEMENT_MIN ), ABATTEMENT_MAX);
        }

        // Retourner la somme des abattements
        return abt1 + abt2;
    }
}
