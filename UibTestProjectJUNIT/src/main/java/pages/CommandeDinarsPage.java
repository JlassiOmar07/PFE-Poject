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

public class CommandeDinarsPage {
    private WebDriver driver ;
    private WebDriverWait wait ;

    public CommandeDinarsPage (WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



    private By nouvelleDemande = By.id("create-request_BTN");
    private By accounts = By.xpath("//select[@class=\"formControls\"][contains(.,\"3XXXXXXXX12 (Compte Adria) , 1 220,42 TND\")]");

    private By amount = By.name("amount");
    private By dateInput = By.xpath("//div[@class=\"react-datepicker__input-container\"]");
    private By allDays = By.cssSelector("div.react-datepicker__day");
    private By dayToSelect = By.xpath("//div[contains(text(),'27')]");
    private By validBTN = By.xpath("//button[@class=\"btnvalide btn btn-default\"]");

    private By errorMessage = By.xpath("//span[@class='help-block']");

    /**
     * Clique sur le bouton "Nouvelle demande" pour ouvrir le formulaire de rédaction.
     */
    public void clickNouvelleDemande (){
        wait.until(ExpectedConditions.visibilityOfElementLocated(nouvelleDemande)).click();

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

    public void setAmount (String Amount){
        driver.findElement(amount).sendKeys(Amount);
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

    public void clickValidBTN (){
        driver.findElement(validBTN).click();
    }

    public String isErrorMessageDisplayed(){
        List<WebElement> elements = driver.findElements(errorMessage);
        if (!elements.isEmpty()) {
            return "Element exists!" ;
        } else {
            return "Element does not exist!";
        }
    }

}
