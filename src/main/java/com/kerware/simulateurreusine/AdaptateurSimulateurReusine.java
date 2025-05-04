package com.kerware.simulateurreusine;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
public final class AdaptateurSimulateurReusine implements ICalculateurImpot {

    private int revenu1;
    private int revenu2;
    private SituationFamiliale situation;
    private int nbEnfants;
    private int nbEnfantsHandicap;
    private boolean parentIsole;

    private SimulateurReusine simulateur = new SimulateurReusine();
    private DonneesFiscales donnees;
    private int resultat;

    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenu1 = rn;
    }

    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenu2 = rn;
    }

    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situation = sf;
    }

    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfants = nbe;
    }

    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsHandicap = nbesh;
    }

    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    @Override
    public void calculImpotSurRevenuNet() {
        this.donnees = new DonneesFiscales(revenu1, revenu2, situation, nbEnfants, nbEnfantsHandicap, parentIsole);
        this.resultat = simulateur.calculerImpot(donnees);
    }

    @Override
    public int getRevenuNetDeclatant1() {
        return revenu1;
    }

    @Override
    public int getRevenuNetDeclatant2() {
        return revenu2;
    }

    @Override
    public double getContribExceptionnelle() {
        return new CalculCEHR().calculer(donnees.getRevenuTotal() - new CalculAbattement().calculer(donnees), donnees.getNbPartsDecl());
    }

    @Override
    public int getRevenuFiscalReference() {
        return (int) (donnees.getRevenuTotal() - new CalculAbattement().calculer(donnees));
    }

    @Override
    public int getAbattement() {
        return (int) new CalculAbattement().calculer(donnees);
    }

    @Override
    public double getNbPartsFoyerFiscal() {
        return new CalculPartFiscale().calculer(donnees);
    }

    @Override
    public int getImpotAvantDecote() {
        return simulateur.getImpotAvantDecote();
    }

    @Override
    public int getDecote() {
        return (int) new CalculDecote().calculer(resultat, donnees.getNbPartsDecl());
    }

    @Override
    public int getImpotSurRevenuNet() {
        return resultat;
    }
}
