package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public class ControllerFereastraPrincipala {

    @FXML
    private HBox hbox;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ListView<UnitateDeInvatamant> listaUnitati;
    @FXML
    private TableView<Angajati> tabelAngajati;
    @FXML
    private Button stergereUnitate;
    @FXML
    private Button editareUnitate;
    @FXML
    private Button adaugareAngajat;
    @FXML
    private Button stergereAngajat;
    @FXML
    private Button editareAngajat;
    @FXML
    private TableColumn<Angajati, String> t1;
    @FXML
    private TableColumn<Angajati, String> t2;
    @FXML
    private TableColumn<Angajati, String> t3;
    @FXML
    private TableColumn<Angajati, LocalDate> t4;
    @FXML
    private TableColumn<Angajati, Double> t5;
    @FXML
    private TableColumn<Angajati, Double> t6;

    private static Angajati angajat;
    private static UnitateDeInvatamant scoala;

    private ArrayList<UnitateDeInvatamant> arr = new ArrayList<>();
    private File file = new File("saveData.dat");

    public void initialize() {
        if (file.length() == 0) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                boolean eof = false;
                while (!eof) {
                    try {
                        UnitateDeInvatamant unitate = (UnitateDeInvatamant) locFile.readObject();
                        arr.add(unitate);
                    } catch (EOFException e) {
                        eof = true;
                    }
                }
            } catch (IOException io) {
                System.out.println("IO Exception " + io.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException " + e.getMessage());
            }
        }
        listaUnitati.getItems().addAll(arr);
        for (UnitateDeInvatamant unit : listaUnitati.getItems()) {
            unit.loadData();
            for (Angajati angajat : unit.getAngajati()) {
                angajat.loadData();
            }
        }

        adaugareAngajat.prefWidthProperty().bind(hbox.widthProperty().divide(3));
        stergereAngajat.prefWidthProperty().bind(hbox.widthProperty().divide(3));
        editareAngajat.prefWidthProperty().bind(hbox.widthProperty().divide(3));

        t1.setCellValueFactory(new PropertyValueFactory<>("nume"));
        t2.setCellValueFactory(new PropertyValueFactory<>("prenume"));
        t3.setCellValueFactory(new PropertyValueFactory<>("decizie"));
        t4.setCellValueFactory(new PropertyValueFactory<>("dataDeciziei"));
        t5.setCellValueFactory(new PropertyValueFactory<>("sumaRestanta"));
        t6.setCellValueFactory(new PropertyValueFactory<>("penalizari"));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        t4.setCellFactory(column -> new TableCell<Angajati, LocalDate>() {
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

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
        t5.setCellFactory(column -> new TableCell<Angajati, Double>() {
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
        t6.setCellFactory(column -> new TableCell<Angajati, Double>() {
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

        listaUnitati.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tabelAngajati.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        listaUnitati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UnitateDeInvatamant>() {
            @Override
            public void changed(ObservableValue<? extends UnitateDeInvatamant> observable, UnitateDeInvatamant oldValue, UnitateDeInvatamant newValue) {
                if (newValue != null) {
                    stergereUnitate.setDisable(false);
                    editareUnitate.setDisable(false);
                    adaugareAngajat.setDisable(false);
                    tabelAngajati.setItems(listaUnitati.getSelectionModel().getSelectedItem().getAngajati());
                }
                if (newValue == null) {
                    stergereUnitate.setDisable(true);
                    editareUnitate.setDisable(true);
                    adaugareAngajat.setDisable(true);
                    stergereAngajat.setDisable(true);
                    editareAngajat.setDisable(true);
                }
            }
        });
        tabelAngajati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Angajati>() {
            @Override
            public void changed(ObservableValue<? extends Angajati> observable, Angajati oldValue, Angajati newValue) {
                if (newValue != null) {
                    stergereAngajat.setDisable(false);
                    editareAngajat.setDisable(false);
                }
                if (newValue == null) {
                    stergereAngajat.setDisable(true);
                    editareAngajat.setDisable(true);
                }
            }
        });
        tabelAngajati.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        if (tabelAngajati.getSelectionModel().getSelectedItem() != null) {
                            angajat = tabelAngajati.getSelectionModel().getSelectedItem();
                            scoala = listaUnitati.getSelectionModel().getSelectedItem();
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("infoAngajat.fxml"));
                                mainBorderPane.getScene().setRoot(root);
                                root.requestFocus();
                            } catch (IOException e) {
                                System.out.println("Eroare incarcare");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        listaUnitati.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node source = event.getPickResult().getIntersectedNode();
            while (source != null && !(source instanceof ListCell)) {
                source = source.getParent();
            }
            if (source == null || (source instanceof ListCell && ((ListCell) source).isEmpty())) {
                listaUnitati.getSelectionModel().clearSelection();
                tabelAngajati.setItems(null);
                stergereUnitate.setDisable(true);
                editareUnitate.setDisable(true);
                adaugareAngajat.setDisable(true);
                stergereAngajat.setDisable(true);
                editareAngajat.setDisable(true);

            }
        });
        tabelAngajati.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node source = event.getPickResult().getIntersectedNode();
            while (source != null && !(source instanceof TableRow)) {
                source = source.getParent();
            }
            if (source == null || (source instanceof TableRow && ((TableRow) source).isEmpty())) {
                if (tabelAngajati.getItems() != null) {
                    if (tabelAngajati.getItems().size() == 0) {
                        listaUnitati.getSelectionModel().clearSelection();
                    }
                    tabelAngajati.getSelectionModel().clearSelection();
                    stergereAngajat.setDisable(true);
                    editareAngajat.setDisable(true);
                }
            }
        });
    }

    @FXML
    private void adaugareUnitateDeInvatamant() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fereastraAdaugareUnitate.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Adaugare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 200);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraAdaugareUnitate controller = fxmlLoader.getController();
            UnitateDeInvatamant newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                listaUnitati.getItems().add(newItem);
                listaUnitati.getSelectionModel().select(newItem);
                saveData();
            }
        });
        dialog.showAndWait();
    }

    @FXML
    private void stergereUnitateDeInvatamant() {
        UnitateDeInvatamant temp = listaUnitati.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(0);
        alert.getButtonTypes().remove(0);
        ButtonType ok = new ButtonType("Stergere", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(ok);
        alert.getDialogPane().getButtonTypes().add(cancel);
        alert.setTitle("Confirmare stergere");
        alert.setHeaderText("Stergeti unitatea de invatamant: " + temp.getNume());
        alert.setContentText("Sunteti sigur? Apasati Stergere pentru confirmare sau Anulare pentru a anula");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            listaUnitati.getItems().remove(temp);
            saveData();
        }
        if (listaUnitati.getItems().size() == 0) {
            stergereUnitate.setDisable(true);
            editareUnitate.setDisable(true);
            adaugareAngajat.setDisable(true);
            stergereAngajat.setDisable(true);
            editareAngajat.setDisable(true);
            tabelAngajati.getItems().clear();
        }
    }

    @FXML
    private void editareUnitateDeInvatamant() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fereastraEditareUnitate.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Modificare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 200);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraEditareUnitate controller = fxmlLoader.getController();
            UnitateDeInvatamant newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                listaUnitati.getSelectionModel().getSelectedItem().setNume(newItem.getNume());
                listaUnitati.refresh();
                saveData();
            }
        });
        dialog.showAndWait();
    }

    @FXML
    private void adaugareAngajat() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fereastraAdaugareAngajat.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Adaugare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 410);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraAdaugareAngajat controller = fxmlLoader.getController();
            Angajati newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                listaUnitati.getSelectionModel().getSelectedItem().getAngajati().add(newItem);
                ObservableList<Angajati> lista = listaUnitati.getSelectionModel().getSelectedItem().getAngajati();
                tabelAngajati.setItems(lista);
                listaUnitati.getSelectionModel().getSelectedItem().savedata();
                saveData();
            }
        });
        dialog.showAndWait();
    }

    @FXML
    private void stergereAngajat() {
        Angajati temp = tabelAngajati.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(0);
        alert.getButtonTypes().remove(0);
        ButtonType ok = new ButtonType("Stergere", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(ok);
        alert.getDialogPane().getButtonTypes().add(cancel);
        alert.setTitle("Confirmare stergere");
        alert.setHeaderText("Stergeti angajatul cu numele: " + temp.getNume() + " " + temp.getPrenume());
        alert.setContentText("Sunteti sigur? Apasati Stergere pentru confirmare sau Anulare pentru a anula");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ok) {
            tabelAngajati.getItems().remove(temp);
            listaUnitati.getSelectionModel().getSelectedItem().savedata();
            saveData();
        }
        if (tabelAngajati.getItems().size() == 0) {
            stergereAngajat.setDisable(true);
            editareAngajat.setDisable(true);
        }
    }

    @FXML
    private void editareAngajat() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("fereastraEditareAngajat.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Eroare incarcare");
            e.printStackTrace();
            return;
        }
        ButtonType ok = new ButtonType("Modificare", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Anulare", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().setPrefSize(400, 410);
        dialog.getDialogPane().getButtonTypes().add(ok);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        Button btnLogin = (Button) dialog.getDialogPane().lookupButton(ok);
        btnLogin.addEventFilter(ActionEvent.ACTION, event -> {
            ControllerFereastraEditareAngajat controller = fxmlLoader.getController();
            Angajati newItem = controller.procesare();
            if (newItem == null) {
                event.consume();
            }
            if (newItem != null) {
                double temp = tabelAngajati.getSelectionModel().getSelectedItem().getSumaRestanta();
                if ((newItem.getSumaRestanta() != 0) && (newItem.getSumaRestanta() != tabelAngajati.getSelectionModel().getSelectedItem().getSumaRestanta())) {
                    tabelAngajati.getSelectionModel().getSelectedItem().setRestanteNeachitate(newItem.getSumaRestanta());
                    if (tabelAngajati.getSelectionModel().getSelectedItem().getRestanteNeachitate() < 0) {
                        tabelAngajati.getSelectionModel().getSelectedItem().setRestanteNeachitate(temp);
                        newItem.setNume("");
                        newItem.setPrenume("");
                        newItem.setDecizie("");
                        newItem.setDataDeciziei(null);
                        controller.eroare();
                        event.consume();
                    } else {
                        tabelAngajati.getSelectionModel().getSelectedItem().setSumaRestanta(newItem.getSumaRestanta());
                    }
                }
                if ((newItem.getDataDeciziei() != null) && (!newItem.getDataDeciziei().equals(tabelAngajati.getSelectionModel().getSelectedItem().getDataDeciziei()))) {
                    if (tabelAngajati.getSelectionModel().getSelectedItem().getPlati().size() == 0) {
                        tabelAngajati.getSelectionModel().getSelectedItem().setDataDeciziei(newItem.getDataDeciziei());
                    } else if ((tabelAngajati.getSelectionModel().getSelectedItem().getPlati().size() > 0)) {
                        if ((newItem.getDataDeciziei().isBefore(tabelAngajati.getSelectionModel().getSelectedItem().getPlati().get(0).getDataPlatii()))
                                || (newItem.getDataDeciziei().equals(tabelAngajati.getSelectionModel().getSelectedItem().getPlati().get(0).getDataPlatii()))) {
                            tabelAngajati.getSelectionModel().getSelectedItem().setDataDeciziei(newItem.getDataDeciziei());
                        } else {
                            newItem.setNume("");
                            newItem.setPrenume("");
                            newItem.setDecizie("");
                            tabelAngajati.getSelectionModel().getSelectedItem().setSumaRestanta(temp);
                            tabelAngajati.getSelectionModel().getSelectedItem().setRestanteNeachitate(temp);
                            controller.eroareData();
                            event.consume();
                        }
                    }
                }
                if ((!newItem.getNume().isEmpty()) && (!newItem.getNume().equals(tabelAngajati.getSelectionModel().getSelectedItem().getNume()))) {
                    tabelAngajati.getSelectionModel().getSelectedItem().setNume(newItem.getNume());
                }
                if ((!newItem.getPrenume().isEmpty()) && (!newItem.getPrenume().equals(tabelAngajati.getSelectionModel().getSelectedItem().getPrenume()))) {
                    tabelAngajati.getSelectionModel().getSelectedItem().setPrenume(newItem.getPrenume());
                }
                if ((!newItem.getDecizie().isEmpty()) && (!newItem.getDecizie().equals(tabelAngajati.getSelectionModel().getSelectedItem().getDecizie()))) {
                    tabelAngajati.getSelectionModel().getSelectedItem().setDecizie(newItem.getDecizie());
                }
                tabelAngajati.getSelectionModel().getSelectedItem().calculPenalitati();
                tabelAngajati.refresh();
                listaUnitati.getSelectionModel().getSelectedItem().savedata();
                saveData();
            }
        });
        dialog.showAndWait();
    }

    public static UnitateDeInvatamant getScoala() {
        return scoala;
    }

    public static Angajati getAngajat() {
        return angajat;
    }

    public void saveData() {
        arr.clear();
        for (int i = 0; i < listaUnitati.getItems().size(); i++) {
            arr.add(listaUnitati.getItems().get(i));
        }
        try (ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            for (UnitateDeInvatamant unitate : arr) {
                locFile.writeObject(unitate);
            }
        } catch (IOException io) {
            System.out.println("IO Exception " + io.getMessage());
        }
    }
}
