import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
 *
 * This file is the GUI file which will display and allow the User to run the program.
 */

public class GUI extends Application{
    static LinkedList<TransferRegion> transferRegions = new LinkedList<>();
    static LinkedList<Silo> siloList = new LinkedList<>();
    static Integer totalRows, totalColumns;
    static Integer outputTR, inputTR;
    static String inputDirection, outputDirection;
    boolean step = true;
    AnimationTimer a = null;
    boolean paused = false;
    static LinkedList<String> inputList = new LinkedList<>();
    static LinkedList<String> outputList = new LinkedList<>();
    static TextArea output = new TextArea();

    static LinkedList<Parser> parserList = new LinkedList<>();

    public static void main(String[] args) {
        LinkedList<String> commandInput = new LinkedList<>();
        boolean inputFinished = false;

        Scanner sc = new Scanner(System.in);
        String temp1 = sc.nextLine();
        commandInput.add(temp1);
        String[] temp2 = temp1.split(" ");
        int var1 = Integer.valueOf(temp2[0]);
        totalRows = var1;
        int var2 = Integer.valueOf(temp2[1]);
        totalColumns = var2;
        int totalEnds = (var1 * var2) + 2;
        int count = 0;
        while (!inputFinished) {
            temp1 = sc.nextLine();
            if (temp1.equals("END")) {
                count++;
            }
            commandInput.add(temp1);
            //By checking the Initial row and column information can see how many
            //ENDs should be written to prevent further input.
            if (count == totalEnds) {
                inputFinished = true;
            }
        }

        //Creating all the transfer regions. It is row X Column + 2.
        //The + 2 is the input and output.
        for (int i = 0; i < totalEnds; i++) {
            transferRegions.add(new TransferRegion());
        }

        //Transfer Region row x column is input
        inputTR = (totalRows * totalColumns);
        //Transfer Region row x column + 1 is output
        outputTR = (totalRows * totalColumns) + 1;

        //Creating the parser will create all silos too.
        Parser parser = new Parser(commandInput, transferRegions);
        parserList.add(parser);

        //Grabbing all Silos
        siloList = parser.sendSilos();

        inputList.addAll(parser.sendInputList());

        inputDirection = parser.getInputDirection();
        outputDirection = parser.getOutputDirection();

        updateInput(inputDirection, parser);

        System.out.println("Value in input: " + transferRegions.get((totalColumns * totalRows)).down);;
        int tempcount = 0;
        for (Silo s: siloList) {
            System.out.println("Silo: " + tempcount);
            System.out.println("TR UP: " + s.trUp);
            System.out.println("TR Down: " + s.trDown);
            System.out.println("TR Left: " + s.trLeft);
            System.out.println("TR Right: " + s.trRight);
            tempcount++;
        }

        launch(args);

    }

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
            root.add(tRegionNode, siloList.get(i).getPosY(totalColumns), siloList.get(i).getPosX(totalColumns));

            root.getRowConstraints().add(rowConstraints);
            root.getColumnConstraints().add(columnConstraints);
        }

        root.getStylesheets().add("Style.css");
        primaryStage.show();

        // This is what I use to run each of the threads
        ExecutorService siloExecutor = Executors.newFixedThreadPool(siloList.size());

        // Animation timer that will be used when start button is pressed
        a = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= TimeUnit.SECONDS.toNanos(2)) {
                    if (step)
                    {
                        for (Silo s : siloList) {
                            siloExecutor.execute(s);
                        }
                        a.stop();
                    }
                    //submit all tasks to thread pool
                    for (Silo s : siloList) {
                        siloExecutor.execute(s);
                    }

                    updateInput(inputDirection, parserList.get(0));
                    parserList.get(0).getOutput(transferRegions);

                    //REFRESH THE JAVA FX HERE
                    lastUpdate = now;
                }
                for (Silo s : siloList){
                    s.refreshFX();
                }
                outputList = parserList.get(0).sendOutputList();
                String temp = "";
                for (String s : outputList) {
                    temp = temp + s + "\n";
                }
                output.setText(temp);
            }
        };

        //code to shut down executor service when done
//        executor.shutdown();
//        try {
//            executor.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
    }

    static void updateInput(String inputDirection, Parser p){
        String temp = null;
        switch (inputDirection){
            case "UP" -> {
                if (transferRegions.get(totalColumns*totalRows).up.matches(" ")){
                    temp = p.sendInput();
                }
            }
            case "DOWN" -> {
                if (transferRegions.get(totalColumns*totalRows).down.matches(" ")){
                    temp = p.sendInput();
                }
            }
            case "LEFT" -> {
                if (transferRegions.get(totalColumns*totalRows).left.matches(" ")){
                    temp = p.sendInput();
                }
            }
            case "RIGHT" -> {
                if (transferRegions.get(totalColumns*totalRows).right.matches(" ")){
                    temp = p.sendInput();
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
                    //System.out.println("adding: " + temp + "to down location");
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
            System.out.println("No Input Value Grabbed");
        }
    }


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
        String temp = "";
        for (String s : inputList){
            temp = temp + s + "\n";
        }
        input.setText(temp);
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
            for (Silo s: siloList){
                s.textEditable = false;
                s.refreshFX();
            }
        });
        ctrlPanel.add(startBtn, 0, 2);

        Button pauseBtn = new Button();
        pauseBtn.setText("Pause");
        pauseBtn.setOnAction(event -> {
            if (paused == false){
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
    void startButton(){
        step = false;
        a.start();
    }
    void pauseButton(){
        if (paused) {
            a.stop();
        }
        else {
            a.start();
        }
    }
    void stepButton(){
        a.stop();
        step = true;
        a.start();
    }
    void stopButton(){
        a.stop();
    }
}
