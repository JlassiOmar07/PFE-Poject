package runners;


import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/features" }, 
plugin = { "pretty",
		"html:target/cucmber-report.html",
		"json:target/cucumber-report.json" },
		glue = {"steps" , "hooks"},
		monochrome = true, snippets = CAMELCASE

)

public class CucumberRunner  {}



