package steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import pages.AcceuilPage;
import pages.DemandeChequierPage;

import java.util.List;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class chequeRequestSteps   {

    private AcceuilPage acceuilPage;
    private DemandeChequierPage demandeChequierPage ;
    List<WebElement> errorMessages ;





    @Given("utilisateur est connecté et se aller vers page d'accueil")
    public void utilisateur_est_connecté_et_se_aller_vers_page_d_accueil() {
        acceuilPage = loginPage.clickDemo();
    }
    @When("utilisateur accède à la page de demande de chéquier")
    public void naviguerVersPageDemandeChequier() {
         demandeChequierPage = acceuilPage.clickDemandeChequierPage();
    }

    @When("utilisateur sélectionne l'option pour une nouvelle demande de chéquier")
    public void cliquerSurBoutonNouvelleDemande() {
        demandeChequierPage.clickNouvelleDemande();
    }

    @When("utilisateur choisit un compte")
    public void selectionnerCompte() {
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        demandeChequierPage.selectFromAccounts(account);
        assertTrue("Aucun compte sélectionné", demandeChequierPage.getSelectedOptionsAccounts().contains(account));
    }

    @When("utilisateur saisit un nom du bénéficiaire")
    public void saisirNomBeneficiaire() {
        demandeChequierPage.setBenificiareName("Omar");
    }

    @When("utilisateur entre un agence")
    public void saisirAgence() {
        demandeChequierPage.setAgence("UIB");
    }

    @When("utilisateur indique un montant")
    public void saisirMontant() {
        demandeChequierPage.setAmount("500");
    }

    @When("utilisateur renseigne un motif")
    public void saisirMotif() {
        demandeChequierPage.setReason("Pauvrité");
    }

    @When("utilisateur soumet la demande en cliquant sur le bouton de validation")
    public void cliquerSurBoutonValidation() {
        demandeChequierPage.clickValidBTN();
    }

    @Then("un message d'erreur, \"Une erreur est survenue!\", doit être affiché")
    public void verifierMessageErreur() {
        String toastMessage = "Une erreur est survenue!";
        assertTrue("L'alerte n'a pas été affichée.", demandeChequierPage.getToastifyText().contains(toastMessage));
    }

    @When("utilisateur soumet la demande sans remplir les champs requis")
    public void cliquerSurValidationSansChamps() {
        demandeChequierPage.clickValidBTN();
    }


    @Then("cinq messages d'erreur doivent apparaître, indiquant les champs manquants ou invalides")
    public void verifierMessagesErreur() throws InterruptedException {
        errorMessages = demandeChequierPage.getErrorMessages();
        Thread.sleep(5000); //à remplacer plus tard 29/04/2025

        assertEquals("Le nombre de messages d'erreur affichés n'est pas correct !", 5, errorMessages.size());
    }













}
