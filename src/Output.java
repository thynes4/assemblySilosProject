import java.util.LinkedList;

import static java.lang.System.exit;

public class Output {
    private Integer outputRow, outputCol;
    String outputDirection;
    private static Integer siloToGrab;
    private LinkedList<String> outputNumbers = new LinkedList<>();


    Output(Integer row, Integer column){
        this.outputRow = row;
        this.outputCol = column;
        System.out.println("Output Row is: " + outputRow);
        System.out.println("Output Column is: " + outputCol);
    }

    void outputTransferRegion(LinkedList<TransferRegion> transferRegions, LinkedList<Silo> siloList, Integer totalCols, Integer totalRows){
        if (outputRow < 0 && outputCol >=0){
            siloList.get(outputCol).trUp = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "DOWN";
            siloToGrab = outputCol;
            System.out.println("Output being added to silo: " + (outputCol) + " Output Direction: " + outputDirection);
        }
        else if (outputCol < 0 && outputRow >= 0){
            siloList.get((totalCols * (outputRow))).trLeft = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "RIGHT";
            siloToGrab = ((totalCols * (outputRow)));
            System.out.println("Output being added to silo: " + (totalCols * (outputRow)) + " Output Direction: " + outputDirection);
        }
        else if (outputRow >= totalRows && outputCol < totalCols){
            siloList.get((totalCols * (outputRow - 1)) + outputCol).trDown = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "UP";
            siloToGrab = ((totalCols * (outputRow - 1)) + outputCol);
            System.out.println("Output being added to silo: " + ((totalCols * (outputRow - 1)) + outputCol) + " Output Direction: " + outputDirection);
        }
        else if (outputCol >= totalCols && outputRow < totalRows){
            siloList.get((outputRow * totalCols) + (outputCol - 1)).trRight = transferRegions.get((totalCols * totalRows) + 1);
            outputDirection = "LEFT";
            siloToGrab = ((outputRow * totalCols) + (outputCol - 1));
            System.out.println("Output being added to silo: " + ((outputRow * totalCols) + (outputCol - 1)) + " Output Direction: " + outputDirection);
        }
        else {
            System.out.println("Bad Output Coordinates, Closing");
            exit(0);
        }
    }

    void getOutput(LinkedList<TransferRegion> transferRegions){
        String temp = null;
        switch (outputDirection){
            case "UP" -> {
                if (!transferRegions.get(siloToGrab).down.matches(" ")){
                    temp = transferRegions.get(siloToGrab).down;
                    transferRegions.get(siloToGrab).down = " ";
                }
            }
            case "DOWN" -> {
                if (!transferRegions.get(siloToGrab).up.matches(" ")){
                    temp = transferRegions.get(siloToGrab).up;
                    transferRegions.get(siloToGrab).up = " ";
                }
            }
            case "LEFT" -> {
                if (!transferRegions.get(siloToGrab).right.matches(" ")){
                    temp = transferRegions.get(siloToGrab).right;
                    transferRegions.get(siloToGrab).right = " ";
                }
            }
            case "RIGHT" -> {
                if (!transferRegions.get(siloToGrab).left.matches(" ")){
                    temp = transferRegions.get(siloToGrab).left;
                    transferRegions.get(siloToGrab).left = " ";
                }
            }
        }

        System.out.println("Adding value: " + temp + " to output");
        if (temp != null){
            outputNumbers.add(temp);
        }
    }

    LinkedList sendOutputList(){
        return outputNumbers;
    }
}
