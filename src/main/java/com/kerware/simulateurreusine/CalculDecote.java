package com.kerware.simulateurreusine;

public class CalculDecote {
    private static final double SEUIL_SEUL = 1929;
    private static final double SEUIL_COUPLE = 3191;
    private static final double MAX_SEUL = 873;
    private static final double MAX_COUPLE = 1444;
    private static final double TAUX = 0.4525;

    public double calculer(double impotBrut, double nbPartsDecl) {
        if (nbPartsDecl == 1 && impotBrut < SEUIL_SEUL) {
            return Math.min(impotBrut, Math.round(MAX_SEUL - impotBrut * TAUX));
        }
        if (nbPartsDecl == 2 && impotBrut < SEUIL_COUPLE) {
            return Math.min(impotBrut, Math.round(MAX_COUPLE - impotBrut * TAUX));
        }
        return 0;
    }
}