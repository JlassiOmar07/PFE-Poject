package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DemandeRemiseDordre {
    private WebDriver driver ;
    private WebDriverWait wait ;

    public DemandeRemiseDordre(WebDriver driver ){
        this.driver = driver ;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private By compteDebiter = By.name("compteDebiterId");
    private By natureRemise = By.name("natureRemise");
    private By typeRemise = By.name("typeRemise");
    private By reference = By.name("referenceInterne");
    private By motif = By.name("motifOrdre");
    private By montantGlobal = By.name("montantGlobal");
    private By nombreVirements = By.name("nombreVirements");

    private By dateExecution = By.xpath("//div[@class=\"react-datepicker__input-container\"]");
    private By date = By.xpath("//div[contains(text(),'16')]");
    private By inputFile = By.id("file");

    private By validBTN = By.xpath("//button[@class=\"btnvalide btn btn-default\"]");


    /**
     * Récupère le menu déroulant des comptes d'opération sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des comptes d'opération.
     */
    private Select findcompteDebiterDropDown (){

        return new Select(driver.findElement(compteDebiter));
    }

    /**
     * Sélectionne un compte d'opération dans la liste déroulante par son texte visible.
     * @param compte Le compte d'opération à sélectionner.
     */
    public void selectFromCompteDebiter(String compte) {
        findcompteDebiterDropDown().selectByVisibleText(compte);
    }

    /**
     * Récupère la liste des devises sélectionnés.
     * @return Une liste contenant les devises sélectionnés.
     */
    public List<String> getSelectedOptionsCompteDebiter() {
        return findcompteDebiterDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Récupère le menu déroulant des natures de la remise sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des natures de la remise.
     */
    private Select findNatureRemiseDropDown (){
        return new Select(driver.findElement(natureRemise));
    }

    /**
     * Sélectionne un nature de la remise dans la liste déroulante par son texte visible.
     * @param nature Le nature de la remise à sélectionner.
     */
    public void selectFromNatureRemise(String nature) {
        findNatureRemiseDropDown().selectByVisibleText(nature);
    }

    /**
     * Récupère la liste des natures de la remise sélectionnés.
     * @return Une liste contenant les natures de la remise sélectionnés.
     */
    public List<String> getSelectedOptionsNatureRemise() {
        return findNatureRemiseDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Récupère le menu déroulant des types de remise sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des types de remise.
     */
    private Select findTypeRemiseDropDown (){
        return new Select(driver.findElement(typeRemise));
    }

    /**
     * Sélectionne un type de remise dans la liste déroulante par son texte visible.
     * @param type Le type de remise à sélectionner.
     */
    public void selectFromTypeRemise(String type) {
        findTypeRemiseDropDown().selectByVisibleText(type);
    }

    /**
     * Récupère la liste des types de remise sélectionnés.
     * @return Une liste contenant les types de remise sélectionnés.
     */
    public List<String> getSelectedOptionsTypeRemise() {
        return findTypeRemiseDropDown().getAllSelectedOptions()
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void setReference (String Reference){
        driver.findElement(reference).sendKeys(Reference);
    }

    public void setMotif (String Motif){
        driver.findElement(motif).sendKeys(Motif);
    }

    public void setMontantGlobal (String montant){
        driver.findElement(montantGlobal).sendKeys(montant);
    }


    public void setNombreVirements (String nombre){
        driver.findElement(nombreVirements).sendKeys(nombre);
    }


    public void setDateExecution (){
        driver.findElement(dateExecution).click();
        driver.findElement(date).click();
    }

    /**
     * Ajoute un fichier en fournissant son chemin absolu.
     * @param absolutePathOfFile Le chemin complet du fichier à télécharger.
     */
    public void uploadTheFile(String absolutePathOfFile) {
        driver.findElement(inputFile).sendKeys(absolutePathOfFile);
    }

    public Boolean clickValidBTN (){
        driver.findElement(validBTN).click();
        return driver.findElement(validBTN).isDisplayed();
    }

}
