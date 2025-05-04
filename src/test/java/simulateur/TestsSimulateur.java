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

    /// COUVERTURE EXIGENCE : EXG_IMPOT_05 ////////////////////////////////////////////////////////////////////////////*

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

    /// COUVERTURE EXIGENCE : EXG_IMPOT_06 ////////////////////////////////////////////////////////////////////////////*

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
