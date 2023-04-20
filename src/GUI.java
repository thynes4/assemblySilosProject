import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 * This file is the GUI file which will display and allow the User to run the program.
 */

public class GUI extends Application{
    static LinkedList<TransferRegion> transferRegions = new LinkedList<>();
    static LinkedList<Silo> siloList = new LinkedList<>();
    static Integer totalRows, totalColumns;
    static Integer outputTR;
    static String inputDirection, outputDirection;
    boolean step = true;
    AnimationTimer a = null;
    boolean paused = false;
    static LinkedList<String> outputValues = new LinkedList<>();
    static TextArea output = new TextArea();
    static LinkedList<Parser> parserList = new LinkedList<>();
    static Integer inputTotal = 0;
    static LinkedList<Input> inputList = new LinkedList<>();
    static LinkedList<Output> outputList = new LinkedList<>();

    /**
     * The main method for the entire Program. It grabs the users input
     * from the command line, sends to parser
     * for initial parsing and reports back errors that can be fixed later
     * @param args
     */
    public static void main(String[] args) {
        LinkedList<String> commandInput = new LinkedList<>();
        boolean inputFinished = false;

        Scanner sc = new Scanner(System.in);
        String temp1 = sc.nextLine();
        commandInput.add(temp1);
        String[] temp2 = temp1.split(" ");
        int var1 = Integer.parseInt(temp2[0]);
        totalRows = var1;
        int var2 = Integer.parseInt(temp2[1]);
        totalColumns = var2;
        int totalEnds = (var1 * var2) + 2;
        int count = 0;
        while (!inputFinished) {
            temp1 = sc.nextLine();
            if (temp1.equals("END")) {
                count++;
            }
            //In case more than 1 input.
            if (temp1.contains("INPUT")){
                inputTotal++;
                totalEnds = (var1 * var2) + (inputTotal * 2);
            }
            commandInput.add(temp1);
            //By checking the Initial row and column information can see how many
            //ENDs should be written to prevent further input.
            if (count == totalEnds) {
                inputFinished = true;
            }
        }

        //Creating all the transfer regions. It is row X Column. Last value/values are the outputs
        for (int i = 0; i < totalEnds; i++) {
            transferRegions.add(new TransferRegion());
        }

        //Transfer Region row x column + 1 is output
        outputTR = (totalRows * totalColumns) + 1;

        //Creating the parser will create all silos too.
        Parser parser = new Parser(commandInput, transferRegions,inputTotal);
        parserList.add(parser);

        //Grabbing all Silos
        siloList = parser.sendSilos();

        inputDirection = parser.getInputDirection();
        outputDirection = parser.getOutputDirection();

        inputList.addAll(parserList.get(0).sendInputList());
        outputList.addAll(parserList.get(0).sendOutputList());

        //update input
        for (int i = 0; i < inputTotal; i++){
            updateInput(inputList.get(i).inputDirection,inputList.get(i),i);
        }

        launch(args);

    }

