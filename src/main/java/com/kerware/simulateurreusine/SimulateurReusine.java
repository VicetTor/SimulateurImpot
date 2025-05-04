package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

public final class SimulateurReusine {

    // Stocke le montant de l'impôt avant application de la décote
    private double impotAvantDecote = 0;

    private void verifierNonNegatif(String champ, int valeur) {
        if (valeur < 0) {
            throw new IllegalArgumentException(champ + " ne peut pas être négatif");
        }
    }

    // Méthode de vérification des données fiscales avant simulation
    public void verificationsExceptions(DonneesFiscales donnees){
        if (donnees == null) {
            throw new IllegalArgumentException("Les données fiscales ne peuvent pas être null");
        }

        if (donnees.getSituation() == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }

        verifierNonNegatif("Le revenu 1", donnees.getRevenu1());
        verifierNonNegatif("Le revenu 2", donnees.getRevenu2());
        verifierNonNegatif("Le nombre d'enfants", donnees.getNbEnfants());
        verifierNonNegatif("Le nombre d'enfants handicapés", donnees.getNbEnfantsHandicap());

        if (donnees.getNbEnfantsHandicap() > donnees.getNbEnfants()) {
            throw new IllegalArgumentException("Le " +
                    "nombre d'enfants handicapés ne peut pas dépasser le nombre d'enfants");
        }

        final int maxEnfants = 7;
        if (donnees.getNbEnfants() > maxEnfants) {
            throw new IllegalArgumentException("Le" +
                    " nombre d'enfants ne peut pas dépasser 7");
        }

        // Vérifie qu'il n'y a pas de revenu2 si le contribuable est seul
        boolean seul = donnees.getSituation() == SituationFamiliale.CELIBATAIRE
                || donnees.getSituation() == SituationFamiliale.DIVORCE
                || donnees.getSituation() == SituationFamiliale.VEUF;

        if (seul && donnees.getRevenu2() > 0) {
            throw new IllegalArgumentException("Le " +
                    " déclarant 2 ne peut pas avoir de revenu si le contribuable est seul");
        }

        // Vérifie qu'un parent isolé n'est pas marié/pacsé
        if (donnees.isParentIsole() && (donnees.getSituation() ==
                SituationFamiliale.MARIE ||
                donnees.getSituation() == SituationFamiliale.PACSE)) {
            throw new IllegalArgumentException("" +
                    "Un parent isolé ne peut pas être marié ou pacsé");
        }
    }

    // Méthode principale de calcul de l'impôt sur le revenu
    public int calculerImpot(DonneesFiscales donnees) {

        // Vérification des données avant traitement
        verificationsExceptions(donnees);

        // Calcul de l’abattement sur revenus
        double abattement = new CalculAbattement().calculer(donnees);

        // Revenu fiscal de référence après abattement
        double rfr = donnees.getRevenuTotal() - abattement;

        // Calcul du nombre de parts fiscales
        double nbParts = new CalculPartFiscale().calculer(donnees);

        // Calcul de l'impôt brut sur le revenu par part et pour l'ensemble des parts
        double revenuParPart  = rfr / nbParts;
        double impotBrut = new CalculerImpotBrut().calculer(revenuParPart) * nbParts;

        // Calcul pour le nombre de parts déclarantes (avant majoration pour enfants, etc.)
        double nbPartsDecl = donnees.getNbPartsDecl();
        double revenuParPartDecl = rfr / nbPartsDecl;
        double impotDecl = new CalculerImpotBrut().calculer(revenuParPartDecl) * nbPartsDecl;

        // Application du plafonnement du quotient familial si nécessaire
        if (nbParts > nbPartsDecl) {
            impotBrut = new PlafondQuotientFamilial().appliquer(impotBrut
                    , impotDecl, nbParts, nbPartsDecl);
        }

        // Stockage de l'impôt avant décote pour consultation éventuelle
        this.impotAvantDecote = impotBrut;

        // Calcul de la décote éventuelle
        double decote = new CalculDecote().calculer(impotBrut, nbPartsDecl);
        if (decote > impotBrut) {
            decote = impotBrut;
        }

        // Calcul de la Contribution Exceptionnelle sur les Hauts Revenus (CEHR)
        double cehr = new CalculCEHR().calculer(rfr, nbPartsDecl);

        // Calcul de l'impôt net : impôt brut - décote + CEHR
        double impotNet = impotBrut - decote + cehr;

        // Retour de l'impôt net arrondi à l'entier
        return (int) Math.round(impotNet);
    }

    // Getter pour récupérer l'impôt avant décote
    public int getImpotAvantDecote() {
        return (int) Math.round(impotAvantDecote);
    }
}
