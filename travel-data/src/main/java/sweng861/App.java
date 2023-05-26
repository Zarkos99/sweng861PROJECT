package sweng861;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.Location;

/**
 * JavaFX App
 * TODO: Summary of function of this project
 *
 * @author Kostis Zarvis
 *
 */
public class App extends Application {

  private static Scene scene;

  @Override
  public void start(Stage stage) throws IOException {
    scene = new Scene(loadFXML("primary"), 640, 480);
    stage.setScene(scene);
    stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {
    launch();

    // Acquiring API access token
    // String api_key = "RjiHomRus5b6TGHtg78pmcYo6sf56cCR";
    // String api_secret = "kYg46eokrVTc8CBI";
    // String authorization_endpoint =
    //     "https://test.api.amadeus.com/v1/security/oauth2/token";
    // String authorization_body = "grant_type=client_credentials&client_id={" +
    //                             api_key + "}&client_secret={" + api_secret +
    //                             "}";

    // HttpClient client = HttpClient.newHttpClient();
    // HttpRequest request =
    //     HttpRequest.newBuilder()
    //         .uri(URI.create(authorization_endpoint))
    //         .POST(HttpRequest.BodyPublishers.ofString(authorization_body))
    //         .header("Content-Type", "application/x-www-form-urlencoded")
    //         .build();

    // HttpResponse<String> response =
    //     client.send(request, HttpResponse.BodyHandlers.ofString());

    // System.out.println(response.toString());
  }
}