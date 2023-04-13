import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;


import static javafx.application.Application.launch;

public class Test extends Application {
    private static void main(String[] args){launch(args);}

    public void start(Stage primaryStage){
        primaryStage.setTitle("Testing...");

        GridPane root = new GridPane();
        primaryStage.setScene(new Scene(root, 1024, 768));

        root.setPadding(new Insets(25, 25, 25, 25));
        root.setAlignment(Pos.CENTER);

        int totalRows = 3;
        int totalColumns = 3;


        ArrayList<TransferRegion> TRegions = new ArrayList<>();
        ArrayList<Silo> Silos = new ArrayList<>();
        for(int i = 0; i < totalRows * totalColumns; i++){
            TransferRegion tRegion = new TransferRegion();
            TRegions.add(tRegion);

            BorderPane tRegionNode = tRegion.getNode();

            Silo silo = new Silo(i);
            Silos.add(silo);

            silo.setSiloCode("["+ Integer.toString(i) + "]");

            Node siloNode = silo.getNode();

            tRegionNode.setCenter(siloNode);

            root.add(tRegionNode, silo.getPosY(totalColumns), silo.getPosX(totalColumns));
        }

        for(Silo silo : Silos){
            System.out.println(silo.returnAllData());
        }

        root.getStylesheets().add("Style.css");
        primaryStage.show();
    }
}
