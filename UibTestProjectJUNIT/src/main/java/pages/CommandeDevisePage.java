package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CommandeDevisePage {

    private WebDriver driver ;


    public CommandeDevisePage (WebDriver driver){
        this.driver = driver ;
    }

    private By nouvelleDemande = By.id("create-request_BTN");
    private By accounts = By.xpath("//select[@class=\"formControls\"][contains(.,\"3XXXXXXXX12 (Compte Adria) , 1 220,42 TND\")]");
    private By motif = By.name("reason");
    private By amount = By.name("amount");
    private By errorMessage = By.xpath("//span[@class=\"help-block\"]");
    private By validBTN = By.xpath("//button[@class=\"btnvalide btn btn-default\"]");
    private By devise = By.xpath("//select[@class=\"formControls\"][contains(.,\"DINAR ALGERIEN\")]");
    private By dateInput = By.xpath("//div[@class=\"react-datepicker__input-container\"]");
    private By dayToSelect = By.xpath("//div[contains(text(),'27')]");
    private By allDays = By.cssSelector("div.react-datepicker__day");
    private By activeDay = By.cssSelector("[tabindex='0']");


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

    /**
     * Récupère le menu déroulant des devises sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des devises.
     */
    private Select findDeviseDropDown (){
        return new Select(driver.findElement(devise));
    }

    /**
     * Sélectionne un sujet dans la liste déroulante par son texte visible.
     * @param Devise Le sujet à sélectionner.
     */
    public void selectFromDevise(String Devise) {
        findDeviseDropDown().selectByVisibleText(Devise);
    }

    /**
     * Récupère la liste des devises sélectionnés.
     * @return Une liste contenant les devises sélectionnés.
     */
    public List<String> getSelectedOptionsDevise() {
        return findDeviseDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


    public void setAmount (String Amount){
        driver.findElement(amount).sendKeys(Amount);
    }

    public void setMotif (String reason){
        driver.findElement(motif).sendKeys(reason);
    }

    public void clickValidBTN (){
        driver.findElement(validBTN).click();
    }

    public void setDateInput () throws InterruptedException {
        driver.findElement(dateInput).click();
        List<WebElement> AllDays = driver.findElements(allDays);

        for (WebElement day : AllDays) {
            if (day.getAttribute("tabindex") != null && day.getAttribute("tabindex").equals("0")) {
                day.click();
                break;
            }
        }
    }

    public String getErrorMessage (){

        List<WebElement> elements = driver.findElements(errorMessage);
        if (!elements.isEmpty()) {
            return "Element exists!" ;
        } else {
            return "Element does not exist!";
        }


    }


}
