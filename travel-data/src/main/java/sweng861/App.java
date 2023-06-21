package sweng861;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
 * This application creates a user interface to specify filtration parameters
 * that are provided to the Amadeus API get method in order to acquire real-time
 * flight data. This flight data is filtered by the parameters provided by the
 * user and output onto the UI screen for their analysis.
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

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  /**
   * Creates and formats the scrollable grid and all of it's child UI elements.
   * @return
   */
  private Scene createGrid() {
    ScrollPane scroll_pane = new ScrollPane();
    GridPane grid = new GridPane();

    grid.setAlignment(Pos.TOP_LEFT);
    grid.setHgap(50);
    grid.setVgap(10);
    grid.setPadding(new Insets(10, 10, 10, 10));
    // grid.setGridLinesVisible(true);  //Can be uncommented for debugging

    scroll_pane.setContent(grid);
    // Create the scene and title
    Scene scene = new Scene(scroll_pane, 1000, 800);
    Text scene_title = new Text("Enter filter parameter data for flights");
    scene_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(scene_title, 0, 0, 2, 1);

    // Create text fields for string input
    InputTextField departure_location_input =
        new InputTextField(grid, "Departure location:", 0, 1);
    departure_location_input.setPromptText("BOS");
    departure_location_input.setDefaultText("LON");

    InputTextField destination_location_input =
        new InputTextField(grid, "Destination location:", 0, 2);
    destination_location_input.setPromptText("LON");
    destination_location_input.setDefaultText("LAS");

    InputTextField departure_date_input =
        new InputTextField(grid, "Departure date:", 0, 3);
    departure_date_input.setPromptText("YYYY-MM-DD");
    departure_date_input.setDefaultText("2023-06-30");

    InputTextField return_date_input =
        new InputTextField(grid, "Return date:", 0, 4);
    return_date_input.setPromptText("YYYY-MM-DD");
    return_date_input.setDefaultText("2023-07-05");

    InputTextField num_travelers_input =
        new InputTextField(grid, "Number of travelers:", 0, 5);
    num_travelers_input.setPromptText("2");
    num_travelers_input.setDefaultText("1");

    InputTextField min_price_input =
        new InputTextField(grid, "Minimum Price:", 2, 1);
    min_price_input.setPromptText("10");
    min_price_input.setDefaultText("1");

    InputTextField max_price_input =
        new InputTextField(grid, "Maximum Price:", 2, 2);
    max_price_input.setPromptText("2000");
    max_price_input.setDefaultText("2000");

    // Create a title for the output area
    Text results_title = new Text("Results:");
    results_title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
    grid.add(results_title, 0, 9, 2, 1);

    InputTextField desired_num_results_input =
        new InputTextField(grid, "Maximum number of results:", 0, 10);
    desired_num_results_input.setPromptText("10");
    desired_num_results_input.setDefaultText("1");

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
        Integer num_travelers = num_travelers_input.getInteger();
        Integer desired_num_results = desired_num_results_input.getInteger();
        Double min_price = min_price_input.getDouble();
        Double max_price = max_price_input.getDouble();

        Boolean valid_inputs = checkInputParamValidity(
            num_travelers, desired_num_results, min_price, max_price);

        // Query for flight data
        if (valid_inputs) {
          FlightOfferSearch[] query_output = initiateAmadeusQuery(
              departure_location_input.getText(),
              destination_location_input.getText(),
              departure_date_input.getText(), return_date_input.getText(),
              num_travelers, desired_num_results);

          handleAmadeusData(grid, query_output, output_accordion, min_price,
                            max_price, departure_location_input.getText(),
                            destination_location_input.getText());
        }
      }
    });

    return scene;
  }

  /**
   * Parse the relevant data from the Amadeus query output and assign that data
   * to tables within the output accordion
   * @param grid
   * @param query_output
   * @param output_accordion
   * @param min_price
   * @param max_price
   * @param departure_location
   * @param destination_location
   */
  private void handleAmadeusData(GridPane grid,
                                 FlightOfferSearch[] query_output,
                                 OutputAccordion output_accordion,
                                 Double min_price, Double max_price,
                                 String departure_location,
                                 String destination_location) {

    if (query_output == null || query_output.length == 0) {
      postAlert(
          "Did not receive flight data.",
          "Try different parameters and ensure all inputs match their initial prompt formats.");
      return;
    }
    // // Can be uncommented to debug the Amadeus query
    // for (FlightOfferSearch item : query_output) {
    //   System.out.println(item.getResponse().getBody());
    // }

    // Generate TitledPanes for the accordion of output flight data
    // and add it to the grid
    Boolean pane_created = false;
    for (Integer pane_num = 1; pane_num <= query_output.length; pane_num++) {

      // Obtain the relevant amadeus information that was queried
      FlightData flight_data = new FlightData();
      flight_data.grabFlightData(query_output[pane_num - 1]);

      // Create accordion panes with flight data and filter out by
      // pricing info
      if (flight_data.getFlight_price() >= min_price &&
          flight_data.getFlight_price() <= max_price) {
        output_accordion.addPane(departure_location, destination_location,
                                 flight_data);
        pane_created = true;

        VBox vbox_accordion = new VBox(output_accordion.getAccordion());
        GridPane.setColumnSpan(vbox_accordion, 10);
        grid.add(vbox_accordion, 0, 12);
      }
    }

    if (!pane_created) {
      if (min_price.equals(max_price)) {

        postAlert(
            "No flights available for $" + min_price,
            "Consider widening your price range or trying a different price.");
      } else {
        postAlert(
            "No flights displayed - All flights above max price.",
            "Flight data was received but all flights are above the maximum price. Consider increasing the parameter.");
      }
    }
  }

  /**
   * This method executes the Amadeus API get() query with all of the user-input
   * parameters of filtration.
   * @param origin_location_code
   * @param destination_location_code
   * @param departure_date
   * @param return_date
   * @param number_of_adults
   * @param number_of_results
   * @return
   */
  private FlightOfferSearch[] initiateAmadeusQuery(
      String origin_location_code, String destination_location_code,
      String departure_date, String return_date, Integer number_of_adults,
      Integer number_of_results) {

    Amadeus amadeus =
        Amadeus.builder("RjiHomRus5b6TGHtg78pmcYo6sf56cCR", "kYg46eokrVTc8CBI")
            .build();

    // Flight Offers price
    FlightOfferSearch[] flightOffersSearches = null;
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

    if (flightOffersSearches != null) {
      System.out.println("Successfully received amadeus query data.");
    }
    return flightOffersSearches;
  }

  /**
   * Post an alert to the user
   * @param header
   * @param content
   */
  void postAlert(String header, String content) {
    Alert errorAlert = new Alert(AlertType.ERROR);
    errorAlert.setHeaderText(header);
    errorAlert.setContentText(content);
    errorAlert.showAndWait();
  }

  /**
   * Takes the parameters input by the user and checks their validity. Alerts
   * the user if any are not valid.
   * @param num_travelers
   * @param desired_num_results
   * @param min_price
   * @param max_price
   * @return
   */
  Boolean checkInputParamValidity(Integer num_travelers,
                                  Integer desired_num_results, Double min_price,
                                  Double max_price) {

    Boolean inputs_valid = true;

    if (max_price <= 0) {
      inputs_valid = false;
      postAlert("Invalid maximum price.",
                "Ensure maximum price is greater than 0.");
    }

    if (min_price < 0) {
      inputs_valid = false;
      postAlert("Invalid minimum price.",
                "Ensure minimum price is non-negative.");
    }

    if (desired_num_results <= 0) {
      inputs_valid = false;
      postAlert("Invalid maximum number of results.",
                "Ensure maximum number of results is greater than 0.");
    }

    if (num_travelers <= 0) {
      inputs_valid = false;
      postAlert("Invalid number of travelers.",
                "Ensure number of travelers is greater than 0.");
    }

    if (min_price > max_price) {
      inputs_valid = false;
      postAlert("Minimum price greater than maximum price.",
                "Ensure the minimum price is less than the maximum price.");
    }

    return inputs_valid;
  }

  public static void main(String[] args) throws ResponseException {
    launch(); // launch GUI
  }
}