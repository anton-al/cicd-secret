package org.example;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

public class SecretLambdaHandler implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder().build();
        String secretName = System.getenv("SECRET_NAME");

        try {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId(secretName)
                    .build();

            GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
            String secretString = secretValueResponse.secretString();

            System.out.println("Username and password successfully retrieved.");
            System.out.println(secretString);

        } catch (SecretsManagerException e) {
            System.err.println("Error accessing secret: " + e.getMessage());
        }

        return null;
    }
}