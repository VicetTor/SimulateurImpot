package com.kerware.simulateurreusine;

import com.kerware.simulateur.Simulateur;
import com.kerware.simulateur.SituationFamiliale;

public class SimulateurLauncher {

    public static void main(String[] args) {

        DonneesFiscales donnees = new DonneesFiscales(
                220000,
                200000,
                SituationFamiliale.MARIE,
                1,
                1,
                false
        );

        SimulateurReusine simulateur = new SimulateurReusine();
        int impot = simulateur.calculerImpot(donnees);
        System.out.println("\n>>> Impôt sur le revenu net final : " + impot + " €");
    }
}
