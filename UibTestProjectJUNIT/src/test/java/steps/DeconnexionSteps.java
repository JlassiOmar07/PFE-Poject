package steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AcceuilPage;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertEquals;

public class DeconnexionSteps  {

    private AcceuilPage acceuilPage;
    @Given("utilisateur connecté")
    public void utilisateur_connecte() throws InterruptedException {
        acceuilPage = loginPage.clickDemo();
    }

    @When("deconnexion processus")
    public void deconnexion_processus() throws InterruptedException {
        acceuilPage.clickOnUserButton();
        acceuilPage.MoveToDeconnexion();
        acceuilPage.clickOnDeconnexion();
        Thread.sleep(2000);
    }

    @Then("verification redirection")
    public void verification_redirection() throws InterruptedException {

        String title = loginPage.getTitle();

        assertEquals(
                "L'utilisateur n'a pas été redirigé vers la page de login après Deconnexion. Titre obtenu : " + title,
                "UIB Société Générale | MYBUSINESS",
                title
        );
    }
}