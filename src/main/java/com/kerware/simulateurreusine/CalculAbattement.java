package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;

public final class CalculAbattement {

    // Taux d'abattement de 10%
    private static final double TAUX_ABATTEMENT = 0.10;

    // Limites de l'abattement (min et max)
    private static final int ABATTEMENT_MAX = 14171;
    private static final int ABATTEMENT_MIN = 495;

    // Méthode pour calculer l'abattement total (EXG_IMPOT_02)
    public double calculer(DonneesFiscales donnees) {

        // Calcul de l'abattement pour le premier revenu (EXG_IMPOT_01)
        int abt1 = Math.min( Math.max( (int) Math.round(donnees.getRevenu1()
                * TAUX_ABATTEMENT), ABATTEMENT_MIN ), ABATTEMENT_MAX );
        // Initialisation de l'abattement pour le deuxième revenu
        int abt2 = 0;

        // Si situation familiale est mariée ou pacsée,
        // calcul de l'abattement pour le deuxième revenu (EXG_IMPOT_01)
        if (donnees.getSituation() == com.kerware.simulateur.SituationFamiliale.MARIE
                || donnees.getSituation() == com.kerware.simulateur.SituationFamiliale.PACSE) {
            abt2 = Math.min( Math.max((int) Math.round( donnees.getRevenu2()
                    * TAUX_ABATTEMENT ), ABATTEMENT_MIN ), ABATTEMENT_MAX);
        }

        // Retourner la somme des abattements
        return abt1 + abt2;
    }
}
