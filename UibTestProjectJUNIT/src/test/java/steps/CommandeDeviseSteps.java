package steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import pages.AcceuilPage;
import pages.CommandeDevisePage;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.*;

public class CommandeDeviseSteps {


    private AcceuilPage acceuilPage;
    private CommandeDevisePage commandeDevisePage ;



    @Given("l'utilisateur UIB est sur la page d'accueil après s'être connecté")
    public void userConnected(){
        acceuilPage = loginPage.clickDemo();
    }

    @When("accède à la page de commande de devises")
    public void userGoToCommandeDevisePage (){
        commandeDevisePage = acceuilPage.clickCommandeDevisePage();

    }

    @When("click sur le bouton Nouvelle demande")
    public void userClickNewButton (){
        commandeDevisePage.clickNouvelleDemande();
    }

    @When("sélectionne un compte")
    public void CompteSelection () throws InterruptedException {
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        commandeDevisePage.selectFromAccounts(account);
        assertTrue("Aucun compte sélectionné", commandeDevisePage.getSelectedOptionsAccounts().contains(account));
    }

    @When("choisit une devise")
    public void DeviseSelection () throws InterruptedException {
        String devise = "DINAR ALGERIEN";
        commandeDevisePage.selectFromDevise(devise);
        assertTrue("Aucune devise sélectionnée", commandeDevisePage.getSelectedOptionsDevise().contains(devise));
    }

    @When("saisit le montant")
    public void MonatantSelection () throws InterruptedException {
        commandeDevisePage.setAmount("800");
    }

    @When("sélectionne le motif")
    public void MotifSelection (){
        commandeDevisePage.setMotif("Reason");
    }


    @When("sélectionne la date")
    public void DateSelection () throws InterruptedException {
        commandeDevisePage.setDateInput();
    }
    @When("click sur le bouton de validation")
    public void validationButton(){
        commandeDevisePage.clickValidBTN();
    }



    @Then("la commande est envoyée aucune erreur ne s'affiche")
    public void check_error_message (){
        assertEquals("Element does not exist!",commandeDevisePage.getErrorMessage());
    }

    @Then ("un message d'erreur s'affiche omar")
    public void trouver_message_erreur (){
        assertEquals("Element exists!",commandeDevisePage.getErrorMessage());
    }




}
