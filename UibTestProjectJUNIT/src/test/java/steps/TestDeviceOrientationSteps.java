
package steps;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

import io.cucumber.java.en.When;

import pages.AcceuilPage;


import java.io.File;
import java.io.IOException;

import static hooks.Hooks.loginPage;
import static org.junit.Assert.assertTrue;

public class TestDeviceOrientationSteps {

    private AcceuilPage acceuilPage ;



    @Given("l'utilisateur est connecté à l'application MyBusiness YY")
    public void userIsLoggedIn() {
        System.out.println("i m here ");
    }

    @When ("l'utilisateur accède à la page d'accueil YY")
    public void userNavigatesToHomePage(){
        acceuilPage = loginPage.clickDemo();
    }

    @And("la mise en page est correcte sur un écran de bureau")
    public void layoutIsCorrectOnDesktop(){
        boolean desktop = acceuilPage.testResponsiveLayoutOnDesktopScreen();
        assertTrue("La mise en page desktop n'est pas correcte", desktop);
    }

    @And("la mise en page est correcte sur un écran tablette")
    public void layoutIsCorrectOnTablet (){
        boolean tablette = acceuilPage.testResponsiveLayoutOnTabletScreen();
        assertTrue("La mise en page tablette n'est pas correcte", tablette);
    }

    @And("la mise en page est correcte sur un écran mobile")
    public void layoutIsCorrectOnMobile() throws IOException {
        boolean mobile = acceuilPage.testResponsiveLayoutOnMobileScreen();
        assertTrue("La mise en page mobile n'est pas correcte", mobile);

    }







}
