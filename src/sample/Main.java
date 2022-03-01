package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {

    final double FONT_SIZE = 46.0;
    final double BUTTON_HEIGHT = 40.0;
    final double BUTTON_WIDTH = 100.0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();
        Canvas mainCanvas = new Canvas(600,600);
        Painter painter = new Painter(mainCanvas);
        painter.fillBlank();
        DataImporter dataImporter = new DataImporter();
        dataImporter.loadFile("./src/Data/N1C1W1_A.BPP");
        Calculator.firstFit(dataImporter.bins1D, 100);

        Label title = new Label("Bin Packing Problem");
        title.setFont(new Font(FONT_SIZE));
        Button showPlot = new Button("Show Plot");
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
                simulatedAnnealing.simulatedAnnealing(new TwoOpt(algoSolution1D),200000000,0.0000000001f, algoSolution1D.size(),0.999f);

                for(int i = 0 ; i < simulatedAnnealing.finalSolution.size() ; i++ ) {
                    painter.drawBox1D((40*(i%14) +16), 300+50*(Math.floorDiv(i,14)), (Bin1D) simulatedAnnealing.finalSolution.get(i));
                }
            }
        });
        //Test
        Bin2D testBin = new Bin2D(500,500);
        testBin.addBox(new Box2D(0,0,20,20));
        testBin.addBox(new Box2D(20,0,30,30));
        testBin.addBox(new Box2D(0,30,40,40));
        //painter.drawBoxesInBin(testBin);
        //painter.drawBox2D(100,100,200,200);

        SequencePairs testSeq = new SequencePairs(new ArrayList<Integer>(Arrays.asList(1,4,3,2,5)),
                new ArrayList<Integer>(Arrays.asList(2,3,5,1,4)),
                new ArrayList<Module>(Arrays.asList(
                        new Module(1,2,4),
                        new Module(2,1,3),
                        new Module(3,2,2),
                        new Module(4,3,4),
                        new Module(5,2,1)
                        )));
        SequencePairs newSeqTest = new SequencePairs(new ArrayList<Integer>(Arrays.asList(1,2,4,5,3)),
                new ArrayList<Integer>(Arrays.asList(3,2,4,1,5)),
                new ArrayList<Module>(Arrays.asList(
                        new Module(1,4,3),
                        new Module(2,2,2),
                        new Module(3,3,1),
                        new Module(4,1,1),
                        new Module(5,2,3)
                        )));

        SequencePairs TestingNew = new SequencePairs(new ArrayList<Integer>(Arrays.asList(5,4,1,3,2)),
                new ArrayList<Integer>(Arrays.asList(3,1,2,5,4)),
                new ArrayList<Module>(Arrays.asList(
                        new Module(1,2,4),
                        new Module(2,1,3),
                        new Module(3,2,2),
                        new Module(4,3,4),
                        new Module(5,2,1)
                )));
        //newSeqTest.calculatePlacementTable();
        //painter.drawBoxesInBin(newSeqTest.testBin);
        TestingNew.calculatePlacementTable();
        painter.drawBoxesInBin(TestingNew.testBin);

        painter.drawGraph(null,null);


        showPlot.setOnAction(a -> {
            plotPython();
        });

        ComboBox<Algorithms> comboBoxAlgorithms = new ComboBox();
        comboBoxAlgorithms.setPrefSize(BUTTON_WIDTH*1.4, BUTTON_HEIGHT);
        comboBoxAlgorithms.setItems(FXCollections.observableArrayList(Algorithms.getAlgorithms(Dimension.ONEDIMENSION)));
        comboBoxAlgorithms.getSelectionModel().selectFirst();
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
        hBox.getChildren().addAll(comboBoxDimensions, comboBoxAlgorithms, calculate, showPlot);

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
        State.getState().selectedDimension = d;
    }

    private void plotPython(){
        if(State.getState().iterList != null ) {
            PythonPlotter scriptPython = new PythonPlotter();
            scriptPython.runPython(State.getState().iterList, State.getState().energyList);
        }
    }

    private void changeAlgorithmState (Algorithms a) {
        State.getState().selectedAlgorithm = a;
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
