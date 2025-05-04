package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;

public class CalculAbattement {
    private static final double TAUX_ABATTEMENT = 0.10;
    private static final int ABATTEMENT_MAX = 14171;
    private static final int ABATTEMENT_MIN = 495;

    public double calculer(DonneesFiscales donnees) {
        int abt1 = Math.min(Math.max((int) Math.round(donnees.revenu1 * TAUX_ABATTEMENT), ABATTEMENT_MIN), ABATTEMENT_MAX);
        int abt2 = 0;
        if (donnees.situation == com.kerware.simulateur.SituationFamiliale.MARIE || donnees.situation == com.kerware.simulateur.SituationFamiliale.PACSE) {
            abt2 = Math.min(Math.max((int) Math.round(donnees.revenu2 * TAUX_ABATTEMENT), ABATTEMENT_MIN), ABATTEMENT_MAX);
        }
        return abt1 + abt2;
    }
}