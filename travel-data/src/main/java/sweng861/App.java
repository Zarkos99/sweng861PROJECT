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

  public static void main(String[] args) throws ResponseException {

    Amadeus amadeus =
        Amadeus.builder("RjiHomRus5b6TGHtg78pmcYo6sf56cCR", "kYg46eokrVTc8CBI")
            .build();

    Location[] locations = amadeus.referenceData.locations.get(
        Params.with("keyword", "LON").and("subType", Locations.ANY));

    for (Location location : locations) {
      System.out.println(location.getResponse().getBody());
    }

    // launch();    //launch GUI
  }
}