package steps;
import io.cucumber.java.en.Given;

import io.cucumber.java.en.When;
import pages.AcceuilPage;
import pages.VirementPermanentPage;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertTrue;

public class VirementPermanentSteps  {


    private AcceuilPage acceuilPage;
    private VirementPermanentPage virementPermanentPage;



    @Given("l'utilisateur est connecté à l'application MyBusiness XX")
    public void userConnected() {
        acceuilPage = loginPage.clickDemo();
    }

    @When("l'utilisateur clique sur \"Créer un virement permanent\"")
    public void navigateToVirmentVermanentPage () {
        virementPermanentPage = acceuilPage.clickVirementPermanentPage();
        virementPermanentPage.clickCreate_transfer_BTN();
    }

    @When("l'utilisateur sélectionne un compte à débiter \"Compte Adria\"")
    public void selectFromDebitAccount(){
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        virementPermanentPage.selectFromDebitAccount(account);
    }

    @When("l'utilisateur sélectionne un compte à créditer \"Nicolas\"")
    public void setCreditAccount(){
        virementPermanentPage.setCreditAccount();
    }

    @When("l'utilisateur sélectionne une fréquence \"Mensuelle\"")
    public void selectFromFrequences(){
        String frequence = "Mensuelle";
        virementPermanentPage.selectFromFrequences(frequence);
    }
    @When("l'utilisateur saisit le montant \"100\"")
    public void setAmount(){
        virementPermanentPage.setAmount("100");
    }

    @When("l'utilisateur sélectionne la date de fin identique à la date d'exécution")
    public void setEndDate(){
        virementPermanentPage.setEndDate();
    }

    @When("l'utilisateur clique sur le bouton valider XX")
    public void clickValidButton (){
        virementPermanentPage.clickValidButton();
        assertTrue("L'URL devrait contenir 'https://www.mybusiness.uib.com.tn/app/virPermanentMultiDevise/show'", virementPermanentPage.getCurrentUrl().contains("https://www.mybusiness.uib.com.tn/app/virPermanentMultiDevise/show"));
    }

    @When("l'utilisateur confirme le virement en saisissant le code secret")
    public void enterTheSecretCode () throws InterruptedException {
        virementPermanentPage.clickSubmit_BTN2();
        virementPermanentPage.enterTheSecretCode();
    }

    @When("un message de succès est affiché XX")
    public void isSuccessMessageDisplayed(){
        virementPermanentPage.isSuccessMessageDisplayed();
    }


}

