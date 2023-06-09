package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class ControllerFereastraAdaugarePlata {

    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private Label label;

    public void initialize() {
        Platform.runLater(() -> textField1.requestFocus());

        textField1.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    label.setText("Introduceti plata");
                    label.setTextFill(Color.BLACK);
                }
            }
        });
        textField2.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    label.setText("Introduceti plata");
                    label.setTextFill(Color.BLACK);
                }
            }
        });
    }

    public Plata procesare() {
        String text1 = textField1.getText().trim();
        String text2 = textField2.getText().trim();
        if (text1.isEmpty() || text2.isEmpty()) {
            label.setText("Introduceti toate informatiile cerute");
            label.setTextFill(Color.RED);
            return null;
        }
        double valoare = 0;
        LocalDate data = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        try {
            data = LocalDate.parse(text1, formatter);
            valoare = Double.parseDouble(text2);

        } catch (NumberFormatException e) {
            valoare = -1989.0203;
        } catch (DateTimeParseException e) {
            data = null;
        }
        if (data == null) {
            label.setText("Introduceti o data vailda");
            label.setTextFill(Color.RED);
            textField1.clear();
            return null;
        }
        if (data.getYear() < 2005 || data.getYear() > 2020) {
            label.setText("Introduceti o data din intervalul \n     01/01/2005 - 31/12/2020");
            label.setTextFill(Color.RED);
            textField1.clear();
            return null;
        }
        if (valoare <= 0) {
            label.setText("Introduceti o suma vailda");
            label.setTextFill(Color.RED);
            textField2.clear();
            return null;
        }
        return new Plata(valoare, data);
    }

    public void schimbareText() {
        label.setText("Totalul platilor introduse depaseste suma restanta");
        label.setTextFill(Color.RED);
        textField2.clear();
    }

    public void eroareData() {
        label.setText("O plata a fost deja inregistrata pe aceasta data");
        label.setTextFill(Color.RED);
        textField1.clear();
    }

    public void eroareDataInvalida() {
        label.setText("Introduceti o data care succeda data deciziei");
        label.setTextFill(Color.RED);
        textField1.clear();
    }
}
