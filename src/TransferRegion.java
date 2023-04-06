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
    private String up, left, right, down;

    TransferRegion(){
        this.up = " ";
        this.left = " ";
        this.right = " ";
        this.down = " ";
    }

    String up(){
        if (this.up.matches(" ")){
            return null;
        }
        else {
            String temp = this.up;
            this.up = " ";
            return temp;
        }
    }

    String left(){
        if (this.left.matches(" ")){
            return null;
        }
        else {
            String temp = this.left;
            this.left = " ";
            return temp;
        }
    }

    String right(){
        if (this.right.matches(" ")){
            return null;
        }
        else {
            String temp = this.right;
            this.right = " ";
            return temp;
        }
    }

    String down(){
        if (this.down.matches(" ")){
            return null;
        }
        else {
            String temp = this.down;
            this.down = " ";
            return temp;
        }
    }
}
