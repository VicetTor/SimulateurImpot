package com.kerware.simulateurreusine;

public final class CalculCEHR {
    // Définition des tranches de revenus pour le calcul de la CEHR
    private static final int[] TRANCHES = {0, 250000, 500000, 1000000, Integer.MAX_VALUE};

    // Taux appliqués pour une personne seule
    private static final double[] TAUX_CELIB = {0.0, 0.03, 0.04, 0.04};

    // Taux appliqués pour un couple
    private static final double[] TAUX_COUPLE = {0.0, 0.0, 0.03, 0.04};

    // Méthode pour calculer la CEHR (Contribution Exceptionnelle sur les Hauts Revenus)
    public double calculer(double rfr, double nbPartsDecl) {
        // Choix du barème selon le nombre de parts fiscales
        double[] taux = (nbPartsDecl == 1) ? TAUX_CELIB : TAUX_COUPLE;
        double montant = 0;

        // Parcours des tranches et calcul du montant à chaque palier
        for (int i = 0; i < taux.length; i++) {
            if (rfr > TRANCHES[i]) {
                double base = Math.min(rfr, TRANCHES[i + 1]) - TRANCHES[i];
                montant += base * taux[i];
            }
        }

        // Retour du montant arrondi
        return Math.round(montant);
    }
}
