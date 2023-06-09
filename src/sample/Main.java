package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Parent root;
    public static FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("fereastraPrincipala.fxml"));
        root = loader.load();
        primaryStage.setTitle("Calcul Penalitati");
        primaryStage.setScene(new Scene(root, 1150, 700));
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static ControllerFereastraPrincipala getCtrl() {
        return loader.getController();
    }
}
