package steps;

import io.cucumber.java.en.*;

import pages.AcceuilPage;
import pages.MessageriePage;


import static hooks.Hooks.loginPage;
import static org.junit.Assert.*;

public class MessagerieSteps{


    private AcceuilPage acceuilPage;
    private MessageriePage messageriePage;


    @Given("l'utilisateur est connecté et sur la page de messagerie")
    public void utilisateur_est_connecte_et_sur_la_page_de_messagerie() throws InterruptedException {
        acceuilPage = loginPage.clickDemo();
        messageriePage = acceuilPage.clickOnMessagerie();
    }

    @When("un nouveau message est initié")
    public void un_nouveau_message_est_initié() {
        messageriePage.clickOnNouveauMessage();
    }

    @When("le destinataire {string} est sélectionné")
    public void le_destinataire_est_selectionne(String destinataire) {
        messageriePage.selectFromDestinataire(destinataire);
        assertTrue(messageriePage.getSelectedOptionsDestinaire().contains(destinataire));
    }

    @When("le sujet {string} est sélectionné")
    public void le_sujet_est_selectionne(String sujet) {
        messageriePage.selectFromSujet(sujet);
        assertTrue(messageriePage.getSelectedOptionsSujet().contains(sujet));
    }

    @When("le message {string} est saisi")
    public void le_message_est_saisi(String message) {
        messageriePage.setTextArea(message);
    }

    @When("un fichier valide est téléchargé")
    public void un_fichier_valide_est_telecharge() {
        messageriePage.uploadTheFile("C:\\workspace\\testautomation\\webDriver_java\\UibTestProject\\resources\\Screenshot 2025-03-24 192518.png");
        assertEquals("fichier téléchargé avec succès",messageriePage.uploadButtonIsEnable());

    }

    @When("Cliquez sur le bouton Soumettre")
    public void le_bouton_doit_etre_active() throws InterruptedException {
        messageriePage.clickEnvoyerButton();
    }

    @Then("Un message de réussite devrait apparaître")
    public void SuccessMessageDisplayed(){
        messageriePage.isSuccessMessageDisplayed();
    }

    @When("un fichier non autorisé est téléchargé")
    public void fichier_non_autorise_est_telecharge() {
        messageriePage.uploadTheFile("C:\\workspace\\testautomation\\webDriver_java\\UibTestProject\\resources\\chrome-win64.zip");
    }

    @Then("un message d'erreur avec {string} doit s'afficher")
    public void message_erreur_avec_texte(String messageErreur) {
        assertTrue("Le message d'erreur attendu n'est pas affiché.",
                messageriePage.getModalErrorText().contains(messageErreur));
    }

}