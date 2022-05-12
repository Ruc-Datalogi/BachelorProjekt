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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

public class PrimaryWindow {

    final static double FONT_SIZE = 46.0;
    final static double BUTTON_HEIGHT = 40.0;
    final static double BUTTON_WIDTH = 100.0;
    final static double CANVAS_HEIGHT = 800.0;
    final static double CANVAS_WIDTH = 1100.0;
    final static double TEXTAREA_WIDTH = 360.0;
    static TextArea debugTextField = new TextArea();
    static Canvas mainCanvas = new Canvas(CANVAS_WIDTH,CANVAS_HEIGHT);
    static Painter painter = new Painter(mainCanvas);

    public static BorderPane createMainWindow() throws IOException {
        BorderPane mainBorderPane = new BorderPane();
        BorderPane topBorderPane = new BorderPane();


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
        Button doSATests = new Button("TestData");
        doSATests.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        doSATests.setOnAction(a-> {
                try {
                    RunMultiTestVariables(0,500000,50000,10,2,0.999f,0.9f,100);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    simulatedAnnealing.simulatedAnnealing(new TwoOpt(algoSolution1D),200,0.1f, algoSolution1D.size());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                for(int i = 0 ; i < simulatedAnnealing.finalSolution.size() ; i++ ) {
                    painter.drawBox1D((40*(i%14) +16), 300+50*(Math.floorDiv(i,14)), (Bin1D) simulatedAnnealing.finalSolution.get(i));
                }
            }
        });

        ArrayList<String> line = new ArrayList<>();

        //StartTemp=0, mintemp=1, coolingrate=2;
        //System.out.println("Testing between 200000 and 0 start temp");
        //RunTestForVariables(0,200000,0,20);
        //System.out.println("Testing between 1 and 0.5 cooling rate");
        //RunTestForVariables(2,0.999999f,0.5f,20);
        //CSVWriter.getCsvWriter().createAndWrite("src/Results/", "testResults.csv",testResult);

        GenerateRectangleDataSet generateRectangleDataSet = new GenerateRectangleDataSet(200,200);
        SequencePairs testSeq = generateRectangleDataSet.generateSeq();
        /*
        SequencePairs testSeq = new SequencePairs(CommonFunctions.randomIntegerList(State.getState().modules.size()),
                CommonFunctions.randomIntegerList(State.getState().modules.size()),
                State.getState().modules);
        */
        /*
        testSeq.calculatePlacementTable();
        SimulatedAnnealing sa = new SimulatedAnnealing();
        sa.simulatedAnnealing(testSeq, 20000000,1f,0.99f);
        painter.drawBoxesInBin(testSeq.testBin);

         */
        /*
        DivideAndConquer divideAndConquer = new DivideAndConquer(new ArrayList<Integer>(Arrays.asList(5,4,1,3,2,6,7,8)),
                new ArrayList<Integer>(Arrays.asList(3,6,5,7,8,1,2,5,4)),
                new ArrayList<Module>(Arrays.asList(
                        new Module(1,2,4),
                        new Module(2,1,3),
                        new Module(3,2,2),
                        new Module(4,3,4),
                        new Module(5,2,1),
                        new Module(6,2,1),
                        new Module(7,3,5),
                        new Module(8,1,5),
                        new Module(9,1,5),
                        new Module(10,1,2),
                        new Module(11,2,3),
                        new Module(12,1,5),
                        new Module(13,4,4),
                        new Module(14,2,2)
                )));
         */
        /*
        int MAX = Integer.MAX_VALUE;
        SequencePairs sequencePairsBest = new SequencePairs(new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Integer>(Arrays.asList(5)), new ArrayList<Module>(Arrays.asList(new Module(1,1,1))));
        for (int i = 0 ; i < 10 ; i++) {
            GenerateRectangleDataSet generateRectangleDataSet1 = new GenerateRectangleDataSet(200,200);
            DivideAndConquer divideAndConquer = generateRectangleDataSet1.generateDivideAndConquer();

            SequencePairs sequencePairs = divideAndConquer.calculatePlacement();
            if(divideAndConquer.bestArea < MAX ) {
                MAX = divideAndConquer.bestArea;
                sequencePairsBest = sequencePairs;
            }
        }

         */
        divideAndConquerRunTestSet();
        /*
        SequencePairs testSeq = new SequencePairs(CommonFunctions.randomIntegerList(State.getState().modules.size()),
                CommonFunctions.randomIntegerList(State.getState().modules.size()),
                State.getState().modules);
        */

        //painter.drawBoxesInBin(testSeq.testBin);

        ComboBox<Algorithms> comboBoxAlgorithms = new ComboBox();
        setComboboxAlgorithms(comboBoxAlgorithms);

        ComboBox<Dimension> comboBoxDimensions = new ComboBox();
        setComboBox(comboBoxAlgorithms, comboBoxDimensions);

        
        HBox hBox = new HBox();
        HBox mainHBox = new HBox();
        hBox.setSpacing(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(comboBoxDimensions, comboBoxAlgorithms, showPlot, doSATests);
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

    private static void RunMultiTestVariables(int p1, float p1_Start, float p1_End, int p1_Steps, int p2, float p2_Start,float p2_End, int p2_Steps) throws IOException {
        File testSetFolder = new File("src/TestSet/");
        System.out.println("TestVariables :)");
        int bestDist = Integer.MAX_VALUE;
        Bin2D ourBestBin = new Bin2D();
        int averageAmountRectangles = 0;
        double STDRectangles = 0;
        ArrayList<Integer> rectangles = new ArrayList<>();
        ArrayList<Integer> everySolution = new ArrayList<>();

        for (File testSet : Objects.requireNonNull(testSetFolder.listFiles())) {
            String s = testSet.getName();
            String sa[] = s.split("_");
            rectangles.add(Integer.valueOf(sa[2].replace(".csv","")));
        }
        int sum = 0;
        for ( Integer i : rectangles) {
            sum += i;
        }
        averageAmountRectangles = sum/rectangles.size();
        STDRectangles = CommonFunctions.calculateSD(rectangles);
        String testResult = "optimal is 40000, dataset run on the same 50 generated dataset with avg of " + averageAmountRectangles +" recntangles with std: " + STDRectangles + "\naverage,std,cool,startTemp,minTemp\n";
        //Default values
        int startTemp = 2000000;
        float minTemp = 0.005f;
        float coolingRate = 0.9994f;

        float stepSizeP1= ((p1_End-p1_Start)/p1_Steps);
        float stepSizeP2= ((p2_End-p2_Start)/p2_Steps);
        for (int x= 0 ; x<p2_Steps;x++) {
            if (p2 == 0) { //StartTemp param
                startTemp = (int) (p2_Start + stepSizeP2 * x);
            } else if (p2 == 1) {//MinTemp param
                minTemp = (p2_Start + stepSizeP2 * x);
            } else if (p2 == 2) {//Cooling rate param
                coolingRate = (p2_Start + stepSizeP2 * x);
            }
            for (int j = 0; j < p1_Steps; j++) {
                System.out.println("x,j:" + x+"," +j);

                if (p1 == 0) { //StartTemp param
                    startTemp = (int) (p1_Start + stepSizeP1 * j);
                } else if (p1 == 1) {//MinTemp param
                    minTemp = (p1_Start + stepSizeP1 * j);
                } else if (p1 == 2) {//Cooling rate param
                    coolingRate = (p1_Start + stepSizeP1 * j);
                }
                everySolution=new ArrayList<>();
                for (File testSet : Objects.requireNonNull(testSetFolder.listFiles())) {

                    ArrayList<Simple2DBox> BoxArray = new ArrayList<>();

                    FileReader fileReader = new FileReader(testSet);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while (bufferedReader.ready()) {
                        String s = bufferedReader.readLine();
                        String[] split = s.split(",");

                        BoxArray.add(new Simple2DBox(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
                    }

                    ArrayList<Integer> positive = new ArrayList<>();
                    ArrayList<Integer> negative = new ArrayList<>();
                    ArrayList<Module> modules = new ArrayList<>();

                    for (int i = 0; i < BoxArray.size(); i++) {
                        positive.add(i + 1);
                        negative.add(i + 1);
                        modules.add(new Module(i + 1, BoxArray.get(i).w, BoxArray.get(i).h));
                    }

                    SequencePairs testSeq = new SequencePairs(positive, negative, modules);
                    SimulatedAnnealing sa = new SimulatedAnnealing();

                    testSeq.calculatePlacementTable();
                    sa.simulatedAnnealing(testSeq, startTemp, minTemp, coolingRate);
                    everySolution.add(testSeq.bestDist);

                    if (bestDist > testSeq.bestDist) {
                        bestDist = testSeq.bestDist;
                        ourBestBin= testSeq.bestBin;
                    }
                    //iterationsSA = sa.i;
                }

                //painter.drawBoxesInBin(drawSQ.testBin);
                sum = 0;
                for (Integer i : everySolution) {
                    sum += i;
                }
                int average = sum / everySolution.size();
                double standardDeviation = CommonFunctions.calculateSD(everySolution);
                //average, std, best, cool, start, min temp
                testResult +=average + "," + standardDeviation + "," +
                        coolingRate + "," + startTemp + "," + minTemp +"\n";
                painter.fillBlank();
                painter.drawBoxesInBin(ourBestBin);
                System.out.println("average " + average +  " coolingrate " + coolingRate +
                        " startT: " + startTemp + " minT: " + minTemp);
            }
        }

        CSVWriter.getCsvWriter().createAndWrite("src/Results/", "test_P1"+ p1 + "_P2" + p2 + "_P1V" +  p1_Start + "-" + p1_End + "_P2V" + p2_Start + "-" + p2_End  +"I" + p2_Steps*p1_Steps + "Dual.csv", testResult);
    }
    private static void divideAndConquerRunTestSet() throws IOException {
        File testSetFolder = new File("src/TestSet/");
        int sum = 0;
        for (File testSet : Objects.requireNonNull(testSetFolder.listFiles())) {

            ArrayList<Simple2DBox> BoxArray = new ArrayList<>();

            FileReader fileReader = new FileReader(testSet);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                String[] split = s.split(",");

                BoxArray.add(new Simple2DBox(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
            }
            ArrayList<Integer> positive = new ArrayList<>();
            ArrayList<Integer> negative = new ArrayList<>();
            ArrayList<Module> modules = new ArrayList<>();

            for (int i = 0; i < BoxArray.size(); i++) {
                positive.add(i + 1);
                negative.add(i + 1);
                modules.add(new Module(i + 1, BoxArray.get(i).w, BoxArray.get(i).h));
            }

            DivideAndConquer divideAndConquer = new DivideAndConquer(positive, negative, modules);
            divideAndConquer.calculatePlacement();
            System.out.println(divideAndConquer.bestArea);
            sum += divideAndConquer.bestArea;
        }
        System.out.println(testSetFolder.listFiles().length);
        System.out.println(sum/testSetFolder.listFiles().length);
    }


    private static void RunTestForVariables(int testParameter, float startParam, float endParam, int stepCount) throws IOException {
        File testSetFolder = new File("src/TestSet/");
        int bestDist = Integer.MAX_VALUE;
        int averageAmountRectangles = 0;
        double STDRectangles = 0;
        ArrayList<Integer> rectangles = new ArrayList<>();
        ArrayList<Integer> everySolution = new ArrayList<>();
        String testResult = """
                optimal 40000
                BoxAmount,Solution,startTemp,minTemp,coolRate
                """;
        for (File testSet : Objects.requireNonNull(testSetFolder.listFiles())) {
            String s = testSet.getName();
            String sa[] = s.split("_");
            rectangles.add(Integer.valueOf(sa[2].replace(".csv","")));
        }
        int sum = 0;
        for ( Integer i : rectangles) {
            sum += i;
        }
        averageAmountRectangles = sum/rectangles.size();
        STDRectangles = CommonFunctions.calculateSD(rectangles);

        float stepSize= ((endParam-startParam)/stepCount);
        for (int j =  0 ; j < stepCount ; j++ ) {
            //Default values
            int startTemp = 2000000;
            float minTemp = 0.005f;
            float coolingRate = 0.9994f;

            if(testParameter==0){ //StartTemp param
                startTemp= (int) (startParam+stepSize*j);
            }else if(testParameter==1){//MinTemp param
                minTemp=startParam+stepSize*j;
            }else if(testParameter==2){//Cooling rate param
                coolingRate=startParam+stepSize*j;
            }

            int iterationsSA = 0;

            for (File testSet : Objects.requireNonNull(testSetFolder.listFiles())) {

                ArrayList<Simple2DBox> BoxArray = new ArrayList<>();

                FileReader fileReader = new FileReader(testSet);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while (bufferedReader.ready()) {
                    String s = bufferedReader.readLine();
                    String[] split = s.split(",");

                    BoxArray.add(new Simple2DBox(Integer.parseInt(split[0]),Integer.parseInt(split[1])));
                }
                bufferedReader.close();


                ArrayList<Integer> positive = new ArrayList<>();
                ArrayList<Integer> negative = new ArrayList<>();
                ArrayList<Module> modules = new ArrayList<>();

                for (int i = 0; i < BoxArray.size() ; i ++) {
                    positive.add(i + 1);
                    negative.add(i + 1);
                    modules.add(new Module(i+1, BoxArray.get(i).w, BoxArray.get(i).h));
                }

                SequencePairs testSeq = new SequencePairs(positive, negative, modules);
                SimulatedAnnealing sa = new SimulatedAnnealing();

                testSeq.calculatePlacementTable();
                sa.simulatedAnnealing(testSeq, startTemp, minTemp, coolingRate);
                everySolution.add(testSeq.bestDist);

                if (bestDist > testSeq.bestDist) {
                    bestDist = testSeq.bestDist;
                }
                iterationsSA = sa.i;
            }

            //painter.drawBoxesInBin(drawSQ.testBin);
            sum = 0;
            for (Integer i: everySolution) {
                sum += i;
            }
            int average = sum/everySolution.size();
            double standardDeviation = CommonFunctions.calculateSD(everySolution);
            testResult += iterationsSA + "," + average + "," + standardDeviation + "," +
                    coolingRate + ","+ averageAmountRectangles +","+ STDRectangles +
                    ","+ startTemp +","+ minTemp +","+ coolingRate +"\n";

            System.out.println("average " + average + " standard "
                    + standardDeviation + " best " + bestDist + " coolingrate " + coolingRate +
                    " startT: " + startTemp + " minT: " +minTemp

                    + " averageRect " + averageAmountRectangles + " stdRectangles  " + STDRectangles
            );
        }

        
        //CSVWriter.getCsvWriter().createAndWrite("src/Results/", "test"+".csv", testResult);
        //CSVWriter.getCsvWriter().createAndWrite("src/Results/", "testResults.csv",testResult);
        CSVWriter.getCsvWriter().createAndWrite("src/Results/", "test_P"+ testParameter + "_S" +  startParam + "_E" + endParam +".csv", testResult);
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