package steps;


import hooks.Hooks;
import io.cucumber.java.en.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertEquals;

import java.time.Duration;

public class SessionTimeoutSteps {



    @Given("l'utilisateur est connecté à l'application")
    public void utilisateur_est_connecte_a_l_application() {
        loginPage.clickDemo();
    }

    @When("la session expire après '5' minutes d'inactivité")
    public void session_expire_apres_minutes_d_inactivite() {
        loginPage.waitForFormVisible();

    }

    @Then("l'utilisateur est redirigé vers la page de login avec le titre 'UIB Société Générale | MYBUSINESS'")
    public void utilisateur_est_redirige_vers_la_page_de_login() {
        String title = loginPage.getTitle();
        assertEquals(
                "L'utilisateur n'a pas été redirigé vers la page de login après timeout. Titre obtenu : " + title,
                "UIB Société Générale | MYBUSINESS",
                title
        );
    }
}