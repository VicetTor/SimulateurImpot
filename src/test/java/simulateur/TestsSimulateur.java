package simulateur;

import com.kerware.simulateur.ICalculateurImpot;
import com.kerware.simulateur.SituationFamiliale;
import com.kerware.simulateurreusine.AdaptateurSimulateurReusine;
import org.apache.commons.validator.Arg;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestsSimulateur {

    private static ICalculateurImpot simulateur;

    @BeforeAll
    public static void setUp() {
        simulateur = new AdaptateurSimulateurReusine();
    }

    /// COUVERTURE EXIGENCE : EXG_IMPOT_01 ////////////////////////////////////////////////////////////////////////////
    /**
     * Les montants calculés sont arrondis à l’€ le plus proche
     */

    public static Stream<Arguments> donneesArrondissementEuroProche() {
        return Stream.of(
                Arguments.of(25008, "CELIBATAIRE", 0, 0, false, 918),
                Arguments.of(25009, "CELIBATAIRE", 0, 0, false, 919)
        );
    }

    @DisplayName("Tests de l'arrondissement à l'euro le plus proche")
    @ParameterizedTest
    @MethodSource("donneesArrondissementEuroProche")
    public void testArrondissement( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                    int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals( impotAttendu, simulateur.getImpotSurRevenuNet());
    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_02 ////////////////////////////////////////////////////////////////////////////
    /**
     * Les revenus nets déclarés bénéficient d’un abattement de 10% qui permet d’obtenir le revenu fiscal de référence
     * L’abattement doit être calculé SEPAREMENT pour les 2 déclarants. L’abattement du foyer est calculé en faisant la
     * somme des 2 abattements des 2 déclarants
     * Cet abattement, par déclarant, ne peut pas être supérieur à 14171 €
     * Cet abattement ne peut pas être inférieur à 495 €
     */

    public static Stream<Arguments> donneesAbattementFoyerFiscal() {
        return Stream.of(
                Arguments.of(4900, "CELIBATAIRE", 0, 0, false, 495), // < 495 => 495
                Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 1200), // 10 %
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 14171) // > 14171 => 14171
        );

    }

    @DisplayName("Tests des abattements pour les foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesAbattementFoyerFiscal" )
    public void testAbattement( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                int nbEnfantsSituationHandicap, boolean parentIsole, int abattementAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   abattementAttendu, simulateur.getAbattement());
    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_03 ////////////////////////////////////////////////////////////////////////////
    /**
     * Le nombre de parts fiscales est calculé en fonction du nombre de membres de la famille vivant dans le même foyer
     * 1 part par déclarant
     * 0,5 part pour les 2 premiers enfants
     * 1 part à partir du 3ième enfant
     * 0,5 part supplémentaire par enfant en situation de handicap
     * 0,5 part supplémentaire si vous êtes un parent isolé
     * Si vous êtes veuf ou veuve avec des enfants à charges vous conservez la part de votre conjoint décédé
     */

    public static Stream<Arguments> donneesPartsFoyerFiscal() {
        return Stream.of(
                Arguments.of(24000, "CELIBATAIRE", 0, 0, false, 1),
                Arguments.of(24000, "CELIBATAIRE", 1, 0, false, 1.5),
                Arguments.of(24000, "CELIBATAIRE", 2, 0, false, 2),
                Arguments.of(24000, "CELIBATAIRE", 3, 0, false, 3),
                Arguments.of(24000, "MARIE", 0, 0, false, 2),
                Arguments.of(24000, "PACSE", 0, 0, false, 2),
                Arguments.of(24000, "MARIE", 3, 1, false, 4.5),
                Arguments.of(24000, "DIVORCE", 2, 0, true, 2.5),
                Arguments.of(24000, "VEUF", 3, 0, true, 4.5)
        );
    }

    @DisplayName("Tests du calcul des parts pour différents foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesPartsFoyerFiscal" )
    public void testNombreDeParts( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                   int nbEnfantsSituationHandicap, boolean parentIsole, double nbPartsAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0 );
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals( nbPartsAttendu, simulateur.getNbPartsFoyerFiscal() );

    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_04 ////////////////////////////////////////////////////////////////////////////
    /**
     * Le calcul de l’impôt se fait en divisant le revenu fiscal de référence par le nombre de parts afin d’appliquer
     * le barème du calcul de l’impôt pour les tranches suivantes :
     * • De 0 € à 11294 € pas d’impôt
     * • De 11295 à 28797 11% d’impôt
     * • De 28798 à 82341 30% d’impôt
     * • De 82342 à 177106 41% d’impôt
     * • Plus de 177106 45% d’impôt
     * Ce calcul est progressif, et tout contribuable est imposé sur toutes les tranches le concernant et non pas
     * sur la dernière tranche seule. Cela permet de rendre le montant de l’impôt continu avec le
     * revenu fiscal de référence
     * Si le revenu fiscal de référence pour une part est de 30000 €, le calcul de l’impôt pour une part devient :
     * • (28797 – 11294) * 11% + (30000 – 28797) * 30%
     * Pour obtenir l’impôt final il faut remultiplier l’impôt obtenu pour une part par le nombre de parts du foyer
     *  fiscal.
     * • Impôt net = impôt( revenu fiscal de référence / nb de parts ) * nb de parts
     */

    public static Stream<Arguments> donneesRevenusFoyerFiscal() {
        return Stream.of(
                Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 0), // 0%
                Arguments.of(20000, "CELIBATAIRE", 0, 0, false, 199), // 11%
                Arguments.of(35000, "CELIBATAIRE", 0, 0, false, 2736 ), // 30%
                Arguments.of(95000, "CELIBATAIRE", 0, 0, false, 19284), // 41%
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 60768) // 45%
        );
    }

    @DisplayName("Tests des différents taux marginaux d'imposition")
    @ParameterizedTest
    @MethodSource( "donneesRevenusFoyerFiscal" )
    public void testTrancheImposition( int revenuNet, String situationFamiliale, int nbEnfantsACharge,
                                int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNet );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(impotAttendu, simulateur.getImpotSurRevenuNet());
    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_05 ///////////////////////////////////////////////////////////////////////////*
    /**
     * Le gain d’impôt ( impôt des déclarants seuls - impôts du foyer fiscal complet ) est plafonné.
     * Les parts supplémentaires apportées par les enfants ne peuvent faire décroitre l’impôts que les
     * déclarants auraient dû payer seuls, de plus de 1759 € en 2024 par demi-part supplémentaires
     * • Soit un couple ayant 3 enfants, le couple a 2 parts, les 3 enfants
     *  apportent 0.5 + 0.5 + 1 = 2 parts
     * • Le gain d’impôt apporté par les enfants ne pourra donc excéder 4 * 1759 € (2 parts apportés par
     *  les enfants / 0.5 = 4) = 7036 €
     */

    public static Stream<Arguments> donneesGainImpot() {
        return Stream.of(
                Arguments.of(95000, 0, "MARIE", 3, 7036),
                Arguments.of(91154, 0, "MARIE", 3, 7036),
                Arguments.of(91153, 0, "MARIE", 3, 7035),
                Arguments.of(50000, 0, "MARIE", 3, 2058),
                Arguments.of(95000, 0, "MARIE", 0, 0),
                Arguments.of(50000, 0, "MARIE", 0, 0)
        );
    }

    @DisplayName("Test du plafonnement du gain d'impôt par parts enfants")
    @ParameterizedTest
    @MethodSource("donneesGainImpot")
    public void testPlafondGainImpot(int revenuNetDeclarant1, int revenuNetDeclarant2, String situationFamiliale,
                                     int nbEnfantsACharge, int gainAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
        simulateur.setRevenusNetDeclarant2(revenuNetDeclarant2);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(0);
        simulateur.setNbEnfantsSituationHandicap(0);
        simulateur.setParentIsole(false);

        simulateur.calculImpotSurRevenuNet();
        int impotSansPartsEnfants = simulateur.getImpotSurRevenuNet();

        // Act
        simulateur.setNbEnfantsACharge(nbEnfantsACharge);
        simulateur.calculImpotSurRevenuNet();
        int impotAvecEnfants = simulateur.getImpotSurRevenuNet();

        int gainReel = impotSansPartsEnfants - impotAvecEnfants;

        // Assert
        assertEquals(gainAttendu, gainReel);
    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_06 //////////////////////////////////////////////////////////////////////Z/////*
    /**
     * L’administration fiscale prévoit un mécanisme de décote (abattement supplémentaire) de l’impôt pour les
     * situations modestes.
     * Pour un couple, si le montant de l’impôt est inférieur à 3191 € la décote s’applique
     * Pour une personne seule, si le montant de l’impôt est inférieur à 1929 € la décote s’applique
     * La décote se calcule en 2024 de la manière suivante :
     * • Pour un couple : 1444 € – 45,25% * Montant impôt
     * • Pour une personne seule : 873 € – 45,25% * Montant impôt
     * On doit donc retrancher la décote du montant de l’impôt pour obtenir l’impôt final.
     */

    public static Stream<Arguments> donneesDecote() {
        return Stream.of(
                
                Arguments.of(40000, "CELIBATAIRE", 0, 0, false, 4086, 4086),
                Arguments.of(32010, "CELIBATAIRE", 0, 0, false, 1929, 1929),

                Arguments.of(57880, "MARIE", 0, 0, false, 3191, 3191),
                Arguments.of(57870, "MARIE", 0, 0, false, 3190, 3189)
        );
    }

    @DisplayName("Tests de la décote pour faibles impositions")
    @ParameterizedTest
    @MethodSource("donneesDecote")
    public void testDecote(int revenuNetDeclarant1, String situationFamiliale,
                           int nbEnfantsACharge, int nbEnfantsSituationHandicap, boolean parentIsole,
                           double impotBrutAttendu, double impotNetAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
        simulateur.setRevenusNetDeclarant2(0);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(nbEnfantsACharge);
        simulateur.setNbEnfantsSituationHandicap(nbEnfantsSituationHandicap);
        simulateur.setParentIsole(parentIsole);

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals((int) impotBrutAttendu, simulateur.getImpotAvantDecote(), "Impôt brut incorrect");
        assertEquals((int) Math.round(impotNetAttendu), simulateur.getImpotSurRevenuNet(), "Impôt net incorrect après décote");
    }


    /// COUVERTURE EXIGENCE : EXG_IMPOT_07 ////////////////////////////////////////////////////////////////////////
    /**
     * En 2024 il y a une contribution exceptionnelle pour les hauts revenus qui
     * est calculée par tranche comme pour l’impôt principal :
     * Pour un célibataire disposant d'un revenu fiscal de référence de 550 000 €, la
     * contribution exceptionnelle est de : [(500 000 € - 250 000 €) x 3 %] + [(550 000 € -
     * 500 000 €) x 4 %] = 9 500 €.
     */
    public static Stream<Arguments> donneesContributionExceptionnelle(){
        return Stream.of(
                // Cas droits
                Arguments.of(240000, 0, "CELIBATAIRE", 0, 0, false, 0),
                Arguments.of(240000, 10000, "MARIE", 0, 0, false, 0),
                Arguments.of(550000, 0, "CELIBATAIRE", 0, 0, false, 8933),
                Arguments.of(1100000, 0, "CELIBATAIRE", 0, 0, false, 30933),

                //Cas aux limites

                Arguments.of(250000, 250000, "MARIE", 0, 0, false, 0),
                Arguments.of(250000, 0, "CELIBATAIRE", 0, 0, false, 0),

                Arguments.of(250001, 0, "CELIBATAIRE", 0, 0, false, 0),


                Arguments.of(500000, 0, "CELIBATAIRE", 0, 0, false, 7075),
                Arguments.of(500001, 0, "CELIBATAIRE", 0, 0, false, 7075),


                Arguments.of(1000000, 0, "CELIBATAIRE", 0, 0, false, 26933),
                Arguments.of(1000001, 0, "CELIBATAIRE", 0, 0, false, 26933),

                Arguments.of(500000, 500001, "MARIE", 0, 0, false, 14150)


        );
    }


    @DisplayName("EXG_IMPOT_07 - Contribution exceptionnelle sur les hauts revenus")
    @ParameterizedTest
    @MethodSource("donneesContributionExceptionnelle")
    void testContributionExceptionnelle(int revenu1, int revenu2, String situation, int enfants, int enfantsHandi, boolean parentIsole, int cehrAttendue) {
        //Arrange
        simulateur.setRevenusNetDeclarant1(revenu1);
        simulateur.setRevenusNetDeclarant2(revenu2);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situation));
        simulateur.setNbEnfantsACharge(enfants);
        simulateur.setNbEnfantsSituationHandicap(enfantsHandi);
        simulateur.setParentIsole(parentIsole);

        //Act
        simulateur.calculImpotSurRevenuNet();

        //Assert
        assertEquals(cehrAttendue, simulateur.getContribExceptionnelle(), "Erreur sur le calcul de la CEHR");
    }


    /// COUVERTURE EXIGENCE : ROBUSTESSE ////////////////////////////////////////////////////////////////////////////

    public static Stream<Arguments> donneesRobustesse() {
        return Stream.of(
                Arguments.of(10000, -1, "MARIE", 0, 0, false), // 0%
                Arguments.of(-1, 0,"CELIBATAIRE", 0, 0, false),
                Arguments.of(20000,0, null , 0, 0, false), // 11%
                Arguments.of(35000,0, "CELIBATAIRE", -1, 0, false ), // 30%
                Arguments.of(95000,0, "CELIBATAIRE", 0, -1, false), // 41%
                Arguments.of(200000,0, "CELIBATAIRE", 3, 4, false, 60768),
                Arguments.of(200000,0, "MARIE", 3, 2, true),
                Arguments.of(200000,0, "PACSE", 3, 2, true),
                Arguments.of(200000,0, "MARIE", 8, 0, false),
                Arguments.of(200000,10000, "CELIBATAIRE", 8, 0, false),
                Arguments.of(200000,10000, "VEUF", 8, 0, false),
                Arguments.of(200000,10000, "DIVORCE", 8, 0, false)
        );
    }

    @DisplayName("Tests de robustesse avec des valeurs interdites")
    @ParameterizedTest( name ="Test avec revenuNetDeclarant1={0}, revenuDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5}")
    @MethodSource( "donneesRobustesse" )
    public void testRobustesse( int revenuNetDeclarant1, int revenuNetDeclarant2 , String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        if ( situationFamiliale == null )
                simulateur.setSituationFamiliale( null  );
        else
                simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale ));
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act & Assert
        assertThrows( IllegalArgumentException.class,  () -> { simulateur.calculImpotSurRevenuNet();} );


    }

    // AVEC D'AUTRES IDEES DE TESTS
    // AVEC @ParameterizedTest et @CsvFileSource
    @DisplayName("Tests supplémentaires de cas variés de foyers fiscaux - ")
    @ParameterizedTest( name = " avec revenuNetDeclarant1={0}, revenuNetDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5} - IMPOT NET ATTENDU = {6}")
    @CsvFileSource( resources={"/datasImposition.csv"} , numLinesToSkip = 1 )
    public void testCasImposition( int revenuNetDeclarant1, int revenuNetDeclarant2,  String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals( Integer.valueOf(impotAttendu), simulateur.getImpotSurRevenuNet() );
    }

}