    /**
     * To display the stage for the GUI
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws InterruptedException
     */
    public void start(Stage primaryStage) throws InterruptedException {

        primaryStage.setTitle("Project 4 Assembly Silos");

        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root));

        root.setHgap(16);
        root.setVgap(16);
        root.setAlignment(Pos.CENTER);

        mkCtrlPanel(root);

        //Creates the fx for Silos
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);

        for (int i = 0; i < (totalRows * totalColumns); i++){
            BorderPane tRegionNode = transferRegions.get(i).getNode();
            Node siloNode = siloList.get(i).getNode();
            tRegionNode.setCenter(siloNode);
            root.add(tRegionNode, siloList.get(i).getPosY(totalColumns),
                    siloList.get(i).getPosX(totalColumns));

        }

        root.getStylesheets().add("Style.css");
        primaryStage.show();

        // This is what I use to run each of the threads
        ExecutorService siloExecutor = Executors.newFixedThreadPool(siloList.size());
        ExecutorService transferExecutor = Executors.newFixedThreadPool(transferRegions.size());

        /**
         * Animation timer that executes the silo threads and waits until they are all finished,
         * executes the transfer regions also and continuously updates the gui.
         */
        a = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= TimeUnit.SECONDS.toNanos(2)) {

                    //submit all tasks to thread pool
                    for (Silo s : siloList) {
                        siloExecutor.execute(s);
                    }

                    //waits for threads to finish before continuing
                    try {
                        siloExecutor.awaitTermination(3,TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    for (Silo s : siloList) {
                        s.resetFinished();
                    }

                    //REFRESH THE JAVA FX HERE

                    if (step) {
                        a.stop();
                    }
                    lastUpdate = now;
                }

                for (TransferRegion t : transferRegions) {
                    transferExecutor.execute(t);
                }

                for (int i = 0; i < inputTotal; i++){
                    updateInput(inputList.get(i).inputDirection,inputList.get(i),i);
                    outputList.get(i).getOutput(transferRegions);
                }

                for (Silo s : siloList){
                    s.refreshFX();
                }
                for(TransferRegion t : transferRegions){
                    t.refreshFX();
                }

                setOutput();
            }
        };

    }

    /**
     * THis is to update the input transfer region area
     * @param inputDirection The input direction
     * @param input The input class
     * @param inputNumber The input number
     */
    static void updateInput(String inputDirection, Input input, Integer inputNumber){
        String temp = null;
        int inputTR = (totalRows * totalColumns) + inputNumber;
        switch (inputDirection){
            case "UP" -> {
                if (transferRegions.get(inputTR).up.matches(" ")){
                    temp = input.sendInputValue();
                }
            }
            case "DOWN" -> {
                if (transferRegions.get(inputTR).down.matches(" ")){
                    temp = input.sendInputValue();
                }
            }
            case "LEFT" -> {
                if (transferRegions.get(inputTR).left.matches(" ")){
                    temp = input.sendInputValue();
                }
            }
            case "RIGHT" -> {
                if (transferRegions.get(inputTR).right.matches(" ")){
                    temp = input.sendInputValue();
                }
            }
        }
        if (temp != null) {
            switch (inputDirection) {
                case "UP" -> {
                    if (transferRegions.get(inputTR).up.matches(" ")) {
                        transferRegions.get(inputTR).addUp(temp);
                    }
                }
                case "DOWN" -> {
                    if (transferRegions.get(inputTR).down.matches(" ")) {
                        transferRegions.get(inputTR).addDown(temp);
                    }
                }
                case "LEFT" -> {
                    if (transferRegions.get(inputTR).left.matches(" ")) {
                        transferRegions.get(inputTR).addLeft(temp);
                    }
                }
                case "RIGHT" -> {
                    if (transferRegions.get(inputTR).right.matches(" ")) {
                        transferRegions.get(inputTR).addRight(temp);
                    }
                }
            }
        }
        else {
        }
    }


    /**
     * THis is to make the control Panel
     * @param root The main root for the GUI
     */
    void mkCtrlPanel(GridPane root){
        GridPane ctrlPanel = new GridPane();
        ctrlPanel.setHgap(8);
        ctrlPanel.setVgap(8);
        root.add(ctrlPanel, 0, 0, 1, GridPane.REMAINING);
        root.getColumnConstraints().add(new ColumnConstraints(256));

        Label inputLbl = new Label();
        inputLbl.setText("Input");
        ctrlPanel.add(inputLbl, 0, 0);

        Label outputLbl = new Label();
        outputLbl.setText("Output");
        ctrlPanel.add(outputLbl, 1, 0);

        TextArea input = new TextArea();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < inputTotal; i++) {
            for (String s : inputList.get(i).initialInputs) {
                temp.append(s).append("\n");
            }
        }
        input.setText(temp.toString());
        ctrlPanel.add(input, 0, 1);
        input.setPrefSize(128, 512);

        ctrlPanel.add(output, 1, 1);
        output.setPrefSize(128, 512);

        input.setEditable(false);
        output.setEditable(false);

        Button startBtn = new Button();
        startBtn.setText("Start");
        startBtn.setOnAction(event -> {
            startButton();
        });
        ctrlPanel.add(startBtn, 0, 2);

        Button pauseBtn = new Button();
        pauseBtn.setText("Pause");
        pauseBtn.setOnAction(event -> {
            if (!paused){
                pauseBtn.setText("Resume");
                paused = true;
                pauseButton();
            }
            else {
                pauseBtn.setText("Pause");
                paused = false;
                pauseButton();
            }
        });
        ctrlPanel.add(pauseBtn, 1, 2);

        Button stepBtn = new Button();
        stepBtn.setText("Step");
        stepBtn.setOnAction(event -> {
            stepButton();
        });
        ctrlPanel.add(stepBtn, 0, 3);

        Button stopBtn = new Button();
        stopBtn.setText("Stop");
        stopBtn.setOnAction(event -> {
            stopButton();
        });
        ctrlPanel.add(stopBtn, 1, 3);
    }

    //Methods for when buttons are pushed

    /**
     * THis is the start button action when pressed
     */
    void startButton(){
        if (parserList.get(0).parseSiloErrors(siloList) == 0){
            for (int i = 0; i < totalRows * totalColumns; i++){
                siloList.get(i).addRunnableList(parserList.get(0).codeToRunnable(siloList.get(i).siloCode,i));
            }
            step = false;
            a.start();
            for (Silo s: siloList){
                s.textEditable = false;
                s.refreshFX();
            }
            for(TransferRegion t : transferRegions){
                t.refreshFX();
            }
            setOutput();
        }
    }

    /**
     * This is the pause button action when pressed.
     */
    void pauseButton(){
        if (paused) {
            a.stop();
        }
        else {
            a.start();
        }
    }

    /**
     * Puts animation timer in step mode and only run a single step
     * of the code.
     */
    void stepButton(){
        a.stop();
        step = true;
        a.start();
    }

    /**
     * This is the stop button action when pressed
     */
    void stopButton(){
        a.stop();
    }

    /**
     * This is to set the output in the output region
     */
    void setOutput(){
        StringBuilder temp1 = new StringBuilder();
        for (int i = 0; i < inputTotal; i++){
            outputValues.addAll(outputList.get(i).sendOutputList());
            for (String s : outputValues){
                temp1.append(s).append("\n");
            }
        }
        output.setText(temp1.toString());
        outputValues.clear();
    }
}

