package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AcceuilPage;
import pages.DemandeRemiseDordre;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertTrue;

public class DemandeRemiseSteps{
    //à réaliser plus tard


    private AcceuilPage acceuilPage;
    private DemandeRemiseDordre demandeRemiseDordre;



    @Given("l'utilisateur est connecté et sur la page de demande de remise d'ordre")
    public void utilisateur_connecte_et_sur_la_page_demande_remise() {
        acceuilPage = loginPage.clickDemo();
        demandeRemiseDordre = acceuilPage.clickDemandeRemiseDordre();
    }

    @When("le compte {string} est sélectionné")
    public void compte_est_selectionne(String compte) {
        demandeRemiseDordre.selectFromCompteDebiter(compte);
        assertTrue("Aucun compte sélectionné",
                demandeRemiseDordre.getSelectedOptionsCompteDebiter().contains(compte));
    }

    @When("la nature de remise {string} est sélectionnée")
    public void nature_remise_selectionnee(String natureRemise) {
        demandeRemiseDordre.selectFromNatureRemise(natureRemise);
        assertTrue("Aucune nature sélectionnée",
                demandeRemiseDordre.getSelectedOptionsNatureRemise().contains(natureRemise));
    }

    @When("le type de remise {string} est sélectionné")
    public void type_remise_selectionne(String typeRemise) {
        demandeRemiseDordre.selectFromTypeRemise(typeRemise);
        assertTrue("Aucun type sélectionné",
                demandeRemiseDordre.getSelectedOptionsTypeRemise().contains(typeRemise));
    }

    @When("les informations de la remise sont saisies")
    public void informations_remise_saisies() {
        demandeRemiseDordre.setReference("MM1425omdo");
        demandeRemiseDordre.setMotif("xhbvhjdb");
        demandeRemiseDordre.setMontantGlobal("524522");
        demandeRemiseDordre.setNombreVirements("2");
        demandeRemiseDordre.setDateExecution();
    }

    @When("le fichier est importé")
    public void fichier_est_importe() {
        demandeRemiseDordre.uploadTheFile("C:\\workspace\\testautomation\\webDriver_java\\UibTestProject\\resources\\Untitled.xyz.txt");
    }

    @When("le bouton de validation est cliqué")
    public void bouton_validation_clique() {
        demandeRemiseDordre.clickValidBTN();
    }

    @Then("la demande est soumise avec succès")
    public void demande_soumise_succes() {

        assertTrue(demandeRemiseDordre.clickValidBTN());
    }




}
