package edu.psgv.sweng861;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import client.send;
/**
 * TODO: Summary of function of this project
 *
 * @author Kostis Zarvis
 *
 */ public class FlightDataProject {
  /**
   * main() accepts commmand line parameters.
   */

  public static void main(String[] args) {

    // Acquiring API access token
    String api_key = "RjiHomRus5b6TGHtg78pmcYo6sf56cCR";
    String api_secret = "kYg46eokrVTc8CBI";
    String authorization_endpoint =
        "https://test.api.amadeus.com/v1/security/oauth2/token";
    String authorization_body = "grant_type=client_credentials&client_id={" +
                                api_key + "}&client_secret={" + api_secret +
                                "}";

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create(authorization_endpoint))
            .POST(HttpRequest.BodyPublishers.ofString(authorization_body))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build();

    HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());

    // System.out.println(response.toString());
  }
}