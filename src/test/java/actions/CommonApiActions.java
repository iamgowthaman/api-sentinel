package actions;

import db.ApiExecutionRepository;
import resources.APIResources;
import utils.AllureLogger;
import utils.SpecBuilders;
import utils.TestContext;

public class CommonApiActions {


    private CommonApiActions() {
        // utility class
    }



    public static void callApi(TestContext context, String apiName, String resource, String method) {
        if (context.getRequest() == null) {
            throw new IllegalStateException(
                    "Request is not initialized. Did you forget to build the request?"
            );
        }
        switch (method.toUpperCase()) {
            case "POST":
                context.setResponse(
                        context.getRequest().when().post(resource)
                );
                break;

            case "GET":
                context.setResponse(
                        context.getRequest().when().get(resource)
                );
                break;

            case "PUT":
                context.setResponse(
                        context.getRequest().when().put(resource)
                );
                break;

            case "DELETE":
                context.setResponse(
                        context.getRequest().when().delete(resource)
                );
                break;

            default:
                throw new IllegalArgumentException("Unsupported method: " + method);
        }

        String status =
                context.getResponse().getStatusCode() >= 200
                        && context.getResponse().getStatusCode() < 300
                        ? "PASS"
                        : "FAIL";
        // ✅ SAVE API RESULT IMMEDIATELY
        ApiExecutionRepository.saveApi(
                context,
                apiName,
                method,
                resource,
                status,
                context.getResponse().time()
        );
    }
}
