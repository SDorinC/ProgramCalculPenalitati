package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class UnitateDeInvatamant implements Serializable {

    private String nume;
    private transient ObservableList<Angajati> angajati;
    private ArrayList<Angajati> arr = new ArrayList<>();

    public UnitateDeInvatamant(String nume) {
        this.nume = nume;
        this.angajati = FXCollections.observableArrayList();
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public ObservableList<Angajati> getAngajati() {
        return angajati;
    }

    @Override
    public String toString() {
        return nume;
    }

    public void savedata() {
        arr.clear();
        for (Angajati angajat : angajati) {
            arr.add(angajat);
        }
    }

    public void loadData() {
        angajati = FXCollections.observableArrayList(arr);
    }
}
