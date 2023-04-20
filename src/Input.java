import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

import static java.lang.System.exit;

public class Input {

    protected Integer inputRow, inputCol;
    protected LinkedList<String> initialInputs = new LinkedList();
    private LinkedList<String> currentInputs = new LinkedList<>();
    String inputDirection;
    private int currentInputLine;
    private Label inputLabel = new Label();


    Input(LinkedList inputs){
        this.currentInputLine = 0;
        this.initialInputs.addAll(inputs);
        String temp1 = initialInputs.get(0);
        String[] temp2 = temp1.split(" ");
        this.inputRow = Integer.valueOf(temp2[0]);
        this.inputCol = Integer.valueOf(temp2[1]);
        initialInputs.remove(0);
        this.currentInputs.addAll(initialInputs);

        System.out.println("Input Row is: " + inputRow);
        System.out.println("Input Column is: " + inputCol);
        System.out.println("Initial Input Numbers: " + initialInputs);
        System.out.println("Current Input Numbers: " + currentInputs);
    }

    void inputTransferRegion(LinkedList<TransferRegion> transferRegions, LinkedList<Silo> siloList, Integer totalCols, Integer totalRows){
        if (inputRow < 0 && inputCol>= 0){
            siloList.get((inputCol)).trUp = transferRegions.get((totalCols * totalRows));
            inputDirection = "DOWN";
            System.out.println("Input being added to silo: " + inputCol + " input Direction: " + inputDirection);
        }
        else if (inputCol < 0 && inputRow >= 0){
            siloList.get((totalCols * (inputRow))).trLeft = transferRegions.get((totalCols * totalRows));
            inputDirection = "RIGHT";
            System.out.println("Input being added to silo: " + (totalCols * (inputRow)) + " input Direction: " + inputDirection);
        }
        else if (inputRow >= totalRows && inputCol < totalCols){
            siloList.get((totalCols * (inputRow - 1)) + inputCol).trDown = transferRegions.get((totalCols * totalRows));
            inputDirection = "UP";
            System.out.println("Input being added to silo: " + ((totalCols * (inputRow - 1)) + inputCol) + " input Direction: " + inputDirection);
        }
        else if (inputCol >= totalCols && inputRow < totalRows){
            siloList.get((inputRow * totalCols) + (inputCol - 1)).trRight = transferRegions.get((totalCols * totalRows));
            inputDirection = "LEFT";
            System.out.println("Input being added to silo: " + ((inputRow * totalCols) + (inputCol - 1)) + " input Direction: " + inputDirection);
        }
        else {
            System.out.println("Bad Input Coordinates, Closing");
            exit(0);
        }
    }

    String sendInputValue(){
        currentInputLine++;
        if (currentInputLine >= currentInputs.size()){
            return null;
        }
        else {
            System.out.println("Sending Input value: " + currentInputs.get(currentInputLine - 1));
            return currentInputs.get(currentInputLine - 1);
        }
    }

    void setInputLbl(GridPane root, int posX, int posY){
        if(inputDirection.equals("DOWN")){
            inputLabel.setText("  ⬇");
        }else if(inputDirection.equals("UP")){
            inputLabel.setText("⬆  ");
        }else if(inputDirection.equals("LEFT")){
            inputLabel.setText("\n⬅");
        }else if(inputDirection.equals("RIGHT")) {
            inputLabel.setText("➡\n");
        }

        root.add(inputLabel, posX, posY);
    }
    void updateInputFX(){
        if(currentInputs.get(currentInputLine) == null){inputLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{inputLabel.setStyle("-fx-text-fill: #a1b56c;");}
    }
}
