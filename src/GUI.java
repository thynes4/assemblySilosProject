import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 *
 * This file is the GUI file which will display and allow the User to run the program.
 */

public class GUI extends Application {
    static LinkedList<TransferRegion> transferRegions = new LinkedList<>();
    static LinkedList<Silo> siloList = new LinkedList<>();
    static Integer totalRows, totalColumns;
    static Integer outputTR, inputTR;
    static String inputDirection, outputDirection;

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
        while (!inputFinished){
            temp1 = sc.nextLine();
            if (temp1.equals("END")){
                count++;
            }
            commandInput.add(temp1);
            //By checking the Initial row and column information can see how many
            //ENDs should be written to prevent further input.
            if (count == totalEnds){
                inputFinished = true;
            }
        }

        //Creating all the transfer regions. It is row X Column + 2.
        //The + 2 is the input and output.
        for (int i = 0; i < totalEnds; i++){
            transferRegions.add(new TransferRegion());
        }

        //Transfer Region row x column is input
        inputTR = (totalRows * totalColumns);
        //Transfer Region row x column + 1 is output
        outputTR = (totalRows * totalColumns) + 1;

        //Creating the parser will create all silos too.
        Parser parser = new Parser(commandInput, transferRegions);

        //Grabbing all Silos
        siloList = parser.sendSilos();

        inputDirection = parser.getInputDirection();
        outputDirection = parser.getOutputDirection();

        //This is just some Test Code
        //Making sure Silos get proper data
        //Also making sure Input sends properly
        System.out.println(siloList.size());
        System.out.println(transferRegions.size());
        updateInput(inputDirection,parser);
        System.out.println(transferRegions.get(inputTR).getDown());
        //Remove Above when finished

        launch(args);
    }


    public void start(Stage primaryStage){
        primaryStage.setTitle("Testing...");

        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root, 1024, 768));

        root.setPadding(new Insets(25, 25, 25, 25));
        root.setAlignment(Pos.CENTER);


        for (int i = 0; i < (totalRows * totalColumns); i++){
            BorderPane tRegionNode = transferRegions.get(i).getNode();
            Node siloNode = siloList.get(i).getNode();
            tRegionNode.setCenter(siloNode);
            root.add(tRegionNode, siloList.get(i).getPosY(totalColumns), siloList.get(i).getPosX(totalColumns));
        }


        root.getStylesheets().add("Style.css");
        primaryStage.show();
    }

    static void updateInput(String inputDirection, Parser p){
        String temp = p.sendInput();
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
    }

}
