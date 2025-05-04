package com.kerware.simulateurreusine;

public final class CalculerImpotBrut {
    // Tranches de revenus pour le barème de l'impôt
    private static final int[] TRANCHES = {0, 11294, 28797, 82341, 177106, Integer.MAX_VALUE};

    // Taux d'imposition associés à chaque tranche
    private static final double[] TAUX = {0.0, 0.11, 0.30, 0.41, 0.45};

    // Méthode pour calculer l'impôt brut sur le revenu par part
    public double calculer(double revenuParPart) {
        double impots = 0;

        // Parcours des tranches et calcul de l'impôt tranche par tranche
        for (int i = 0; i < TAUX.length; i++) {
            if (revenuParPart > TRANCHES[i]) {
                double base = Math.min(revenuParPart, TRANCHES[i + 1]) - TRANCHES[i];
                impots += base * TAUX[i];
            }
        }

        // Retour du montant total d'impôt brut
        return impots;
    }
}
