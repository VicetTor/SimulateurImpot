package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;
import com.kerware.simulateur.SituationFamiliale;

public class CalculPartFiscale {
    public double calculer(DonneesFiscales d) {
        double parts = (d.situation == SituationFamiliale.MARIE || d.situation == SituationFamiliale.PACSE) ? 2 : 1;
        parts += Math.min(2, d.nbEnfants) * 0.5;
        if (d.nbEnfants > 2) parts += (d.nbEnfants - 2);
        if (d.parentIsole && d.nbEnfants > 0) parts += 0.5;
        if (d.situation == SituationFamiliale.VEUF && d.nbEnfants > 0) parts += 1;
        parts += d.nbEnfantsHandicap * 0.5;
        return parts;
    }
}