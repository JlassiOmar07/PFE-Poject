package steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AcceuilPage;
import pages.CommandeDinarsPage;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.*;

public class CommandeDinarSteps {

    private AcceuilPage acceuilPage;
    private CommandeDinarsPage commandeDinarsPage;




    @Given("l'utilisateur est connecté et se trouve sur la page d'accueil")
    public void utilisateurEstConnecteSurPageAccueil() {

        acceuilPage = loginPage.clickDemo();
    }

    @When("navigation vers la section de commande de dinars")
    public void naviguerVersCommandeDinars() {
        commandeDinarsPage = acceuilPage.clickCommandeDinarsPage();
    }

    @When("ouverture du formulaire de nouvelle demande")
    public void ouvrirFormulaireNouvelleDemande() throws InterruptedException {
        commandeDinarsPage.clickNouvelleDemande();
    }

    @When("sélection d’un compte, saisie du montant et choix d’une date")
    public void remplirFormulaireCommande() throws InterruptedException {
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        commandeDinarsPage.selectFromAccounts(account);
        assertTrue("Aucun compte sélectionné", commandeDinarsPage.getSelectedOptionsAccounts().contains(account));

        commandeDinarsPage.setAmount("800");
        commandeDinarsPage.setDateInput();
    }

    @When("soumission de la commande")
    public void soumettreCommande() {
        commandeDinarsPage.clickValidBTN();
    }

    @Then("validation réussie et absence de message d’erreur")
    public void validationCommandeReussie() {
        assertEquals("Element does not exist!",commandeDinarsPage.isErrorMessageDisplayed());

    }

    @When ("sélection d’un compte uniquement")
    public void selectionnerCompteSeul() throws InterruptedException {
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        commandeDinarsPage.selectFromAccounts(account);

    }

    @When("soumission de la commande sans")
    public void soumettreCommandeSansChampsObligatoires (){
        commandeDinarsPage.clickValidBTN();
    }

    @Then("affichage d’un message d’erreur signalant les champs manquants")
    public void  afficherMessageErreurChampsManquants(){
        assertEquals("Element exists!",commandeDinarsPage.isErrorMessageDisplayed());
    }


}



