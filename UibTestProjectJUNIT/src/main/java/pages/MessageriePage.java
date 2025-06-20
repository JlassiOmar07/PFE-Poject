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

        import static org.junit.Assert.assertTrue;

/**
 * Cette classe représente la page Messagerie et encapsule les interactions possibles.
 */
public class MessageriePage {

    private WebDriver driver;
    private WebDriverWait wait ;

    // Localisation des éléments de la page
    private By newMessageButton = By.xpath("//button[@type=\"button\"][contains(.,\"Nouveau message\")]");
    private By destinataireDropDown = By.xpath("//select[@class=\"formControls\"][contains(.,\" Mon chargé d'affaires\")]");
    private By sujetDropDown = By.xpath("//select[@class=\"formControls\"][contains(.,\" Contact Technical Support\")]");
    private By textArea = By.xpath("//div[@data-gramm=\"false\"]");
    private By inputFile = By.id("PJFiles");
    private By uploadButton = By.xpath("//label[contains(text(),'Fichier')]/following::button[1]");
    private By envoyerButton = By.xpath("//label[contains(text(),'Fichier')]/following::button[3]");

    private By uploadAlert = By.xpath("//div[@class=\"Attachedbloc___1SvZl alert alert-info\"]");

    private By modalErrorText = By.xpath("//div[@class='modal-body']/h4");
    private By uploadbtn = By.xpath("//button[contains(@class, 'btnvalide')]//i[@class='fa fa-plus']");
    private By successAlert = By.xpath("//div[@class=\"alert alert-success alert-dismissable\"]");

    public MessageriePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Clique sur le bouton "Nouveau message" pour ouvrir le formulaire de rédaction.
     */
    public void clickOnNouveauMessage() {
        driver.findElement(newMessageButton).click();
    }

    /**
     * Récupère le menu déroulant des destinataires sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des destinataires.
     */
    private Select findDestinataireDropDown() {
        return new Select(driver.findElement(destinataireDropDown));
    }

    /**
     * Sélectionne un destinataire dans la liste déroulante par son texte visible.
     * @param destinataire Le nom du destinataire à sélectionner.
     */
    public void selectFromDestinataire(String destinataire) {
        findDestinataireDropDown().selectByVisibleText(destinataire);
    }

    /**
     * Récupère la liste des destinataires sélectionnés.
     * @return Une liste contenant les noms des destinataires sélectionnés.
     */
    public List<String> getSelectedOptionsDestinaire() {
        return findDestinataireDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Récupère le menu déroulant des sujets sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des sujets.
     */
    private Select findSujetDropDown() {
        return new Select(driver.findElement(sujetDropDown));
    }

    /**
     * Sélectionne un sujet dans la liste déroulante par son texte visible.
     * @param sujet Le sujet à sélectionner.
     */
    public void selectFromSujet(String sujet) {
        findSujetDropDown().selectByVisibleText(sujet);
    }

    /**
     * Récupère la liste des sujets sélectionnés.
     * @return Une liste contenant les sujets sélectionnés.
     */
    public List<String> getSelectedOptionsSujet() {
        return findSujetDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Saisit du texte dans la zone de message.
     * @param text Le texte à insérer dans la zone de message.
     */
    public void setTextArea(String text) {
        driver.findElement(textArea).sendKeys(text);
    }

    /**
     * Ajoute un fichier en fournissant son chemin absolu.
     * @param absolutePathOfFile Le chemin complet du fichier à télécharger.
     */
    public void uploadTheFile(String absolutePathOfFile) {
        driver.findElement(inputFile).sendKeys(absolutePathOfFile);


    }


    public String uploadButtonIsEnable() {
        wait.until(ExpectedConditions.elementToBeClickable(uploadbtn)).click();
        List<WebElement> elements = driver.findElements(uploadAlert);
        if (!elements.isEmpty()) {
            return "fichier téléchargé avec succès" ;
        } else {
            return "le fichier n'a pas été téléchargé avec succès";
        }

    }
    /**
     * Vérifie si le bouton "Envoyer" est activé et clique dessus si possible.
     * @return true si le bouton est activé et a été cliqué, sinon false.
     */
    public void clickEnvoyerButton() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(envoyerButton)).click();
        //driver.findElement(envoyerButton).click();

    }

    /**
     * Récupère le texte du message d'erreur affiché dans la modale.
     * @return Le texte du message d'erreur.
     */
    public String getModalErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(modalErrorText)).getText();
    }


    public void isSuccessMessageDisplayed (){
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(successAlert));
        assertTrue("Message non affiché", messageElement.isDisplayed());
    }
}

