package stepDefinitions.user;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import stepDefinitions.BaseStepDefinitions;
import utils.SpecBuilders;
import utils.TestContext;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class AddUserStepDefinitions extends BaseStepDefinitions {


    @Given("Add a new user payload")
    public void add_a_new_user_payload() {
     context.setRequest(given().spec(spec.requestWithToken()));
    }

}
