package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    private WebDriver driver ;
    private WebDriverWait wait ;

    public LoginPage(WebDriver driver){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private By username = By.id("username_TXTF");
    private By password = By.id("password_TXTF");
    private By demoButton = By.xpath("//button[@class=\"btn\"]");
    private By submit_BTN = By.id("submit_BTN");
    private By errorMsg = By.xpath("//div[@class=\"text-danger\"]");

    private By buttons = By.xpath("//div[@class=\"btnMatrix___aTc_l\"]//button");

    public AcceuilPage clickDemo()   {
        wait.until(ExpectedConditions.visibilityOfElementLocated(demoButton)).click();
        return new AcceuilPage(driver);
    }

    public void setUsername (String username){
        wait.until(ExpectedConditions.visibilityOfElementLocated(this.username)).sendKeys(username);
    }
    public void setPassword ()  {
        driver.findElement(password).click();
        List<WebElement> Buttons = driver.findElements(buttons);

        // Parcourir chaque bouton et cliquer uniquement sur ceux qui ne sont pas désactivés
        for (WebElement button : Buttons) {
            // Vérifier si le bouton n'est PAS désactivé
            if (button.getAttribute("disabled") == null || !button.getAttribute("disabled").equals("true")) {
                // Cliquer sur le bouton s'il est actif
                button.click();
                System.out.println("Clic sur le bouton : " + button.getText());
            }
        }

    }


    public void clickOnSubmit (){
        driver.findElement(submit_BTN).click();
    }
    public String getErrorMsg (){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).getText();
    }

    public String getTitle(){
        return driver.getTitle();
    }

    public void hhhh (){
        driver.manage().window().setSize(new Dimension(375, 667));//Taille iPhone
    }

    public void waitForFormVisible() {
        WebDriverWait waitTitle = new WebDriverWait(driver, Duration.ofSeconds(305)); // 5 minutes timeout
        try {
            waitTitle.until(ExpectedConditions.titleIs("UIB Société Générale | MYBUSINESS"));
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new AssertionError("La redirection vers la page de login n'a pas eu lieu dans le délai imparti.", e);
        }
    }



}
