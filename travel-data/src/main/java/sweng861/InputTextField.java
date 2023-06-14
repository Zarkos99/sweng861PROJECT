package sweng861;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Creates and handles data input for a TextField with an associated label.
 */
class InputTextField {
  private String input_text;
  private GridPane grid;
  private String name;
  private Integer grid_column;
  private Integer grid_row;
  private TextField text_field;

  /**
   * Creates a new instance of an InputTextField object.
   * @param grid_pane
   * @param label_name
   * @param column
   * @param row
   */
  public InputTextField(GridPane grid_pane, String label_name, Integer column,
                        Integer row) {

    grid = grid_pane;
    name = label_name;
    grid_column = column;
    grid_row = row;

    createLabel();
    createTextField();
    handleTextInput();
  }

  /**
   * This method creates a label to associate the TextField with what the user
   * should input into it.
   */
  private void createLabel() {
    Label label = new Label(name);
    grid.add(label, grid_column, grid_row);
  }

  /**
   * This method creates the TextField one column to the right of the label.
   */
  private void createTextField() {
    text_field = new TextField("");
    grid.add(text_field, grid_column + 1, grid_row);
  }

  /**
   * This method handles all input into the TextField by saving it into the
   * input_text member variable.
   */
  private void handleTextInput() {
    text_field.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent e) {
        input_text = text_field.getText();
      }
    });
  }

  /**
   * This method sets text to give the user a prompt of what they should input.
   * @param text
   */
  public void setPromptText(String text) { text_field.setPromptText(text); }

  /**
   * This method sets the default value for the TextField.
   * @param text
   */
  public void setDefaultText(String text) {
    text_field.setText(text);
    input_text = text;
  }

  /**
   * This method gets the text that was input in the TextField by the user.
   * @return
   */
  public String getText() { return input_text; }

  /**
   * This method gets the text that was input in the TextField by the user,
   * translates it to an integer, and posts an alert if that translation failed
   * due to invalid user input.
   * @return
   */
  public Integer getInteger() {
    try {
      return Integer.parseInt(input_text);
    } catch (NumberFormatException nfe) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Invalid integer input for " + name + " " +
                               input_text);
      errorAlert.setContentText(
          "Ensure the input is in a proper integer format.");
      errorAlert.showAndWait();
      return -1;
    }
  }

  /**
   * This method gets the text that was input in the TextField by the user,
   * translates it to a double, and posts an alert if that translation failed
   * due to invalid user input.
   * @return
   */
  public Double getDouble() {
    try {
      return Double.parseDouble(input_text);
    } catch (NumberFormatException nfe) {
      Alert errorAlert = new Alert(AlertType.ERROR);
      errorAlert.setHeaderText("Invalid double input for " + name + " " +
                               input_text);
      errorAlert.setContentText(
          "Ensure the input is in a proper double format.");
      errorAlert.showAndWait();
      return -1.0;
    }
  }
}
;
