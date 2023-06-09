package sweng861;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

class InputTextField {
  private String input_text;
  private GridPane grid;
  private String name;
  private Integer grid_column;
  private Integer grid_row;

  public InputTextField(GridPane grid_pane, String label_name, Integer column,
                        Integer row) {

    grid = grid_pane;
    name = label_name;
    grid_column = column;
    grid_row = row;

    createLabel();
    TextField text_field = createTextField();
    handleTextInput(text_field);
  }

  private void createLabel() {
    Label label = new Label(name);
    grid.add(label, grid_column, grid_row);
  }

  private TextField createTextField() {
    TextField text_field = new TextField();
    grid.add(text_field, grid_column + 1, grid_row);
    return text_field;
  }

  private void handleTextInput(TextField text_field) {
    text_field.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        input_text = text_field.getText();
      }
    });
  }

  public String getInputText() { return input_text; }
}
;
