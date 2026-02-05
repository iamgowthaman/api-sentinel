package stepDefinitions;

import utils.SpecBuilders;
import utils.TestContext;

public abstract class BaseStepDefinitions {

    protected TestContext context;
    protected SpecBuilders spec;

    protected BaseStepDefinitions() {
        this.context = TestContext.get();
        this.spec = context.getObjectManager().getSpecBuilders();
    }
}
