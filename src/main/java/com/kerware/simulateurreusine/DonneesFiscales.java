package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

public final class DonneesFiscales {
    // Données fiscales : revenus, situation familiale et informations sur les enfants
    private final int revenu1;
    private final int revenu2;
    private final SituationFamiliale situation;
    private final int nbEnfants;
    private final int nbEnfantsHandicap;
    private final boolean parentIsole;

    public int getRevenu1(){
        return revenu1;
    }

    public int getRevenu2(){
        return revenu2;
    }

    public int getNbEnfants() {
        return nbEnfants;
    }

    public int getNbEnfantsHandicap() {
        return nbEnfantsHandicap;
    }

    public boolean isParentIsole() {
        return parentIsole;
    }

    public SituationFamiliale getSituation() {
        return situation;
    }

    // Constructeur pour initialiser les données fiscales
    public DonneesFiscales(int r1, int r2, SituationFamiliale sit
            , int enfants, int enfantsHandi, boolean isol) {
        this.revenu1 = r1;
        this.revenu2 = r2;
        this.situation = sit;
        this.nbEnfants = enfants;
        this.nbEnfantsHandicap = enfantsHandi;
        this.parentIsole = isol;
    }

    // Calcul et retour du revenu total du foyer
    public double getRevenuTotal() {
        return revenu1 + revenu2;
    }

    // Retour du nombre de déclarants : 2 si marié/pacsé, 1 sinon
    public int getNbPartsDecl() {
        return (situation == SituationFamiliale.MARIE ||
                situation == SituationFamiliale.PACSE) ? 2 : 1;
    }
}