import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

import static java.lang.System.exit;

public class Input {

    protected Integer inputRow, inputCol;
    protected LinkedList<String> initialInputs = new LinkedList();
    private LinkedList<String> currentInputs = new LinkedList<>();
    protected String inputDirection;
    private int currentInputLine;
    private Label inputLabel = new Label();


    /**
     * This is the Input Class
     * @param inputs The input values being given to this input
     */
    Input(LinkedList inputs){
        this.currentInputLine = 0;
        this.initialInputs.addAll(inputs);
        String temp1 = initialInputs.get(0);
        String[] temp2 = temp1.split(" ");
        this.inputRow = Integer.valueOf(temp2[0]);
        this.inputCol = Integer.valueOf(temp2[1]);
        initialInputs.remove(0);
        this.currentInputs.addAll(initialInputs);

    }

    /**
     * This is to put the input transfer region in the correct area
     * @param transferRegions The list of transfer regions
     * @param siloList The list of silos
     * @param totalCols The total columns of the array
     * @param totalRows The total rows of the array
     */
    void inputTransferRegion(LinkedList<TransferRegion> transferRegions,
                             LinkedList<Silo> siloList, Integer totalCols, Integer totalRows){
        if (inputRow < 0 && inputCol>= 0){
            siloList.get((inputCol)).trUp = transferRegions.get((totalCols * totalRows));
            inputDirection = "DOWN";
        }
        else if (inputCol < 0 && inputRow >= 0){
            siloList.get((totalCols * (inputRow))).trLeft =
                    transferRegions.get((totalCols * totalRows));
            inputDirection = "RIGHT";
        }
        else if (inputRow >= totalRows && inputCol < totalCols){
            siloList.get((totalCols * (inputRow - 1)) + inputCol).trDown =
                    transferRegions.get((totalCols * totalRows));
            inputDirection = "UP";
        }
        else if (inputCol >= totalCols && inputRow < totalRows){
            siloList.get((inputRow * totalCols) + (inputCol - 1)).trRight =
                    transferRegions.get((totalCols * totalRows));
            inputDirection = "LEFT";
        }
        else {
            System.out.println("Bad Input Coordinates, Closing");
            exit(0);
        }
    }

    /**
     * This is to send an input value to the transfer region
     * @return
     */
    String sendInputValue(){
        currentInputLine++;
        if (currentInputLine >= currentInputs.size()){
            return null;
        }
        else {
            return currentInputs.get(currentInputLine - 1);
        }
    }

    /**
     * THis is to set the input label
     * @param root The java fx root
     * @param posX The x position for the label
     * @param posY The y position for the label
     */
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

    /**
     * This is to update the FX for the input
     */
    void updateInputFX(){
        if(currentInputs.get(currentInputLine) == null){inputLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{inputLabel.setStyle("-fx-text-fill: #a1b56c;");}
    }
}
