package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ControllerFereastraEditareUnitate {

    @FXML
    private TextField textField;
    @FXML
    private Label label;

    public void initialize() {
        Platform.runLater(() -> textField.requestFocus());

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    label.setText("Modificati numele unitatii de invatamant");
                    label.setTextFill(Color.BLACK);
                }
            }
        });
    }

    public UnitateDeInvatamant procesare() {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            label.setText("Introduceti noul nume");
            label.setTextFill(Color.RED);
            return null;
        }
        return new UnitateDeInvatamant(text);
    }
}