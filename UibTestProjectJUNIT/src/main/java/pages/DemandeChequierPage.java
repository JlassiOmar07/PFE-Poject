package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DemandeChequierPage {


    private WebDriver driver ;
    private WebDriverWait wait ;
    public DemandeChequierPage(WebDriver driver ){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Localisation des éléments de la page
    private By nouvelleDemande = By.id("create-request_BTN");
    private By accounts = By.xpath("//select[@class=\"formControls\"][contains(.,\"3XXXXXXXX12 (Compte Adria) , 1 220,42 TND\")]");
    private By benificiareName = By.xpath("//input[@name=\"benificiareName\"]");

    private By agence = By.xpath("//input[@name=\"agence\"]");

    private By amount = By.xpath("//input[@name=\"amount\"]");
    private By reason = By.xpath("//input[@name=\"reason\"]");

    private By validBTN = By.xpath("//button[@class=\"btnvalide btn btn-default\"]");

    private By errorMessages = By.xpath("//span[@class=\"help-block\"]");

    private By Toastify = By.xpath("//div[@class=\"Toastify__toast-body\"]");

    /**
     * Clique sur le bouton "Nouvelle demande" pour ouvrir le formulaire de rédaction.
     */
    public void clickNouvelleDemande (){
        driver.findElement(nouvelleDemande).click();

    }

    /**
     * Récupère le menu déroulant des comptes sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des comptes.
     */
    private Select findAccountDropDown (){
        return new Select(driver.findElement(accounts));
    }

    /**
     * Sélectionne un sujet dans la liste déroulante par son texte visible.
     * @param compte Le sujet à sélectionner.
     */
    public void selectFromAccounts(String compte) {
        findAccountDropDown().selectByVisibleText(compte);
    }

    /**
     * Récupère la liste des comptes sélectionnés.
     * @return Une liste contenant les comptes sélectionnés.
     */
    public List<String> getSelectedOptionsAccounts() {
        return findAccountDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


    public void setBenificiareName (String BenificiareName){
        driver.findElement(benificiareName).sendKeys(BenificiareName);
    }

    public void setAgence (String Agence){
        driver.findElement(agence).sendKeys(Agence);
    }



    public void setAmount (String Amount){
        driver.findElement(amount).sendKeys(Amount);
    }

    public void setReason (String Reason){
        driver.findElement(reason).sendKeys(Reason);
    }


    public void clickValidBTN (){
        driver.findElement(validBTN).click();
    }

    public List<WebElement> getErrorMessages (){
        return driver.findElements(errorMessages);

    }

    public String getToastifyText (){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(Toastify)).getText();
    }



}
