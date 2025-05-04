package com.kerware.simulateurreusine;

public class CalculDecote {
    // Seuils d'impôt brut en dessous desquels la décote s'applique
    private static final double SEUIL_SEUL = 1929;
    private static final double SEUIL_COUPLE = 3191;

    // Montants maximums de décote
    private static final double MAX_SEUL = 873;
    private static final double MAX_COUPLE = 1444;

    // Taux de réduction appliqué sur l'impôt brut
    private static final double TAUX = 0.4525;

    // Méthode pour calculer la décote selon l'impôt brut et le nombre de parts fiscales
    public double calculer(double impotBrut, double nbPartsDecl) {
        double decote = 0;

        // Application des règles selon situation : seul ou couple
        if (nbPartsDecl == 1 && impotBrut < SEUIL_SEUL) {
            decote = MAX_SEUL - (impotBrut * TAUX);
        } else if (nbPartsDecl == 2 && impotBrut < SEUIL_COUPLE) {
            decote = MAX_COUPLE - (impotBrut * TAUX);
        }

        // La décote ne peut pas dépasser l'impôt brut
        decote = Math.min(decote, impotBrut);

        // Retour du montant de décote arrondi
        return Math.round(decote);
    }
}
