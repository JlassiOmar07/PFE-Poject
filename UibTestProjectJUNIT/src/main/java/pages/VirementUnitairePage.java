package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class VirementUnitairePage {

    private WebDriver driver ;

    private WebDriverWait wait;
    public VirementUnitairePage(WebDriver driver){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Locators (à ajuster selon le DOM réel)
    private By create_transfer_BTN = By.id("create-transfer_BTN");
    private By DebitAccount = By.xpath("//select[@class=\"formControls\"][contains(.,\"3XXXXXXXX12 (Compte Adria) , 1 220,42 TND\")]");

    private By amount = By.id("amount_TXTF");


    private By submit_BTN = By.id("submit_BTN");
    private By CreditAccount = By.id("react-select-2-input");
    private By submit_BTN2 = By.id("submit_BTN");

    private By validate_BTN = By.id("validate_BTN");
    private By successAlert = By.xpath("//div[contains(@class, 'alert-success')]//p");



    public void clickCreate_transfer_BTN(){
        wait.until(ExpectedConditions.elementToBeClickable(create_transfer_BTN)).click();
    }


    /**
     * Récupère le menu déroulant des comptes à débiter sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des comptes à débiter.
     */
    private Select findDebitAccountDropDown(){
        return new Select(driver.findElement(DebitAccount));
    }

    /**
     * Sélectionne un compte dans la liste déroulante par son texte visible.
     * @param compte Le compte à sélectionner.
     */
    public void selectFromDebitAccount(String compte) {
        findDebitAccountDropDown().selectByVisibleText(compte);
    }

    /**
     * Récupère la liste des comptes sélectionnés.
     * @return Une liste contenant les comptes sélectionnés.
     */
    public List<String> getSelectedOptionsAccounts() {
        return findDebitAccountDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


    public void setCreditAccount(){
        driver.findElement(CreditAccount).sendKeys("Nicolas, 1XXXXXXXXXXXXXXXXX88");
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();

    }

    public void setAmount (String monatant){
        driver.findElement(amount).sendKeys(monatant);
    }

    public void clickValidButton (){
        driver.findElement(submit_BTN).click();
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    public void clickSubmit_BTN2(){
        driver.findElement(submit_BTN2).click();
    }

    public void enterTheSecretCode()  {
        // Entrer un code secret, par exemple "2580"
        String secretCode = "2580";

        // Pour chaque chiffre du code
        for (char digit : secretCode.toCharArray()) {
            List<WebElement> buttons = driver.findElements(By.id("keypad"));
            for (WebElement button : buttons) {
                if (button.getText().equals(String.valueOf(digit))) {
                    button.click();
                    break;
                }
            }

        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(validate_BTN)).click();
    }

    public void isSuccessMessageDisplayed (){
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        assertTrue("Message non affiché", messageElement.isDisplayed());
    }

}
