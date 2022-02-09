package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();
        Canvas mainCanvas = new Canvas(600,600);

        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile("./src/Data/N1C1W1_A.BPP");

        Algorithms.firstFit(dataImporter.bins1D, 100);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,600,600);

        Label title = new Label("Knapsacking boys");
        ToolBar toolBar = new ToolBar();
        toolBar.getItems().add(new Button("Button 1"));
        toolBar.getItems().add(new Button("Button 2"));
        toolBar.getItems().add(new Button("Button 3"));
        toolBar.getItems().add(new Button("Button 4"));

        topBorderPane.setTop(title);
        topBorderPane.setCenter(toolBar);


        mainBorderPane.setTop(topBorderPane);
        mainBorderPane.setCenter(mainCanvas);
        primaryStage.setTitle("Drengene");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(mainBorderPane, 1000, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
