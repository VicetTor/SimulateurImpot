package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

public class SimulateurReusine {

    public int calculerImpot(DonneesFiscales donnees) {

        if (donnees == null) {
            throw new IllegalArgumentException("Les données fiscales ne peuvent pas être null");
        }
        if (donnees.situation == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }
        if (donnees.revenu1 < 0 || donnees.revenu2 < 0) {
            throw new IllegalArgumentException("Le revenu net ne peut pas être négatif");
        }
        if (donnees.nbEnfants < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }
        if (donnees.nbEnfantsHandicap < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas être négatif");
        }
        if (donnees.nbEnfantsHandicap > donnees.nbEnfants) {
            throw new IllegalArgumentException("Le nombre d'enfants handicapés ne peut pas dépasser le nombre d'enfants");
        }
        if (donnees.nbEnfants > 7) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas dépasser 7");
        }
        boolean seul = donnees.situation == SituationFamiliale.CELIBATAIRE
                || donnees.situation == SituationFamiliale.DIVORCE
                || donnees.situation == SituationFamiliale.VEUF;
        if (seul && donnees.revenu2 > 0) {
            throw new IllegalArgumentException("Le déclarant 2 ne peut pas avoir de revenu si le contribuable est seul");
        }
        if (donnees.parentIsole && (donnees.situation == SituationFamiliale.MARIE || donnees.situation == SituationFamiliale.PACSE)) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé");
        }

        double abattement = new CalculAbattement().calculer(donnees);
        double rfr = donnees.getRevenuTotal() - abattement;
        double nbParts = new CalculPartFiscale().calculer(donnees);

        double revenuParPart  = rfr / nbParts;
        double impotBrut = new CalculerImpotBrut().calculer(revenuParPart) * nbParts;

        double nbPartsDecl = donnees.getNbPartsDecl();
        double revenuParPartDecl = rfr / nbPartsDecl;
        double impotDecl = new CalculerImpotBrut().calculer(revenuParPartDecl) * nbPartsDecl;

        if (nbParts > nbPartsDecl) {
            impotBrut = new PlafondQuotientFamilial().appliquer(impotBrut, impotDecl, nbParts, nbPartsDecl);
        }

        double decote = new CalculDecote().calculer(impotBrut, nbPartsDecl);

        if (decote > impotBrut) {
            decote = impotBrut;
        }

        double cehr = new CalculCEHR().calculer(rfr, nbPartsDecl);

        return (int) Math.round((impotBrut - decote) + cehr);
    }
}
