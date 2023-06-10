package sweng861;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

class InputTextField {
  private String input_text;
  private GridPane grid;
  private String name;
  private Integer grid_column;
  private Integer grid_row;
  private TextField text_field;

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

  private void createLabel() {
    Label label = new Label(name);
    grid.add(label, grid_column, grid_row);
  }

  private void createTextField() {
    text_field = new TextField("");
    grid.add(text_field, grid_column + 1, grid_row);
  }

  private void handleTextInput() {
    text_field.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent e) {
        input_text = text_field.getText();
      }
    });
  }

  public void setPromptText(String text) { text_field.setPromptText(text); }
  public void setDefaultText(String text) { text_field.setText(text); }
  public String getText() { return input_text; }
}
;
