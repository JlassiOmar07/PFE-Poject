package steps;


import io.cucumber.java.en.*;


import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertEquals;

public class LoginPageSetps {



    @Given("L'utilisateur est sur la page de connexion PP")
    public void utilisateur_est_connecte() {
        System.out.println("i m here");
    }

    @When("Il saisit l'identifiant {string}")
    public void saisit_identifiant(String username){
        loginPage.setUsername(username);
    }

    @When("Il saisit un OTP invalide")
    public void saisit_OTP_invalide(){
        loginPage.setPassword();
    }

    @When("Il clique sur le bouton Se connecter")
    public void click_OnSubmit(){
        loginPage.clickOnSubmit();
    }

    @Then("Un message d'erreur apparaît")
    public void isErrorMessageDisplayed(){
        assertEquals("Les informations d'identification sont invalides", loginPage.getErrorMsg());
    }



}