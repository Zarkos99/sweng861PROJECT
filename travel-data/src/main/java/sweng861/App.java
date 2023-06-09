package sweng861;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
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
    Scene scene = initializeGrid();
    stage.setScene(scene);
    stage.setTitle("FlightInfo");
    stage.show();
  }

  private Scene initializeGrid() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(25, 25, 25, 25));

    // Create the scene and title
    Scene scene = new Scene(grid, 800, 800);
    Text scenetitle = new Text("Enter filter parameter data for flights");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);

    // Create text fields for string input
    InputTextField input_text_field =
        new InputTextField(grid, "Departure location:", 0, 1);

    // Create button to initiate query for filtered travel data
    Button btn = new Button("Calculate");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 1, 4);

    final Text actiontarget = new Text();
    grid.add(actiontarget, 1, 6);
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        System.out.println(input_text_field.getInputText());
      }
    });

    return scene;
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

    // Amadeus amadeus =
    //     Amadeus.builder("RjiHomRus5b6TGHtg78pmcYo6sf56cCR",
    //     "kYg46eokrVTc8CBI")
    //         .build();

    // // Query parameters
    // String origin_location_code = "BOS";
    // String destination_location_code = "LON";
    // String departure_date = "2023-06-11";
    // String return_date = "2023-06-14";
    // Integer number_of_adults = 1;
    // Integer number_of_results = 4;

    // // Flight Offers price
    // FlightOfferSearch[] flightOffersSearches =
    //     amadeus.shopping.flightOffersSearch.get(
    //         Params.with("originLocationCode", origin_location_code)
    //             .and("destinationLocationCode", destination_location_code)
    //             .and("departureDate", departure_date)
    //             .and("returnDate", return_date)
    //             .and("adults", number_of_adults)
    //             .and("max", number_of_results));

    // for (FlightOfferSearch location : flightOffersSearches) {
    //   System.out.println(location.getResponse().getBody());
    // }
    launch(); // launch GUI
  }
}