package com.kerware.simulateurreusine;

public class PlafondQuotientFamilial {
    private static final double PLAFOND_DEMI_PART = 1759.0;

    public double appliquer(double impotsBrut, double impotsDecl, double nbParts, double nbPartsDecl) {
        double gain = impotsDecl - impotsBrut;
        double maxGain = ((nbParts - nbPartsDecl) / 0.5) * PLAFOND_DEMI_PART;
        if (gain > maxGain) {
            return impotsDecl - maxGain;
        }
        return Math.round(impotsBrut);
    }
}