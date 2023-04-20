import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

import static java.lang.System.exit;

public class Output {
    private Integer outputRow, outputCol;
    protected String outputDirection;
    private static Integer siloToGrab;
    private LinkedList<String> outputNumbers = new LinkedList<>();
    private Label outputLabel = new Label();


    /**
     * This is the output Class
     * @param row The row for the output
     * @param column The column for the output
     */
    Output(Integer row, Integer column){
        this.outputRow = row;
        this.outputCol = column;
    }

    /**
     * This is to put in the output transfer region into the array
     * @param transferRegions The transfer region list
     * @param siloList The silo list
     * @param totalCols The total columns of the array
     * @param totalRows The total rows of the array
     * @param totalInputs The total inputs
     * @param outputNum The output number
     */
    void outputTransferRegion(LinkedList<TransferRegion> transferRegions,
                              LinkedList<Silo> siloList, Integer totalCols,
                              Integer totalRows, Integer totalInputs, Integer outputNum){
        if (outputRow < 0 && outputCol >=0){
            siloList.get(outputCol).trUp =
                    transferRegions.get(((totalCols * totalRows) + totalInputs) + outputNum);
            outputDirection = "DOWN";
            siloToGrab = outputCol;
        }
        else if (outputCol < 0 && outputRow >= 0){
            siloList.get((totalCols * (outputRow))).trLeft =
                    transferRegions.get(((totalCols * totalRows) + totalInputs) + outputNum);
            outputDirection = "RIGHT";
            siloToGrab = ((totalCols * (outputRow)));
        }
        else if (outputRow >= totalRows && outputCol < totalCols){
            siloList.get((totalCols * (outputRow - 1)) + outputCol).trDown =
                    transferRegions.get(((totalCols * totalRows) + totalInputs) + outputNum);
            outputDirection = "UP";
            siloToGrab = ((totalCols * (outputRow - 1)) + outputCol);
        }
        else if (outputCol >= totalCols && outputRow < totalRows){
            siloList.get((outputRow * totalCols) + (outputCol - 1)).trRight =
                    transferRegions.get(((totalCols * totalRows) + totalInputs) + outputNum);
            outputDirection = "LEFT";
            siloToGrab = ((outputRow * totalCols) + (outputCol - 1));
        }
        else {
            System.out.println("Bad Output Coordinates, Closing");
            exit(0);
        }
    }

    /**
     * This is to get a value from the output region
     * @param transferRegions The list of transfer regions
     */
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

        if (temp != null){
            outputNumbers.add(temp);
        }
    }

    /**
     * This is to send the list of outputs received
     * @return The list of outputs received
     */
    LinkedList sendOutputList(){
        return outputNumbers;
    }

    /**
     * This is to set the output label
     * @param root The root of the Java FX
     * @param posX The X position
     * @param posY The Y position
     */
    void setOutputLabel(GridPane root, int posX, int posY){
        if(outputDirection.equals("DOWN")){
            outputLabel.setText("  ⬇");
        }else if(outputDirection.equals("UP")){
            outputLabel.setText("⬆  ");
        }else if(outputDirection.equals("LEFT")){
            outputLabel.setText("\n⬅");
        }else if(outputDirection.equals("RIGHT")) {
            outputLabel.setText("➡\n");
        }

        root.add(outputLabel, posX, posY);
    }

    /**
     * This is to update the output label
     */
    void updateInputFX(){
        if(outputNumbers.get(0) == null){outputLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{outputLabel.setStyle("-fx-text-fill: #a1b56c;");}
    }
}
