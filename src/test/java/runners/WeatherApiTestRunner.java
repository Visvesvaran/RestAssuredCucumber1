package runners;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

 

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "Feature"		
		,glue={"stepDefinition"}
		,plugin = { "pretty",
				  	"html:target/cucumber-htmlreport"
					}
		
		)
public class WeatherApiTestRunner {

}
