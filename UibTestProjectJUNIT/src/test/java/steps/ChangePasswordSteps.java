package steps;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebElement;
import pages.AcceuilPage;
import pages.ChangePasswordPage;
import java.util.List;


import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangePasswordSteps {

    private AcceuilPage acceuilPage;
    private ChangePasswordPage changePasswordPage;
    private List<WebElement> errorMessages;

    @Given("utilisateur est connecté")
    public void utilisateur_est_connecte(){

        acceuilPage = loginPage.clickDemo();
    }

    @When("il navigue vers la page de changement de mot de passe")
    public void navigue_vers_changement_mdp() {
        changePasswordPage = acceuilPage.clickChangePassword();
    }

    @When("il clique sur le bouton de validation sans remplir les champs")
    public void clique_bouton_validation_sans_remplir() {
        changePasswordPage.clickValidateButton();
        errorMessages = changePasswordPage.getErrorMessages();
    }

    @Then("trois messages d'erreur doivent s'afficher")
    public void verifier_trois_messages_erreur() {
        assertEquals("Le nombre de messages d'erreur affichés n'est pas correct !", 3, errorMessages.size());
        for (WebElement error : errorMessages) {
            assertTrue("Un des messages d'erreur n'est pas affiché !", error.isDisplayed());
        }
    }

    @When("il saisit l'ancien mot de passe {string}")
    public void saisir_ancien_mdp(String ancien) {
        changePasswordPage.enterOldPassword(ancien);
    }

    @When("il saisit le nouveau mot de passe {string}")
    public void saisir_nouveau_mdp(String nouveau) {
        changePasswordPage.enterNewPassword(nouveau);
    }

    @When("il saisit la confirmation du mot de passe {string}")
    public void saisir_confirmation_mdp(String confirmation) {
        changePasswordPage.enterConfirmPassword(confirmation);
    }

    @When("il clique sur le bouton de validation du mot de passe")
    public void clique_sur_valider() {
        changePasswordPage.clickValidateButton();
    }

    @Then("un message d'erreur doit s'afficher pour la non-concordance des mots de passe")
    public void verifier_erreur_non_concordance() {
        errorMessages = changePasswordPage.getErrorMessages();
        assertEquals("Un seul message d'erreur attendu pour la non-concordance des mots de passe", 3, errorMessages.size());
    }
}
