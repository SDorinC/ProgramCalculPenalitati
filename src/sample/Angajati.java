package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Angajati implements Serializable {

    private String nume;
    private String prenume;
    private String decizie;
    private LocalDate dataDeciziei;
    private double sumaRestanta;
    private double restanteNeachitate;
    private double penalizari;
    private transient ObservableList<Plata> plati;
    private ArrayList<Plata> arr = new ArrayList<>();

    public Angajati(String nume, String prenume, String decizie, LocalDate dataDeciziei, double sumaRestanta) {
        this.nume = nume;
        this.prenume = prenume;
        this.decizie = decizie;
        this.dataDeciziei = dataDeciziei;
        this.sumaRestanta = sumaRestanta;
        this.restanteNeachitate = this.sumaRestanta;
        this.plati = FXCollections.observableArrayList();
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getDecizie() {
        return decizie;
    }

    public LocalDate getDataDeciziei() {
        return dataDeciziei;
    }

    public double getSumaRestanta() {
        return sumaRestanta;
    }

    public double getRestanteNeachitate() {
        return restanteNeachitate;
    }

    public double getPenalizari() {
        return penalizari;
    }

    public ObservableList<Plata> getPlati() {
        return plati;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public void setDecizie(String decizie) {
        this.decizie = decizie;
    }

    public void setDataDeciziei(LocalDate dataDeciziei) {
        this.dataDeciziei = dataDeciziei;
    }

    public void setSumaRestanta(double sumaRestanta) {
        this.sumaRestanta = sumaRestanta;
    }

    public void setRestanteNeachitate(double suma) {
        this.restanteNeachitate = suma;
        BigDecimal b1 = BigDecimal.valueOf(restanteNeachitate);
        for (Plata plata : plati) {
            BigDecimal b2 = BigDecimal.valueOf(plata.getSumaPlatita());
            b1 = b1.subtract(b2);
        }
        restanteNeachitate = b1.doubleValue();
    }

    public void sortare() {
        Collections.sort(plati);
    }

    public boolean adaugarePlata(Plata plata) {
        BigDecimal b1 = BigDecimal.valueOf(restanteNeachitate);
        BigDecimal b2 = BigDecimal.valueOf(plata.getSumaPlatita());
        if (b1.subtract(b2).compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        BigDecimal b3 = b1.subtract(b2);
        restanteNeachitate = b3.doubleValue();
        return true;
    }

    public void stergerePlata(Plata plata) {
        BigDecimal b1 = BigDecimal.valueOf(restanteNeachitate);
        BigDecimal b2 = BigDecimal.valueOf(plata.getSumaPlatita());
        BigDecimal b3 = b1.add(b2);
        restanteNeachitate = b3.doubleValue();
    }

    public boolean editarePlata(Plata plataVeche, Plata plataNoua) {
        BigDecimal b1 = BigDecimal.valueOf(restanteNeachitate);
        BigDecimal b2 = BigDecimal.valueOf(plataVeche.getSumaPlatita());
        BigDecimal b3 = BigDecimal.valueOf(plataNoua.getSumaPlatita());
        if (b1.add(b2).subtract(b3).compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        BigDecimal b4 = b1.add(b2).subtract(b3);
        restanteNeachitate = b4.doubleValue();
        return true;
    }

    public void calculPenalitati() {
        penalizari = 0;
        int dataInitiala;
        int dataPlatii;
        double dobanda = 0;
        double restante = sumaRestanta;
        for (int i = 0; i < plati.size(); i++) {
            Plata temp = plati.get(i);
            if (i == 0) {
                dataInitiala = transformareData(dataDeciziei.getYear(), dataDeciziei.getMonthValue(), dataDeciziei.getDayOfMonth());
                dataPlatii = transformareData(temp.getDataPlatii().getYear(), temp.getDataPlatii().getMonthValue(), temp.getDataPlatii().getDayOfMonth());
                for (int j = dataInitiala; j <= dataPlatii; j++) {
                    dobanda += sumaRestanta * rataDobanda(j) / 100;
                }
                penalizari += dobanda;
                dobanda = 0;
            } else {
                dataInitiala = transformareData(plati.get(i - 1).getDataPlatii().getYear(), plati.get(i - 1).getDataPlatii().getMonthValue(), plati.get(i - 1).getDataPlatii().getDayOfMonth()) + 1;
                dataPlatii = transformareData(temp.getDataPlatii().getYear(), temp.getDataPlatii().getMonthValue(), temp.getDataPlatii().getDayOfMonth());
                for (int j = dataInitiala; j <= dataPlatii; j++) {
                    dobanda += restante * rataDobanda(j) / 100;
                }
                penalizari += dobanda;
                dobanda = 0;
            }
            restante -= temp.getSumaPlatita();
        }
    }

    private static boolean anBisect(int an) {
        if ((an < 1) || (an > 9999)) {
            return false;
        } else if (((an % 4 == 0) && (an % 100 != 0)) || (an % 400 == 0)) {
            return true;
        }
        return false;
    }

    public int transformareData(int an, int luna, int ziua) {
        int zi = 0;
        for (int i = 2006; i <= an; i++) {
            if (anBisect(i - 1)) {
                zi += 366;
            } else {
                zi += 365;
            }
        }
        int februarie;
        if (anBisect(an)) {
            februarie = 29;
        } else {
            februarie = 28;
        }
        switch (luna) {
            case 1:
                zi += ziua;
                break;
            case 2:
                zi = zi + 31 + ziua;
                break;
            case 3:
                zi = zi + 31 + februarie + ziua;
                break;
            case 4:
                zi = zi + 31 + februarie + 31 + ziua;
                break;
            case 5:
                zi = zi + 31 + februarie + 31 + 30 + ziua;
                break;
            case 6:
                zi = zi + 31 + februarie + 31 + 30 + 31 + ziua;
                break;
            case 7:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + ziua;
                break;
            case 8:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + 31 + ziua;
                break;
            case 9:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + 31 + 31 + ziua;
                break;
            case 10:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + 31 + 31 + 30 + ziua;
                break;
            case 11:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + ziua;
                break;
            case 12:
                zi = zi + 31 + februarie + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + ziua;
                break;
        }
        return zi;
    }

    private double rataDobanda(int zi) {
        double dobanda;
        if (((zi > 1095 && zi < 1462) || (zi > 2556 && zi < 2923) || (zi > 4017 && zi < 4384) || (zi > 5478 && zi < 5845))) {
            dobanda = dobandaVariabila(zi) / 366;
        } else {
            dobanda = dobandaVariabila(zi) / 365;
        }
        return dobanda;
    }

    private double dobandaVariabila(int zi) {
        double dobanda = 0;
        if (zi >= 1 && zi <= 181) {
            dobanda = 17.31;
        } else if (zi > 181 && zi <= 365) {
            dobanda = 8;
        } else if (zi > 365 && zi <= 546) {
            dobanda = 7.5;
        } else if (zi > 546 && zi <= 730) {
            dobanda = 8.5;
        } else if (zi > 730 && zi <= 911) {
            dobanda = 8.75;
        } else if (zi > 911 && zi <= 1095) {
            dobanda = 7.25;
        } else if (zi > 1095 && zi <= 1277) {
            dobanda = 7.5;
        } else if (zi > 1277 && zi <= 1461) {
            dobanda = 9.75;
        } else if (zi > 1461 && zi <= 1642) {
            dobanda = 10.25;
        } else if (zi > 1642 && zi <= 1826) {
            dobanda = 9.5;
        } else if (zi > 1826 && zi <= 2007) {
            dobanda = 8;
        } else if (zi > 2007 && zi <= 2191) {
            dobanda = 6.25;
        } else if (zi > 2191 && zi <= 2434) {
            dobanda = 6.25;
        } else if (zi > 2434 && zi <= 2497) {
            dobanda = 10.25;
        } else if (zi > 2497 && zi <= 2556) {
            dobanda = 10;
        } else if (zi > 2556 && zi <= 2561) {
            dobanda = 10;
        } else if (zi > 2561 && zi <= 2589) {
            dobanda = 9.75;
        } else if (zi > 2589 && zi <= 2645) {
            dobanda = 9.5;
        } else if (zi > 2645 && zi <= 2922) {
            dobanda = 9.25;
        } else if (zi > 2922 && zi <= 3104) {
            dobanda = 9.25;
        } else if (zi > 3104 && zi <= 3139) {
            dobanda = 9;
        } else if (zi > 3139 && zi <= 3195) {
            dobanda = 8.5;
        } else if (zi > 3195 && zi <= 3231) {
            dobanda = 8.25;
        } else if (zi > 3231 && zi <= 3295) {
            dobanda = 8;
        } else if (zi > 3295 && zi <= 3322) {
            dobanda = 7.75;
        } else if (zi > 3322 && zi <= 3503) {
            dobanda = 7.5;
        } else if (zi > 3503 && zi <= 3560) {
            dobanda = 7.25;
        } else if (zi > 3560 && zi <= 3595) {
            dobanda = 7;
        } else if (zi > 3595 && zi <= 3659) {
            dobanda = 6.75;
        } else if (zi > 3659 && zi <= 3687) {
            dobanda = 6.5;
        } else if (zi > 3687 && zi <= 3742) {
            dobanda = 6.25;
        } else if (zi > 3742 && zi <= 3778) {
            dobanda = 6;
        } else if (zi > 3778 && zi <= 4017) {
            dobanda = 5.75;
        } else if (zi > 4017 && zi <= 4383) {
            dobanda = 5.75;
        } else if (zi > 4383 && zi <= 4748) {
            dobanda = 5.75;
        } else if (zi > 4748 && zi <= 4756) {
            dobanda = 5.75;
        } else if (zi > 4756 && zi <= 4786) {
            dobanda = 6;
        } else if (zi > 4786 && zi <= 4875) {
            dobanda = 6.25;
        } else if (zi > 4875 && zi <= 5844) {
            dobanda = 6.5;
        }
        return dobanda;
    }

    public void savedata() {
        arr.clear();
        for (Plata plata : plati) {
            arr.add(plata);
        }
    }

    public void loadData() {
        plati = FXCollections.observableArrayList(arr);
    }
}
