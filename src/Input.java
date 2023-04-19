import java.util.LinkedList;

import static java.lang.System.exit;

public class Input {

    protected Integer inputRow, inputCol;
    private LinkedList<String> initialInputs = new LinkedList();
    private LinkedList<String> currentInputs = new LinkedList<>();
    String inputDirection;


    Input(LinkedList inputs){
        this.initialInputs.addAll(inputs);
        this.currentInputs.addAll(initialInputs);
        String temp1 = initialInputs.get(0);
        String[] temp2 = temp1.split(" ");
        this.inputRow = Integer.valueOf(temp2[0]);
        this.inputCol = Integer.valueOf(temp2[1]);
        initialInputs.remove(0);
        this.currentInputs.addAll(initialInputs);

        System.out.println("Input Row is: " + inputRow);
        System.out.println("Input Column is: " + inputCol);
        System.out.println("Initial Input Numbers: " + initialInputs);
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
}
