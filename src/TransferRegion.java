import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;

/**
 * Sean Davies, Thomas Hynes, Christopher Jarek
 * Project 4 Assembly Silos
 * This is to build Assembly Silos that take in their own code. An input is
 * sent in and the silos process their code together 1 line at a time (until all silos are
 * done with that line) then continue on until an output is achieved.
 *
 * This file is the Transfer Region Class
 */

public class TransferRegion {
    protected String up, left, right, down;
    private Label upLabel = new Label();
    private Label leftLabel = new Label();
    private Label rightLabel = new Label();
    private Label downLabel = new Label();

    TransferRegion(){
        this.up = " ";
        this.left = " ";
        this.right = " ";
        this.down = " ";
    }

    /**
     * This will grab the up arrow of the transfer region, return it and clear it.
     * @return The data in the up arrow
     */
    String getUp(){
        if (this.up.matches(" ")){
            return null;
        }
        else {
            String temp = this.up;
            this.up = " ";
            return temp;
        }
    }

    /**
     * This will grab the left arrow of the transfer region, return it and clear it.
     * @return The data in the left arrow
     */
    String getLeft(){
        if (this.left.matches(" ")){
            return null;
        }
        else {
            String temp = this.left;
            this.left = " ";
            return temp;
        }
    }

    /**
     * This will grab the right arrow of the transfer region, return it and clear it.
     * @return the data of the right arrow
     */
    String getRight(){
        if (this.right.matches(" ")){
            return null;
        }
        else {
            String temp = this.right;
            this.right = " ";
            return temp;
        }
    }

    /**
     * This will grab the down arrow of the transfer region, return it and clear it.
     * @return The data in the down arrow
     */
    String getDown(){
        if (this.down.matches(" ")){
            return null;
        }
        else {
            String temp = this.down;
            this.down = " ";
            return temp;
        }
    }

    void addUp(String s){
        this.up = s;
    }
    void addLeft(String s){
        this.left = s;
    }
    void addRight(String s){
        this.right = s;
    }
    void addDown(String s){
        this.down = s;
    }

    BorderPane getNode(){
        BorderPane outerPane = new BorderPane();

        upLabel.setAlignment(Pos.CENTER);
        outerPane.setTop(upLabel);
        outerPane.setAlignment(upLabel, Pos.CENTER);

        leftLabel.setAlignment(Pos.CENTER);
        outerPane.setLeft(leftLabel);
        outerPane.setAlignment(leftLabel, Pos.CENTER);

        rightLabel.setAlignment(Pos.CENTER);
        outerPane.setRight(rightLabel);
        outerPane.setAlignment(rightLabel, Pos.CENTER);

        downLabel.setAlignment(Pos.CENTER);
        outerPane.setBottom(downLabel);
        outerPane.setAlignment(downLabel, Pos.CENTER);

        refreshFX();
        return outerPane;
    }

    void refreshFX(){
        String upText = "⬆  " + up;
        upLabel.setText(upText);
        if(up.equals("")){upLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{upLabel.setStyle("-fx-text-fill: #a1b56c;");}

        String leftText = left + "\n⬅";
        leftLabel.setText(leftText);
        if(left.equals("")){leftLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{leftLabel.setStyle("-fx-text-fill: #a1b56c;");}


        String rightText = "➡\n" + right;
        rightLabel.setText(rightText);
        if(right.equals("")){rightLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{rightLabel.setStyle("-fx-text-fill: #a1b56c;");}


        String downText = down + "  ⬇";
        downLabel.setText(downText);
        if(right.equals("")){downLabel.setStyle("-fx-text-fill: #dc9656;");}
        else{downLabel.setStyle("-fx-text-fill: #a1b56c;");}

    }
}
