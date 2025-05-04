package com.kerware.simulateurreusine;

public final class PlafondQuotientFamilial {
    // Plafond de réduction d'impôt pour une demi-part fiscale
    private static final double PLAFOND_DEMI_PART = 1759.0;

    // Méthode pour appliquer le plafonnement du quotient familial
    public double appliquer(double impotsBrut, double impotsDecl
            , double nbParts, double nbPartsDecl) {
        // Calcul du gain obtenu grâce aux parts supplémentaires
        double gain = impotsDecl - impotsBrut;

        // Calcul du gain maximal autorisé en fonction du nombre de demi-parts supplémentaires
        final double maxGain = ((nbParts - nbPartsDecl) / 0.5) * PLAFOND_DEMI_PART;

        // Si le gain dépasse le plafond, on limite l'avantage fiscal
        if (gain > maxGain) {
            return impotsDecl - maxGain;
        }

        // Sinon, on retourne l'impôt brut arrondi
        return Math.round(impotsBrut);
    }
}
