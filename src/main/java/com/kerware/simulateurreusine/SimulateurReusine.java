package com.kerware.simulateurreusine;

public class SimulateurReusine {

    public int calculerImpot(DonneesFiscales donnees) {
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
