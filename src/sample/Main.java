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
        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile("./src/Data/N1C1W1_A.BPP");

        Calculator.firstFit(dataImporter.bins1D, 100);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,600,600);

        Label title = new Label("Bin Packing Problem");
        Button calculate = new Button("Calculate");
        calculate.setOnAction((e) -> {
            if(state.selectedDimension == Dimension.ONEDIMENSION) {
                Calculator.calculateOneDimension(state.selectedAlgorithmOneDimension, dataImporter.bins1D, 100);
            }
        });
        calculate.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

        title.setFont(new Font(FONT_SIZE));
        HBox hBox = new HBox();
        ComboBox<Dimension> comboBoxDimensions = new ComboBox();
        comboBoxDimensions.setItems(FXCollections.observableArrayList(Dimension.values()));
        ComboBox<AlgorithmsOneDimension> comboBoxAlgorithms = new ComboBox();
        comboBoxAlgorithms.setItems(FXCollections.observableArrayList(AlgorithmsOneDimension.values()));

        comboBoxAlgorithms.setOnAction((e) -> {
            changeAlgorithmState(comboBoxAlgorithms);
        });


        comboBoxDimensions.getSelectionModel().selectFirst();
        comboBoxDimensions.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxDimensions.setOnAction((e) -> {
            changeDimensionState(comboBoxDimensions.getSelectionModel().getSelectedItem(), comboBoxAlgorithms);
        });

        comboBoxAlgorithms.getSelectionModel().selectFirst();
        comboBoxAlgorithms.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);

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
        switch (d) {
            case ONEDIMENSION:
                c.setItems(FXCollections.observableArrayList(AlgorithmsOneDimension.values()));
                c.getSelectionModel().selectFirst();
                state.selectedDimension = Dimension.ONEDIMENSION;
                break;
            case TWODIMENSION:
                c.setItems(FXCollections.observableArrayList(AlgorithmsTwoDimension.values()));
                c.getSelectionModel().selectFirst();
                state.selectedDimension = Dimension.TWODIMENSION;
                break;
            case THREEDIMENSION:
                state.selectedDimension = Dimension.THREEDIMENSION;
                break;
        }
    }

    private void changeAlgorithmState(ComboBox c) {
        if (state.selectedDimension == Dimension.ONEDIMENSION) {

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
