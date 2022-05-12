package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GenerateRectangleDataSet.generateDatasets(50);
        primaryStage.setTitle("Drengene");
        primaryStage.setScene(new Scene(PrimaryWindow.createMainWindow(),0,0));
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
