package sweng861;

import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * This class creates an accordion of formatted flight data to display to the
 * user.
 */
class OutputAccordion {
  private final Integer MAX_HEIGHT_FOR_ONE_TABLE_CELL = 100;
  private Accordion accordion;
  private GridPane grid;

  public OutputAccordion(GridPane grid_pane) {
    grid = grid_pane;
    accordion = new Accordion();
    accordion.setMaxWidth(2000);
  }

  public void addPane(String departure_location, String destination_location,
                      FlightData flight_data) {
    String title = "Flight " + flight_data.getFlight_id() + ":  " +
                   departure_location + "(" + flight_data.getOrigin_location() +
                   ") to " + destination_location + "(" +
                   flight_data.getDestination_location() + ") Total Price: $" +
                   flight_data.getFlight_price();

    TableView table = createFlightDataTable(flight_data);
    TitledPane pane = new TitledPane(title, table);
    accordion.getPanes().add(pane);
  }

  private TableView createFlightDataTable(FlightData flight_data) {
    TableView table = new TableView();
    table.setMaxHeight(MAX_HEIGHT_FOR_ONE_TABLE_CELL);

    // Create columns of flight data using PropertyValueFactories based on the
    // getters in FlightData.java

    createTableColumn(table, "Departure Location", "Departure_location");
    createTableColumn(table, "Arrival Location", "Arrival_location");
    createTableColumn(table, "Departure Date", "Departure_date");
    createTableColumn(table, "Arrival Date", "Arrival_date");
    createTableColumn(table, "Duration", "Duration");
    createTableColumn(table, "Number Of Bookable Seats",
                      "Number_of_bookable_seats");
    createTableColumn(table, "Airline", "Airline_code");

    for (FlightSegmentData segment : flight_data.getFlight_segments()) {
      table.getItems().add(segment);
    }
    return table;
  }

  private void createTableColumn(TableView table, String column_name,
                                 String cell_value_name) {
    TableColumn<FlightSegmentData, String> column =
        new TableColumn<FlightSegmentData, String>(column_name);
    column.setStyle("-fx-alignment: CENTER;");
    column.setCellValueFactory(new PropertyValueFactory<>(cell_value_name));
    table.getColumns().add(column);
  }

  public void removeAllPanes() {
    ObservableList<TitledPane> pane_list = accordion.getPanes();
    pane_list.remove(0, pane_list.size());
    VBox vBox = new VBox(accordion);
    grid.add(vBox, 0, 10);
  }

  public Accordion getAccordion() { return accordion; }
}
;
