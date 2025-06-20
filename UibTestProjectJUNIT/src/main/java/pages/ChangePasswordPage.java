package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Cette classe représente la page de changement de mot de passe.
 * Elle contient des méthodes pour interagir avec les champs de saisie et les boutons de validation.
 */
public class ChangePasswordPage {
    private WebDriver driver ;

    public ChangePasswordPage (WebDriver driver){
        this.driver = driver ;
    }

    // Sélecteurs des champs de mot de passe et du bouton de validation
    private By password = By.xpath("(//input[@type='password' and contains(@class, 'ReactPasswordStrength-input')])[1]");
    private By pass1 = By.xpath("(//input[@type='password' and contains(@class, 'ReactPasswordStrength-input')])[2]");
    private By pass2 = By.xpath ("(//input[@type='password' and contains(@class, 'ReactPasswordStrength-input')])[3]");
    private By validBtn = By.xpath("//button[@class=\"btnvalide btn btn-default\"]");
    private By errorMessages = By.xpath("//div[(@class=\"errorPasswor___1RDM7\")]");


    /**
     * Récupère la liste des messages d'erreur affichés.
     *
     * @return Liste des éléments Web représentant les messages d'erreur.
     */
    public List<WebElement> getErrorMessages(){
        return driver.findElements(errorMessages);
    }

    /**
     * Clique sur le bouton de validation pour soumettre le formulaire.
     */
    public void clickValidateButton(){
        driver.findElement(validBtn).click();
    }

    /**
     * Remplit le champ de l'ancien mot de passe.
     *
     * @param password Ancien mot de passe à saisir.
     */
    public void enterOldPassword(String password){
        WebElement ancienPasswordInput = driver.findElement(this.password);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='"+password+"';",ancienPasswordInput);

    }

    /**
     * Remplit le champ du nouveau mot de passe.
     *
     * @param password Nouveau mot de passe à saisir.
     */
    public void enterNewPassword(String password){
        WebElement newPasswordInput = driver.findElement(pass1);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='"+password+"';",newPasswordInput);
    }

    /**
     * Remplit le champ de confirmation du mot de passe.
     *
     * @param password Mot de passe à confirmer.
     */
    public void enterConfirmPassword(String password){
        WebElement newPasswordInput = driver.findElement(pass2);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='"+password+"';",newPasswordInput);
    }



}
