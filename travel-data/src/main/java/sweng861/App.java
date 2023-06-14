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
import com.amadeus.resources.FlightOfferSearch;

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
    Scene scene = createGrid();
    stage.setScene(scene);
    stage.setTitle("FlightInfo");
    stage.show();
  }

  private Scene createGrid() {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.TOP_LEFT);
    grid.setHgap(50);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    // grid.setGridLinesVisible(true);  //Can be uncommented for debugging

    // Create the scene and title
    Scene scene = new Scene(grid, 800, 800);
    Text scene_title = new Text("Enter filter parameter data for flights");
    scene_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scene_title, 0, 0, 2, 1);

    // Create text fields for string input
    InputTextField departure_location_input =
        new InputTextField(grid, "Departure location:", 0, 1);
    departure_location_input.setPromptText("BOS");
    departure_location_input.setDefaultText("BOS");

    InputTextField destination_location_input =
        new InputTextField(grid, "Destination location:", 0, 2);
    destination_location_input.setPromptText("LON");
    destination_location_input.setDefaultText("LON");

    InputTextField departure_date_input =
        new InputTextField(grid, "Departure date:", 0, 3);
    departure_date_input.setPromptText("YYYY-MM-DD");
    departure_date_input.setDefaultText("2023-06-15");

    InputTextField return_date_input =
        new InputTextField(grid, "Return date:", 0, 4);
    return_date_input.setPromptText("YYYY-MM-DD");
    return_date_input.setDefaultText("2023-06-20");

    InputTextField num_adults_traveling_input =
        new InputTextField(grid, "Number of adults traveling:", 0, 5);
    num_adults_traveling_input.setPromptText("2");
    num_adults_traveling_input.setDefaultText("1");

    // Create a title for the output area
    Text results_title = new Text("Results:");
    results_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(results_title, 0, 9, 2, 1);

    InputTextField desired_num_results_input =
        new InputTextField(grid, "Desired number of results:", 0, 10);
    desired_num_results_input.setDefaultText("3");

    // Create button to initiate query for filtered travel data
    Button btn = new Button("Find flights");
    HBox hbBtn = new HBox(10);
    hbBtn.setAlignment(Pos.TOP_LEFT);
    hbBtn.getChildren().add(btn);
    grid.add(hbBtn, 1, 9);

    // Handle the pressing of the "Find flights" button by initiating amadeus
    // query and organizing data into an accordion with a TitledPane per flight
    // each containing a table of info
    OutputAccordion output_accordion = new OutputAccordion(grid);
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        // Clear all previous flight data output
        output_accordion.removeAllPanes();

        // Query for flight data
        FlightOfferSearch[] query_output = initiateAmadeusQuery(
            departure_location_input.getText(),
            destination_location_input.getText(),
            departure_date_input.getText(), return_date_input.getText(),
            num_adults_traveling_input.getText(),
            desired_num_results_input.getText());

        /* // Can be uncommented to debug the Amadeus query
        for (FlightOfferSearch item : query_output) {
          System.out.println(item.getResponse().getBody());
        }
        */

        // Generate TitledPanes for the accordion of output flight data
        // and add it to the grid
        for (Integer pane_num = 1;
             pane_num <= Integer.parseInt(desired_num_results_input.getText());
             pane_num++) {

          // Obtain the relevant amadeus information that was queried
          FlightData flight_data = new FlightData();
          flight_data.grabFlightData(query_output[pane_num - 1]);
          // Create accordion panes with flight data
          String pane_title = "Flight " + pane_num + ":  " +
                              flight_data.getOriginLocation() + " to " +
                              flight_data.getDestinationLocation();
          output_accordion.addPane(pane_title, flight_data);
        }

        VBox vbox_accordion = new VBox(output_accordion.getAccordion());
        GridPane.setColumnSpan(vbox_accordion, 10);
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

  private FlightOfferSearch[] initiateAmadeusQuery(
      String origin_location_code, String destination_location_code,
      String departure_date, String return_date, String number_of_adults_input,
      String number_of_results_input) {

    Boolean valid_inputs = true;

    Integer number_of_adults = 0;
    try {
      number_of_adults = Integer.parseInt(number_of_adults_input);
    } catch (NumberFormatException nfe) {
      valid_inputs = false;
      System.out.println(
          "Invalid integer input for number of adults traveling: " +
          number_of_adults_input);
    }

    Integer number_of_results = 0;
    try {
      number_of_results = Integer.parseInt(number_of_results_input);
    } catch (NumberFormatException nfe) {
      valid_inputs = false;
      System.out.println("Invalid integer input for number of results: " +
                         number_of_results_input);
    }

    Amadeus amadeus =
        Amadeus.builder("RjiHomRus5b6TGHtg78pmcYo6sf56cCR", "kYg46eokrVTc8CBI")
            .build();

    // Flight Offers price
    FlightOfferSearch[] flightOffersSearches = null;
    if (valid_inputs) {
      try {
        flightOffersSearches = amadeus.shopping.flightOffersSearch.get(
            Params.with("originLocationCode", origin_location_code)
                .and("destinationLocationCode", destination_location_code)
                .and("departureDate", departure_date)
                .and("returnDate", return_date)
                .and("adults", number_of_adults)
                .and("max", number_of_results));
      } catch (ResponseException exception) {
        System.out.println("Received response exception");
      }
    }
    return flightOffersSearches;
  }
  public static void main(String[] args) throws ResponseException {

    launch(); // launch GUI
  }
}