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
import javafx.scene.layout.VBox;
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
    grid.setAlignment(Pos.TOP_LEFT);
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));

    // Create the scene and title
    Scene scene = new Scene(grid, 800, 800);
    Text scenetitle = new Text("Enter filter parameter data for flights");
    scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scenetitle, 0, 0, 2, 1);

    // Create text fields for string input
    InputTextField departure_location_input =
        new InputTextField(grid, "Departure location:", 0, 1);
    departure_location_input.setPromptText("Boston");

    InputTextField destination_location_input =
        new InputTextField(grid, "Destination location:", 0, 2);
    destination_location_input.setPromptText("London");

    InputTextField departure_date_input =
        new InputTextField(grid, "Departure date:", 0, 3);
    departure_date_input.setPromptText("YYYY-MM-DD");

    InputTextField return_date_input =
        new InputTextField(grid, "Return date:", 0, 4);
    return_date_input.setPromptText("YYYY-MM-DD");

    InputTextField num_adults_traveling_input =
        new InputTextField(grid, "Number of adults traveling:", 0, 5);
    num_adults_traveling_input.setPromptText("2");

    InputTextField desired_num_results_input =
        new InputTextField(grid, "Desired number of results:", 0, 6);
    desired_num_results_input.setDefaultText("3");

    // Create button to initiate query for filtered travel data
    Button btn = new Button("Calculate flight data");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.TOP_RIGHT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 6, 0);

    OutputAccordion output_accordion = new OutputAccordion(grid);
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // TODO: Initiate amadeus query and json parsing

        // Clear all previous flight data output
        output_accordion.removeAllPanes();

        // Obtain the relevant amadeus information that was queried
        FlightData flight_data = new FlightData();
        flight_data.origin_location = departure_location_input.getText();
        flight_data.destination_location = destination_location_input.getText();
        flight_data.departure_date = departure_date_input.getText();
        flight_data.return_date = return_date_input.getText();
        try {
          flight_data.number_of_adults =
              Integer.parseInt(num_adults_traveling_input.getText());
        } catch (NumberFormatException nfe) {
          System.out.println(
              "Invalid integer input for Number of adults traveling: " +
              num_adults_traveling_input.getText());
        }

        // Generate TitledPanes for the accordion of output flight data and add
        // it to the grid
        for (Integer pane_num = 1;
             pane_num <= Integer.parseInt(desired_num_results_input.getText());
             pane_num++) {
          output_accordion.addPane("Flight " + pane_num, flight_data);
        }
        VBox vbox_accordion = new VBox(output_accordion.getAccordion());
        GridPane.setColumnSpan(vbox_accordion, GridPane.REMAINING);
        grid.add(vbox_accordion, 0, 12);
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