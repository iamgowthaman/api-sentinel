package hooks;

import db.ScenarioRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.TestContext;

import java.util.UUID;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {

        TestContext context = TestContext.get();
        context.setScenarioId(UUID.randomUUID());

        context.setScenarioName(scenario.getName());
        scenario.getId();
        System.err.println("Starting Scenario: " + scenario.getName());
        context.setStartTime(System.currentTimeMillis());

        String feature =
                scenario.getUri().getPath()
                        .substring(scenario.getUri().getPath().lastIndexOf("/") + 1)
                        .replace(".feature", "");

        context.setFeatureName(feature);
        System.err.println("Feature: " + feature);
        ScenarioRepository.insertScenario(context);
    }


}
