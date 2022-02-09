package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    final static double FONT_SIZE = 46.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();
        Canvas mainCanvas = new Canvas(600,600);

        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile("C:\\Users\\marti\\Desktop\\BachelorProjekt\\src\\Data\\N1C1W1_A.BPP");
        Algorithms.firstFit(dataImporter.bins1D, 100);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,600,600);

        Label title = new Label("Bin Packing Problem");
        Button calculate = new Button("Calculate");
        calculate.setPrefSize(100,40);
        Button tester = new Button("Tester");
        tester.setPrefSize(100,40);

        title.setFont(new Font(FONT_SIZE));
        HBox hBox = new HBox();
        hBox.setSpacing(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(calculate, tester);


        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(16));
        topBorderPane.setCenter(hBox);
        topBorderPane.setTop(title);


        mainBorderPane.setTop(topBorderPane);
        mainBorderPane.setCenter(mainCanvas);
        primaryStage.setTitle("Drengene");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(mainBorderPane, 1000, 800));
        primaryStage.show();
    }

    private ComboBox algorithms () {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1 Dimension",
                        "2 Dimension",
                        "3 Dimension"
                );

        ComboBox comboBox = new ComboBox(options);
        return comboBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
