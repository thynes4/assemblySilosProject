import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.geometry.Insets;
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


        ArrayList<Silo> Silos = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Silo silo = new Silo(i);
            Silos.add(silo);

            Node siloNode = silo.getNode();
            root.add(siloNode, silo.getPosY(), silo.getPosX());
        }

        for(Silo silo : Silos){
            System.out.println(silo.returnAllData());
        }

        primaryStage.show();
    }
}
