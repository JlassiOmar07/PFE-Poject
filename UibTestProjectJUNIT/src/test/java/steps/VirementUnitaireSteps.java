package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AcceuilPage;
import pages.VirementUnitairePage;


import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertTrue;

public class VirementUnitaireSteps {

    private AcceuilPage acceuilPage;
    private VirementUnitairePage virementUnitairePage;



    @Given("l'utilisateur est connecté à UIB acceuil")
    public void userConnected() {
        acceuilPage = loginPage.clickDemo();
    }

    @When("il navigue vers créer un virment unitaire page")
    public void navigateToVirmentUnitairePage() {
        virementUnitairePage = acceuilPage.clickVirementUnitairePage();
        virementUnitairePage.clickCreate_transfer_BTN();
    }

    @When("l'utilisateur sélectionne Compte à débiter")
    public void selectDebitAccount() {
        String account = "3XXXXXXXX12 (Compte Adria) , 1 220,42 TND";
        virementUnitairePage.selectFromDebitAccount(account);
    }

    @When("l'utilisateur sélectionne Compte à créditer")
    public void selectCreditAccount() throws InterruptedException {
        virementUnitairePage.setCreditAccount();
    }


    @When("il saisit le montant {string}")
    public void enterAmount(String amount) {
        virementUnitairePage.setAmount("500");
    }


    @When("il clique sur Valider")
    public void clickValidButton() {
        virementUnitairePage.clickValidButton();
        assertTrue("L'URL devrait contenir 'https://www.mybusiness.uib.com.tn/app/virBeneficiaireMultiDevise/show/97543'", virementUnitairePage.getCurrentUrl().contains("https://www.mybusiness.uib.com.tn/app/virBeneficiaireMultiDevise/show/97543"));
        virementUnitairePage.clickSubmit_BTN2();
    }

    @When("il entre le code secret")
    public void enterTheSecretCode()  {
        virementUnitairePage.enterTheSecretCode();
    }

    @Then("un message de confirmation doit s'afficher")
    public void verifyConfirmationUrl (){
        virementUnitairePage.isSuccessMessageDisplayed();
    }






}

