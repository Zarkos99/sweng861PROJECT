package sweng861;

import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class OutputAccordion {
  private final Integer MAX_HEIGHT_FOR_ONE_TABLE_CELL = 61;
  private Accordion accordion;
  private GridPane grid;

  public OutputAccordion(GridPane grid_pane) {
    grid = grid_pane;
    accordion = new Accordion();
    accordion.setMaxWidth(2000);
  }

  public void addPane(String title, FlightData flight_data) {
    TableView table = createFlightDataTable(flight_data);
    TitledPane pane = new TitledPane(title, table);
    accordion.getPanes().add(pane);
  }

  private TableView createFlightDataTable(FlightData flight_data) {
    TableView table = new TableView();
    table.setMaxHeight(MAX_HEIGHT_FOR_ONE_TABLE_CELL);

    // Create columns of flight data
    createTableColumn(table, "Departure Location", "OriginLocation");
    createTableColumn(table, "Destination Location", "DestinationLocation");
    createTableColumn(table, "Departure Date", "DepartureDate");
    createTableColumn(table, "Return Date", "ReturnDate");
    createTableColumn(table, "Number Of Adults", "NumberOfAdults");

    table.getItems().add(flight_data);

    return table;
  }

  private TableColumn<FlightData, String>
  createTableColumn(TableView table, String column_name,
                    String cell_value_name) {
    TableColumn<FlightData, String> column = new TableColumn<>(column_name);
    column.setCellValueFactory(new PropertyValueFactory<>(cell_value_name));
    table.getColumns().add(column);
    return column;
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
