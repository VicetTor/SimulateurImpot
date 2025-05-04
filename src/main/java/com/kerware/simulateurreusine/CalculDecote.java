package com.kerware.simulateurreusine;

public class CalculDecote {
    private static final double SEUIL_SEUL = 1929;
    private static final double SEUIL_COUPLE = 3191;
    private static final double MAX_SEUL = 873;
    private static final double MAX_COUPLE = 1444;
    private static final double TAUX = 0.4525;

    public double calculer(double impotBrut, double nbPartsDecl) {
        double decote = 0;
        if (nbPartsDecl == 1 && impotBrut < SEUIL_SEUL) {
            decote = MAX_SEUL - (impotBrut * TAUX);
        } else if (nbPartsDecl == 2 && impotBrut < SEUIL_COUPLE) {
            decote = MAX_COUPLE - (impotBrut * TAUX);
        }

        decote = Math.min(decote, impotBrut);
        return Math.round(decote);
    }

}