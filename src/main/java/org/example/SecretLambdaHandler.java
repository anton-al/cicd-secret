package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SecretLambdaHandler implements RequestHandler<Object, Object> {
    private static final Logger logger = LoggerFactory.getLogger(SecretLambdaHandler.class);

    @Override
    public Object handleRequest(Object input, Context context) {
        String secretName = System.getenv("SECRET_NAME");
        String sessionToken = System.getenv("AWS_SESSION_TOKEN");

        try {
            // Get the secret with Secrets Lambda Extension
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:2773/secretsmanager/get?secretId="
                            + URLEncoder.encode(secretName, StandardCharsets.UTF_8)))
                    .header("X-Aws-Parameters-Secrets-Token", sessionToken)
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String secretString = response.body();


            // Get the secret with AWS SDK 2
//            SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder().build();
//            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//                    .secretId(secretName)
//                    .build();
//
//            GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
//            String secretString = secretValueResponse.secretString();

            logger.info("Username and password successfully retrieved.");
            logger.info(secretString);

        } catch (Exception e) {
            logger.error("Error accessing secret: " + e.getMessage());
        }

        return null;
    }
}