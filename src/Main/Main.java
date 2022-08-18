package Main;

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**Main Method*/
public class Main extends Application {

    /**Start method. This is the home screen for the application*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenu.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /** Generic test parts and products */
    public static void main(String[] args){
        Part part1 = new InHouse(1, "Wheel", 29.99, 4, 1, 10, 33);
        Part part2 = new Outsourced(2, "Tire", 32.99, 6, 1, 10, "Goodyear");
        Part part3 = new InHouse(3, "Paddle", 19.99, 3, 2, 10, 8);
        Part part4 = new Outsourced(4, "Light Bulb", 2.99, 4, 1, 10, "General Electric");

        Product product1 = new Product(1, "Bike", 110.99, 5, 2, 10);
        Product product2 = new Product(2, "Scooter", 179.98, 4, 2, 10);
        Product product3 = new Product(3, "Lamp", 26.50, 3, 2, 10);
        Product product4 = new Product(4, "Paddle Board", 59.99, 6, 2, 10);

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);



            launch(args);
    }

}
