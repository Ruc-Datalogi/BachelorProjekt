package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimaryWindow {

    final static double FONT_SIZE = 46.0;
    final static double BUTTON_HEIGHT = 40.0;
    final static double BUTTON_WIDTH = 100.0;
    final static double CANVAS_HEIGHT = 800.0;
    final static double CANVAS_WIDTH = 1100.0;
    final static double TEXTAREA_WIDTH = 360.0;
    static TextArea debugTextField = new TextArea();

    public static BorderPane createMainWindow() throws IOException {
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();
        Canvas mainCanvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
        Painter painter = new Painter(mainCanvas);
        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile1D("./src/Data/N1C1W1_A.BPP");
        dataImporter.loadFile2D("./src/Data/2D_DataSet.csv");
        Label title = new Label("Bin Packing Problem");
        title.setFont(new Font(FONT_SIZE));
        debugTextField.setMaxHeight(mainCanvas.getHeight());
        debugTextField.setPrefHeight(mainCanvas.getHeight());
        debugTextField.setPrefWidth(TEXTAREA_WIDTH);
        debugTextField.setMaxWidth(TEXTAREA_WIDTH);
        debugTextField.setWrapText(true);
        debugTextField.setEditable(true);
        debugTextField.setDisable(false);
        Button showPlot = new Button("Show Plot");
        showPlot.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        showPlot.setOnAction(a -> {
            plotPython();
        });
        Button calculate = new Button("Calculate");
        calculate.setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        calculate.setOnAction((e) -> {
            painter.fillBlank();
            if(State.getState().selectedDimension == Dimension.ONEDIMENSION){
                ArrayList<Bin1D> algoSolution1D = Calculator.calculateOneDimension(State.getState().selectedAlgorithm, dataImporter.bins1D, 100);

                AtomicInteger sum = new AtomicInteger();
                algoSolution1D.forEach((b) -> sum.addAndGet(b.capacity));
                for(int i = 0 ; i < algoSolution1D.size() ; i++ ) {
                    painter.drawBox1D((40*(i%14) +16), 50+50*(Math.floorDiv(i,14)), algoSolution1D.get(i));
                }
                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
                try {
                    simulatedAnnealing.simulatedAnnealing(new TwoOpt(algoSolution1D),200,0.1f, algoSolution1D.size(),0.9f);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                for(int i = 0 ; i < simulatedAnnealing.finalSolution.size() ; i++ ) {
                    painter.drawBox1D((40*(i%14) +16), 300+50*(Math.floorDiv(i,14)), (Bin1D) simulatedAnnealing.finalSolution.get(i));
                }
            }
        });
        GenerateRectangleDataSet generateRectangleDataSet = new GenerateRectangleDataSet(200,200);
        SequencePairs testSeq = generateRectangleDataSet.generateSeq();
        /*
        SequencePairs testSeq = new SequencePairs(CommonFunctions.randomIntegerList(State.getState().modules.size()),
                CommonFunctions.randomIntegerList(State.getState().modules.size()),
                State.getState().modules);
        */
        testSeq.calculatePlacementTable();
        SimulatedAnnealing sa = new SimulatedAnnealing();
        sa.simulatedAnnealing(testSeq, 20000000,1f,testSeq.optimizationFactor,0.99f);
        //painter.drawBoxesInBin(testSeq.testBin);

        ComboBox<Algorithms> comboBoxAlgorithms = new ComboBox();
        setComboboxAlgorithms(comboBoxAlgorithms);

        ComboBox<Dimension> comboBoxDimensions = new ComboBox();
        setComboBox(comboBoxAlgorithms, comboBoxDimensions);

        HBox hBox = new HBox();
        HBox mainHBox = new HBox();
        hBox.setSpacing(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(comboBoxDimensions, comboBoxAlgorithms, showPlot);
        mainHBox.setSpacing(8);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.getChildren().addAll(mainCanvas, debugTextField);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(16));
        topBorderPane.setCenter(hBox);
        topBorderPane.setTop(title);
        mainBorderPane.setTop(topBorderPane);
        mainBorderPane.setCenter(mainHBox);
        mainBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                System.exit(1);
            }
            e.consume();
        });
        return mainBorderPane;
    }

    private static void setComboboxAlgorithms(ComboBox<Algorithms> comboBoxAlgorithms) {
        comboBoxAlgorithms.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxAlgorithms.setItems(FXCollections.observableArrayList(Algorithms.getAlgorithms(Dimension.ONEDIMENSION)));
        comboBoxAlgorithms.getSelectionModel().selectFirst();
        comboBoxAlgorithms.setOnAction((e) -> {
            changeAlgorithmState(comboBoxAlgorithms.getSelectionModel().getSelectedItem());
        });
    }

    private static void setComboBox(ComboBox<Algorithms> comboBoxAlgorithms, ComboBox<Dimension> comboBoxDimensions) {
        comboBoxDimensions.setItems(FXCollections.observableArrayList(Dimension.values()));
        comboBoxDimensions.getSelectionModel().selectFirst();
        comboBoxDimensions.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxDimensions.setOnAction((e) -> {
            changeDimensionState(comboBoxDimensions.getSelectionModel().getSelectedItem(), comboBoxAlgorithms);
        });
    }

    private static void changeDimensionState (Dimension d, ComboBox c) {
        c.setItems(FXCollections.observableArrayList(Algorithms.getAlgorithms(d)));
        c.getSelectionModel().selectFirst();
        State.getState().selectedDimension = d;
    }

    private static void plotPython(){
        if(State.getState().iterList != null ) {
            PythonPlotter scriptPython = new PythonPlotter();
            scriptPython.runPython(State.getState().iterList, State.getState().energyList);
        }
    }

    public static void changeDebugMessage(String s) {
        String[] lines = s.split("\n");
        String output = "";
        for(String s2 : lines) {
            output += s2 + "\n";
        }
        debugTextField.setText(output);
    }

    private static void changeAlgorithmState (Algorithms a) {
        State.getState().selectedAlgorithm = a;
    }
}