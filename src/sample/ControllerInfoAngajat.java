package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class ControllerInfoAngajat {

    @FXML
    private BorderPane pane;
    @FXML
    private HBox hbox;
    @FXML
    private HBox hbox1;
    @FXML
    private HBox hbox2;
    @FXML
    private HBox hbox3;
    @FXML
    private HBox hbox4;
    @FXML
    private Button adaugarePlata;
    @FXML
    private Button stergerePlata;
    @FXML
    private Button editarePlata;
    @FXML
    private Button inapoi;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;
    @FXML
    private Label label7;
    @FXML
    private Label label8;
    @FXML
    private TableView<Plata> tabelPlati;
    @FXML
    private TableColumn<Plata, LocalDate> t1;
    @FXML
    private TableColumn<Plata, Double> t2;

    private Angajati angajat = ControllerFereastraPrincipala.getAngajat();
    private UnitateDeInvatamant scoala = ControllerFereastraPrincipala.getScoala();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
    private DecimalFormat df = new DecimalFormat("#.##", otherSymbols);

    public void initialize() {
        adaugarePlata.prefWidthProperty().bind(hbox.widthProperty().divide(3));
        stergerePlata.prefWidthProperty().bind(hbox.widthProperty().divide(3));
        editarePlata.prefWidthProperty().bind(hbox.widthProperty().divide(3));

        label1.prefWidthProperty().bind(hbox1.widthProperty().divide(10).multiply(9));
        inapoi.prefWidthProperty().bind(hbox1.widthProperty().divide(10));

        label2.prefWidthProperty().bind(hbox2.widthProperty().divide(2));
        label3.prefWidthProperty().bind(hbox2.widthProperty().divide(2));

        label4.prefWidthProperty().bind(hbox3.widthProperty().divide(5).multiply(3));
        label5.prefWidthProperty().bind(hbox3.widthProperty().divide(5).multiply(2));

        label6.prefWidthProperty().bind(hbox4.widthProperty().divide(3));
        label7.prefWidthProperty().bind(hbox4.widthProperty().divide(3));
        label8.prefWidthProperty().bind(hbox4.widthProperty().divide(3));

        label1.setText("Unitate de invatamant: " + scoala.getNume());
        label2.setText("Nume: " + angajat.getNume());
        label3.setText("Prenume: " + angajat.getPrenume());
        label4.setText("Decizie: " + angajat.getDecizie());
        label5.setText("Data deciziei: " + String.valueOf(angajat.getDataDeciziei().format(formatter)));
        label6.setText("Suma restanta: " + String.valueOf(df.format(angajat.getSumaRestanta())));
        label7.setText("Restante neachitate: " + String.valueOf(df.format(angajat.getRestanteNeachitate())));
        label8.setText("Dobanda legala: " + String.valueOf(df.format(angajat.getPenalizari())));

        tabelPlati.setItems(angajat.getPlati());

        if (angajat.getRestanteNeachitate() == 0) {
            adaugarePlata.setDisable(true);
        }

        tabelPlati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Plata>() {
            @Override
            public void changed(ObservableValue<? extends Plata> observable, Plata oldValue, Plata newValue) {
                if (newValue != null) {
                    stergerePlata.setDisable(false);
                    editarePlata.setDisable(false);
                }
                if (newValue == null) {
                    stergerePlata.setDisable(true);
                    editarePlata.setDisable(true);
                }
            }
        });

        t1.setCellValueFactory(new PropertyValueFactory<>("dataPlatii"));
        t2.setCellValueFactory(new PropertyValueFactory<>("sumaPlatita"));

        tabelPlati.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        t1.setCellFactory(column -> new TableCell<Plata, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        t2.setCellFactory(column -> new TableCell<Plata, Double>() {
            @Override
            protected void updateItem(Double suma, boolean empty) {
                super.updateItem(suma, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(df.format(suma));
                }
            }
        });

        tabelPlati.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node source = event.getPickResult().getIntersectedNode();
            while (source != null && !(source instanceof TableRow)) {
                source = source.getParent();
            }
            if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty())) {
                if (tabelPlati.getItems() != null) {
                    tabelPlati.getSelectionModel().clearSelection();
                    stergerePlata.setDisable(true);
                    editarePlata.setDisable(true);
                }
            }
        });
    }

    @FXML
    private void inapoi() {
        Parent root = Main.root;
        pane.getScene().setRoot(root);
        VBox vb = (VBox) root.getChildrenUnmodifiable().get(1);
        TableView tv = (TableView) vb.getChildren().get(0);
        tv.refresh();
    }

    @FXML
    private void adaugarePlata() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FereastraAdaugarePlata.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Adaugare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 260);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraAdaugarePlata controller = fxmlLoader.getController();
            Plata newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                for (Plata plata : angajat.getPlati()) {
                    if (newItem.getDataPlatii().equals(plata.getDataPlatii())) {
                        controller.eroareData();
                        event.consume();
                        return;
                    }
                }
                if (newItem.getDataPlatii().isBefore(angajat.getDataDeciziei())) {
                    controller.eroareDataInvalida();
                    event.consume();
                    return;
                }
                boolean temp = angajat.adaugarePlata(newItem);
                if (!temp) {
                    controller.schimbareText();
                    event.consume();
                } else {
                    angajat.getPlati().add(newItem);
                    tabelPlati.setItems(angajat.getPlati());
                    angajat.sortare();
                    angajat.calculPenalitati();
                    label7.setText("Restante neachitate: " + String.valueOf(df.format(angajat.getRestanteNeachitate())));
                    label8.setText("Total penalizari: " + String.valueOf(df.format(angajat.getPenalizari())));
                    if (angajat.getRestanteNeachitate() == 0) {
                        adaugarePlata.setDisable(true);
                    }
                    angajat.savedata();
                    Main.getCtrl().saveData();
                }
            }
        });
        dialog.showAndWait();
    }

    @FXML
    private void stergerePlata() {
        Plata temp = tabelPlati.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(0);
        alert.getButtonTypes().remove(0);
        ButtonType ok = new ButtonType("Stergere", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(ok);
        alert.getDialogPane().getButtonTypes().add(cancel);
        alert.setTitle("Confirmare stergere");
        alert.setHeaderText("Stergeti plata introdusa");
        alert.setContentText("Sunteti sigur? Apasati Stergere pentru confirmare sau Anulare pentru a anula");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            angajat.stergerePlata(temp);
            tabelPlati.getItems().remove(temp);
            angajat.calculPenalitati();
            label7.setText("Restante neachitate: " + String.valueOf(df.format(angajat.getRestanteNeachitate())));
            label8.setText("Total penalizari: " + String.valueOf(df.format(angajat.getPenalizari())));
            if (angajat.getRestanteNeachitate() > 0 && adaugarePlata.isDisabled()) {
                adaugarePlata.setDisable(false);
            }
            angajat.savedata();
            Main.getCtrl().saveData();
        }
        if (tabelPlati.getItems().size() == 0) {
            stergerePlata.setDisable(true);
            editarePlata.setDisable(true);
        }
    }

    @FXML
    private void editarePlata() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(pane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FereastraEditarePlata.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Modificare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 260);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraEditarePlata controller = fxmlLoader.getController();
            Plata newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                if (newItem.getDataPlatii() != null) {
                    for (Plata plata : angajat.getPlati()) {
                        if ((newItem.getDataPlatii().equals(plata.getDataPlatii())) && (!newItem.getDataPlatii().equals(tabelPlati.getSelectionModel().getSelectedItem().getDataPlatii()))) {
                            controller.eroareData();
                            event.consume();
                            return;
                        }
                    }
                    if (newItem.getDataPlatii().isBefore(angajat.getDataDeciziei())) {
                        controller.eroareDataInvalida();
                        event.consume();
                        return;
                    }
                }
                if ((newItem.getSumaPlatita() != 0) && (newItem.getSumaPlatita() != tabelPlati.getSelectionModel().getSelectedItem().getSumaPlatita())) {
                    boolean temp = angajat.editarePlata(tabelPlati.getSelectionModel().getSelectedItem(), newItem);
                    if (!temp) {
                        newItem.setDataPlatii(null);
                        controller.schimbareText();
                        event.consume();
                    } else {
                        tabelPlati.getSelectionModel().getSelectedItem().setSumaPlatita(newItem.getSumaPlatita());
                        if (angajat.getRestanteNeachitate() == 0) {
                            adaugarePlata.setDisable(true);
                        }
                        if (angajat.getRestanteNeachitate() > 0 && adaugarePlata.isDisabled()) {
                            adaugarePlata.setDisable(false);
                        }
                    }
                }
                if ((newItem.getDataPlatii() != null) && (!newItem.getDataPlatii().equals(tabelPlati.getSelectionModel().getSelectedItem().getDataPlatii()))) {
                    tabelPlati.getSelectionModel().getSelectedItem().setDataPlatii(newItem.getDataPlatii());
                }
                angajat.sortare();
                angajat.calculPenalitati();
                label7.setText("Restante neachitate: " + String.valueOf(df.format(angajat.getRestanteNeachitate())));
                label8.setText("Total penalizari: " + String.valueOf(df.format(angajat.getPenalizari())));
                tabelPlati.refresh();
                angajat.savedata();
                Main.getCtrl().saveData();
            }
        });
        dialog.showAndWait();
    }
}
