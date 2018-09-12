package com.klog.connection;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.google.gson.Gson;
import com.klog.exception.InternalServerErrorException;
import com.klog.model.aws.DBSecret;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnection {

    /**
     * Connect to the MySQL database
     *
     * @return a Connection object
     */
    static public Connection connect() {

        try {
            DBSecret dbSecret = getSecret();
            if (dbSecret == null) {
                throw new InternalServerErrorException("Get secrets failed");
            } else {
                String url = dbSecret.getHost();
                String user = dbSecret.getUsername();
                String password = dbSecret.getPassword();

                return DriverManager.getConnection(url, user, password);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    static private DBSecret getSecret() {

        String secretName = "Mysql";
        String endpoint = "secretsmanager.us-east-2.amazonaws.com";
        String region = "us-east-2";

        AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        AWSSecretsManager client = clientBuilder.build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;
        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);

        } catch (ResourceNotFoundException e) {
            System.out.println("The requested secret " + secretName + " was not found");
        } catch (InvalidRequestException e) {
            System.out.println("The request was invalid due to: " + e.getMessage());
        } catch (InvalidParameterException e) {
            System.out.println("The request had invalid params: " + e.getMessage());
        }

        if (getSecretValueResult == null) {
            return null;
        }

        if (getSecretValueResult.getSecretString() != null) {
            Gson gson = new Gson();
            return gson.fromJson(getSecretValueResult.getSecretString(), DBSecret.class);
        } else {
            return null;
        }
    }

}

