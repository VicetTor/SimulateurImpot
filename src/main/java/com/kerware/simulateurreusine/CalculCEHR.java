package com.kerware.simulateurreusine;

public class CalculCEHR {
    private static final int[] TRANCHES = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};
    private static final double[] TAUX_CELIB = {0.0, 0.03, 0.04, 0.04};
    private static final double[] TAUX_COUPLE = {0.0, 0.0, 0.03, 0.04};

    public double calculer(double rfr, double nbPartsDecl) {
        double[] taux = (nbPartsDecl == 1) ? TAUX_CELIB : TAUX_COUPLE;
        double montant = 0;
        for (int i = 0; i < taux.length; i++) {
            if (rfr > TRANCHES[i]) {
                double base = Math.min(rfr, TRANCHES[i + 1]) - TRANCHES[i];
                montant += base * taux[i];
            }
        }
        return Math.round(montant);
    }
}
