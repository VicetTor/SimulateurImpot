package com.kerware.simulateurreusine;

public class CalculerImpotBrut {
    private static final int[] TRANCHES = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};
    private static final double[] TAUX = {0.0, 0.11, 0.30, 0.41, 0.45};

    public double calculer(double revenuParPart) {
        double impots = 0;
        for (int i = 0; i < TAUX.length; i++) {
            if (revenuParPart > TRANCHES[i]) {
                double base = Math.min(revenuParPart, TRANCHES[i + 1]) - TRANCHES[i];
                impots += base * TAUX[i];
            }
        }
        return impots;
    }
}