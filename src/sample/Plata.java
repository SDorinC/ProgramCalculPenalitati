package sample;

import java.io.Serializable;
import java.time.LocalDate;

public class Plata implements Comparable<Plata>, Serializable {

    private double sumaPlatita;
    private LocalDate dataPlatii;

    public Plata(double sumaPlatita, LocalDate dataPlatii) {
        this.sumaPlatita = sumaPlatita;
        this.dataPlatii = dataPlatii;
    }

    public double getSumaPlatita() {
        return sumaPlatita;
    }

    public void setSumaPlatita(double sumaPlatita) {
        this.sumaPlatita = sumaPlatita;
    }

    public LocalDate getDataPlatii() {
        return dataPlatii;
    }

    public void setDataPlatii(LocalDate dataPlatii) {
        this.dataPlatii = dataPlatii;
    }

    @Override
    public int compareTo(Plata o) {
        return getDataPlatii().compareTo(o.getDataPlatii());
    }
}
