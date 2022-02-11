package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    final double FONT_SIZE = 46.0;
    final double BUTTON_HEIGHT = 40.0;
    final double BUTTON_WIDTH = 100.0;
    final State state = new State();
    static final String[] dimensionList = new String[]{"One Dimension","Two Dimension","Three Dimension"};

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();
        Canvas mainCanvas = new Canvas(600,600);
        GraphicsContext gc = mainCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,600,600);

        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile("./src/Data/N1C1W1_A.BPP");
        Calculator.firstFit(dataImporter.bins1D, 100);

        Label title = new Label("Bin Packing Problem");
        title.setFont(new Font(FONT_SIZE));

        Button calculate = new Button("Calculate");
        calculate.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        calculate.setOnAction((e) -> {
            if(state.selectedDimension == Dimension.ONEDIMENSION){
                Calculator.calculateOneDimension(state.selectedAlgorithm, dataImporter.bins1D, 100);
            }
        });

        ComboBox<Algorithms> comboBoxAlgorithms = new ComboBox();
        comboBoxAlgorithms.getSelectionModel().selectFirst();
        comboBoxAlgorithms.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxAlgorithms.setItems(FXCollections.observableArrayList(Algorithms.getAlgorithms(Dimension.ONEDIMENSION)));
        comboBoxAlgorithms.setOnAction((e) -> {
            changeAlgorithmState(comboBoxAlgorithms.getSelectionModel().getSelectedItem());
        });

        ComboBox<Dimension> comboBoxDimensions = new ComboBox();
        comboBoxDimensions.setItems(FXCollections.observableArrayList(Dimension.values()));
        comboBoxDimensions.getSelectionModel().selectFirst();
        comboBoxDimensions.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxDimensions.setOnAction((e) -> {
            changeDimensionState(comboBoxDimensions.getSelectionModel().getSelectedItem(), comboBoxAlgorithms);
        });

        HBox hBox = new HBox();
        hBox.setSpacing(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(comboBoxDimensions, comboBoxAlgorithms, calculate);

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

    private void changeDimensionState (Dimension d, ComboBox c) {
        c.setItems(FXCollections.observableArrayList(Algorithms.getAlgorithms(d)));
        c.getSelectionModel().selectFirst();
        state.selectedDimension = d;
    }

    private void changeAlgorithmState (Algorithms a) {
        state.selectedAlgorithm = a;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
