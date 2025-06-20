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

public class VirementPermanentPage {
    private WebDriver driver ;
    private WebDriverWait wait ;
    public VirementPermanentPage (WebDriver driver){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By create_transfer_BTN = By.id("create-transfer_BTN");

    private By DebitAccount = By.xpath("//select[@class=\"formControls\"][contains(.,\"3XXXXXXXX12 (Compte Adria) , 1 220,42 TND\")]");

    private By CreditAccount = By.id("react-select-2-input");
    private By frequence = By.xpath("//select[@class=\"formControls\"][contains(.,\"Mensuelle\")]");
    private By amount = By.id("amount_TXTF");

    private By submit_BTN = By.id("submit_BTN");

    private By submit_BTN2 = By.id("submit_BTN");

    private By executionDate = By.id("execution-date_TXTF");
    private By EndDate = By.id("end-date_TXTF");
    private By validate_BTN = By.id("validate_BTN");
    private By successAlert = By.xpath("//div[contains(@class, 'alert-success')]//p");

    private By activeDay = By.cssSelector("[tabindex='0']");
    private By allDays = By.cssSelector("div.react-datepicker__day");

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

    /**
     * Récupère le menu déroulant des fréquences sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des fréquences.
     */
    private Select findFrequencesDropDown(){
        return new Select(driver.findElement(frequence));
    }

    /**
     * Sélectionne un Fréquence dans la liste déroulante par son texte visible.
     * @param Frequence Le compte à sélectionner.
     */
    public void selectFromFrequences(String Frequence) {
        findFrequencesDropDown().selectByVisibleText(Frequence);
    }

    /**
     * Récupère la liste des Fréquences sélectionnés.
     * @return Une liste contenant les Fréquences sélectionnés.
     */
    public List<String> getSelectedOptionsFrequences() {
        return findFrequencesDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void setAmount (String montant){
        driver.findElement(amount).sendKeys(montant);
    }


    public void clickValidButton (){
        driver.findElement(submit_BTN).click();
    }

    public void clickSubmit_BTN2(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(submit_BTN2)).click();
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    public String getExecutionDateText (){
        return driver.findElement(activeDay).getText();

    }

    public void setEndDate(){
       driver.findElement(EndDate).click();
        // 3. Chercher tous les jours visibles
        List<WebElement> AllDays = driver.findElements(allDays);

        // 4. Cliquer sur le jour qui a tabindex="0" (c'est le jour actif)
        for (WebElement day : AllDays) {
            if (day.getAttribute("tabindex") != null && day.getAttribute("tabindex").equals("0")) {
                day.click();
                break; // très important de sortir après avoir cliqué
            }
        }
    }

    public void enterTheSecretCode () throws InterruptedException {
       // Entrer un code secret, par exemple "2580"
        String secretCode = "2580";

        // Pour chaque chiffre du code
        for (char digit : secretCode.toCharArray()) {
            List<WebElement> buttons = driver.findElements(By.id("keypad"));
            for (WebElement button : buttons) {
                if (button.getText().equals(String.valueOf(digit))) {
                    button.click();
                    Thread.sleep(3000);
                    break;
                }
            }
        }

        wait.until(ExpectedConditions.elementToBeClickable(validate_BTN)).click();


    }

    public void isSuccessMessageDisplayed (){
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        assertTrue("Message non affiché", messageElement.isDisplayed());
    }


}
