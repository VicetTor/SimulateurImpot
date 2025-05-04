package com.kerware.simulateurreusine;

import com.kerware.simulateurreusine.DonneesFiscales;
import com.kerware.simulateur.SituationFamiliale;

public final class CalculPartFiscale {

    // Méthode pour calculer le nombre de parts fiscales en fonction de la situation et des
    // enfants (EXG_IMPOT_03)
    public double calculer(DonneesFiscales d) {
        // Base : 2 parts si marié/pacsé, sinon 1
        double parts = (d.getSituation() == SituationFamiliale.MARIE ||
                d.getSituation() == SituationFamiliale.PACSE) ? 2 : 1;

        // 0,5 part pour les deux premiers enfants
        final double demiPart = 0.5;
        parts += Math.min(2, d.getNbEnfants()) * demiPart;

        // 1 part par enfant à partir du troisième
        if(d.getNbEnfants() > 2){
            parts += (d.getNbEnfants() - 2);
        }

        // 0,5 part supplémentaire pour parent isolé avec enfant
        if(d.isParentIsole() && d.getNbEnfants() > 0){
            parts += demiPart;
        }

        // 1 part supplémentaire pour veuf avec enfant
        if(d.getSituation() == SituationFamiliale.VEUF && d.getNbEnfants() > 0){
            parts += 1;
        }

        // 0,5 part par enfant handicapé
        parts += d.getNbEnfantsHandicap() * demiPart;

        // Retour du total de parts fiscales
        return parts;
    }
}
