package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;
import com.kerware.simulateur.SituationFamiliale;

public class CalculPartFiscale {
    // Méthode pour calculer le nombre de parts fiscales en fonction de la situation et des enfants
    public double calculer(DonneesFiscales d) {
        // Base : 2 parts si marié/pacsé, sinon 1
        double parts = (d.situation == SituationFamiliale.MARIE || d.situation == SituationFamiliale.PACSE) ? 2 : 1;

        // 0,5 part pour les deux premiers enfants
        parts += Math.min(2, d.nbEnfants) * 0.5;

        // 1 part par enfant à partir du troisième
        if (d.nbEnfants > 2) parts += (d.nbEnfants - 2);

        // 0,5 part supplémentaire pour parent isolé avec enfant
        if (d.parentIsole && d.nbEnfants > 0) parts += 0.5;

        // 1 part supplémentaire pour veuf avec enfant
        if (d.situation == SituationFamiliale.VEUF && d.nbEnfants > 0) parts += 1;

        // 0,5 part par enfant handicapé
        parts += d.nbEnfantsHandicap * 0.5;

        // Retour du total de parts fiscales
        return parts;
    }
}
