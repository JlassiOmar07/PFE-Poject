package pages;

import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class AcceuilPage {

    private WebDriver driver ;
    private WebDriverWait wait ;

    public AcceuilPage (WebDriver driver){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Localisation des éléments de la page par XPath
    private By userButton = By.xpath("//li[@class=\"undefined user-nav dropdown\"]");
    private By userBox = By.xpath("//a[@role=\"menuitem\"][contains(.,\"Se déconnecter\")]");
    private By deconnexionbutton = By.xpath("//button[@type=\"button\"][contains(.,\"Se déconnecter\")]");
    private By messagerieButton = By.xpath("//a[@href=\"/app/Messagerie\"][contains(.,\"Messagerie\")]");
    private By administrationLink = By.id("administration_LNK");
    private By changePassword = By.xpath("//a[text()='Changement de mot de passe']");
    private By servicesLink = By.xpath("//a//i[@class=\"iServices\"]");

    private By iPayment = By.xpath("//a//i[@class=\"iPayment\"]");
    private By virementUnitaire = By.id("transfer_LNK");
    private By virementPermanent = By.xpath("//div[@id='subHeader' and contains(text(),'Virements Domestiques')]/following-sibling::a[contains(.,'Virement Permanent')]");
    private By virementMultiple = By.xpath("//a[@href=\"/app/multiTransfer/request\"]");
    private By chequeRequest = By.xpath("//a[contains(@href, '/app/request/cheque_bank')]");
    private By orderDinars  = By.xpath("//a[contains(@href, '/app/request/local_devise')]");
    private By orderDevise = By.xpath("//a[contains(@href, '/app/request/devise')]");
    private By remiseDordreXmlMon = By.xpath("//a[contains(@href, '/app/remiseDordreXmlMon')]");
    private By remiseDordre = By.xpath("//a[contains(@href, '/app/remiseDordre')]");
    private By menu = By.xpath("//nav[@class=\"vMenu\"]");


    // Méthode pour cliquer sur le bouton utilisateur (ouvre le menu déroulant)
    public void clickOnUserButton(){

        driver.findElement(userButton).click();
    }
    // Méthode pour déplacer la souris vers l'option "Se déconnecter" et cliquer dessus
    public void MoveToDeconnexion() {

        Actions actions = new Actions(driver);
        WebElement deconnexion = driver.findElement(userBox);
        actions.moveToElement(deconnexion).click().perform();
    }
    // Méthode pour cliquer sur le bouton "Se déconnecter"
    public void clickOnDeconnexion () throws InterruptedException {
        driver.findElement(deconnexionbutton).click();
    }
    // Méthode pour cliquer sur le bouton "Messagerie" et retourner une instance de MessageriePage
    public MessageriePage clickOnMessagerie (){
        driver.findElement(messagerieButton).click();
        return new MessageriePage(driver);
    }

    public ChangePasswordPage clickChangePassword() {
        driver.findElement(administrationLink).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(changePassword)).click().perform();

        return new ChangePasswordPage(driver);

    }


    public DemandeChequierPage clickDemandeChequierPage(){
        driver.findElement(servicesLink).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(chequeRequest)).click().perform();
        return new DemandeChequierPage(driver);
    }

    public CommandeDinarsPage clickCommandeDinarsPage (){
        driver.findElement(servicesLink).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(orderDinars)).click().perform();
        return new CommandeDinarsPage(driver);
    }

    public CommandeDevisePage clickCommandeDevisePage () {
        driver.findElement(servicesLink).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(orderDevise)).click().perform();
        return new CommandeDevisePage(driver);
    }


    public DemandeRemiseDordre clickDemandeRemiseDordre () {
        driver.findElement(servicesLink).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(remiseDordre)).click().perform();
        return new DemandeRemiseDordre(driver);
    }

    public VirementUnitairePage clickVirementUnitairePage() {
        driver.findElement(iPayment).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(virementUnitaire)).click().perform();
        return new VirementUnitairePage(driver);
    }

    public VirementPermanentPage clickVirementPermanentPage() {
        driver.findElement(iPayment).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(ExpectedConditions.visibilityOfElementLocated(virementPermanent))).click().perform();
        return new VirementPermanentPage(driver);
    }

    public VirementMultiplePage clickVirementMultiplePage() {
        driver.findElement(iPayment).click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(virementMultiple)).click().perform();
        return new VirementMultiplePage(driver);
    }


    public boolean testResponsiveLayoutOnMobileScreen() throws IOException {
        driver.manage().window().setSize(new Dimension(390, 844)); //iphone
        File source = ((ChromeDriver)driver).getScreenshotAs(OutputType.FILE);
        FileHandler.copy(source, new File("Responsive Layout On Mobile Screen.png"));
        return driver.findElement(menu).isDisplayed();

    }

    public boolean testResponsiveLayoutOnTabletScreen(){
        driver.manage().window().setSize(new Dimension(1024,768)); //iPad
        return driver.findElement(menu).isDisplayed();
    }

    public boolean testResponsiveLayoutOnDesktopScreen (){
        driver.manage().window().setSize(new Dimension(1920, 1080));

        return driver.findElement(menu).isDisplayed();
    }

}
