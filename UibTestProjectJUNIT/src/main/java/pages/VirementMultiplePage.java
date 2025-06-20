package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class VirementMultiplePage {
    private WebDriver driver ;
    public VirementMultiplePage(WebDriver driver){
        this.driver = driver ;
    }

    private By account = By.xpath("//select[@class=\"formControls\"][contains(.,\"Compte Adria: 3XXXXXXXX12 ,Solde  1 220,42 TND\")]");
    private By beneficiary = By.xpath("//option[@value=\"87903\"]");
    private By addBenefBtn = By.xpath("//button[@class=\"addBenefBtn btn btn-default\"]");
    private By valideBtn = By.xpath("//button[@class=\"btnvalide btn btn-default\"][contains(.,\"Valider\")]");

    private By montant = By.xpath("//input[@type='text' and @name='montant']");

    private  By btn = By.xpath("//button[@class=\"btn btn-xs pull-right btn btn-default\"]");


    /**
     * Récupère le menu déroulant des comptes sous forme d'un objet Select.
     * @return Un objet Select représentant le menu déroulant des comptes .
     */
    private Select findAccountDropDown(){
        return new Select(driver.findElement(account));
    }

    /**
     * Sélectionne un compte dans la liste déroulante par son texte visible.
     * @param compte Le compte à sélectionner.
     */
    public void selectFromDebitAccount(String compte) {
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

   public void setBeneficiary(){
        driver.findElement(beneficiary).click();
        driver.findElement(addBenefBtn).click();
   }


    public void setMontant(){
        driver.findElement(montant).sendKeys("500");
        driver.findElement(btn).click();
    }

    public void clickValidButton (){
        driver.findElement(valideBtn).click();
    }


}
