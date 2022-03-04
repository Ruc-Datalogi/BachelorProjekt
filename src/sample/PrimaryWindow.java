package sample;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimaryWindow {

    final static double FONT_SIZE = 46.0;
    final static double BUTTON_HEIGHT = 40.0;
    final static double BUTTON_WIDTH = 100.0;


    public static BorderPane createMainWindow(){
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

        SequencePairs testSeq = new SequencePairs(new ArrayList<Integer>(Arrays.asList(1,4,3,2,5,6,7,8,9)),
                new ArrayList<Integer>(Arrays.asList(2,3,5,1,4,6,7,8,9)),
                new ArrayList<Module>(Arrays.asList(
                        new Module(1,2,4),
                        new Module(2,1,3),
                        new Module(3,2,2),
                        new Module(4,3,4),
                        new Module(5,2,2),
                        new Module(6,5,1),
                        new Module(7,3,1),
                        new Module(8,2,2),
                        new Module(9,2,5)
                )));

        //newSeqTest.calculatePlacementTable();
        //painter.drawBoxesInBin(newSeqTest.testBin);
        testSeq.calculatePlacementTable();

        SimulatedAnnealing sa = new SimulatedAnnealing();

        sa.simulatedAnnealing(testSeq, 2000000,0.1f,testSeq.optimizationFactor,0.999f);

        System.out.println(sa.finalSolution);

        painter.drawBoxesInBin(testSeq.testBin);

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
        return mainBorderPane;
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

    private static void changeAlgorithmState (Algorithms a) {
        State.getState().selectedAlgorithm = a;
    }
}
