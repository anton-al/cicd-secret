package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

public class SecretLambdaHandler implements RequestHandler<Object, Object> {
    private static final Logger logger = LoggerFactory.getLogger(SecretLambdaHandler.class);

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

            logger.info("Username and password successfully retrieved.");
            logger.info(secretString);

        } catch (SecretsManagerException e) {
            logger.error("Error accessing secret: " + e.getMessage());
        }

        return null;
    }
}