package com.kerware.simulateurreusine;

import com.kerware.simulateur.SituationFamiliale;

public class DonneesFiscales {
    public final int revenu1;
    public final int revenu2;
    public final SituationFamiliale situation;
    public final int nbEnfants;
    public final int nbEnfantsHandicap;
    public final boolean parentIsole;

    public DonneesFiscales(int r1, int r2, SituationFamiliale sit, int enfants, int enfantsHandi, boolean isol) {
        this.revenu1 = r1;
        this.revenu2 = r2;
        this.situation = sit;
        this.nbEnfants = enfants;
        this.nbEnfantsHandicap = enfantsHandi;
        this.parentIsole = isol;
    }

    public double getRevenuTotal() {
        return revenu1 + revenu2;
    }

    public int getNbPartsDecl() {
        return (situation == SituationFamiliale.MARIE || situation == SituationFamiliale.PACSE) ? 2 : 1;
    }
}
