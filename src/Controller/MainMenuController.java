package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Main Menu Controller contains the Parts and Products tableview with add and delete functions*/
public class MainMenuController implements Initializable {

    /**Stage*/
    Stage stage;
    /**Scene*/
    Parent scene;


    /**Search Parts Text*/
    @FXML
    private TextField searchPartsText;

    /**Main Menu Table Vioew*/
    @FXML
    private TableView<Part> MainMenuPartsTableView;

    /**Part ID Column*/
    @FXML
    private TableColumn<Part, Integer> colPartID;

    /**Part Name Column*/
    @FXML
    private TableColumn<Part, String> colPartName;

    /**Part Inventory Column*/
    @FXML
    private TableColumn<Part, Integer> colPartInventoryLevel;

    /**Price Column*/
    @FXML
    private TableColumn<Part, Double> colPartPrice;

    /**Add Part Button*/
    @FXML
    private Button addPartButton;

    /**Modify Part Button*/
    @FXML
    private Button modifyPartButton;

    /**Delete Part Button*/
    @FXML
    private Button deletePartButton;

    /**Search Products Text*/
    @FXML
    private TextField searchProductsTxt;

    /**Products Table View*/
    @FXML
    private TableView<Product> MainMenuProductsTableView;

    /**Product ID Column*/
    @FXML
    private TableColumn<Product, Integer> colProductID;

    /**Product Name Column*/
    @FXML
    private TableColumn<Product, String> colProductName;

    /**Product Inventory Level*/
    @FXML
    private TableColumn<Product, Integer> colProductInventoryLevel;

    /**Product Price Column*/
    @FXML
    private TableColumn<Product, Double> colProductPrice;

    /**Add Product Button*/
    @FXML
    private Button addProductButton;

    /**Modify Product Button*/
    @FXML
    private Button modifyProductButton;

    /**Delete Product Button*/
    @FXML
    private Button deleteProductButton;

    /**Exit Button*/
    @FXML
    private Button exitButton;


    /**Add Part Event. Takes you to the Add Part Screen*/
    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Add Product Event. Takes you to the Add Product Screen*/
    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Delete Part Event. Deleted selected part after alert confirmation.*/
    @FXML
    void onActionDeletePart(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part will be deleted. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Part selectedPart = MainMenuPartsTableView.getSelectionModel().getSelectedItem();
            Inventory.deletePart(selectedPart);
            MainMenuPartsTableView.setItems(Inventory.getAllParts());
        }
    }

    Product product;
    Part part;
    /**Delete Product Event. Deletes selected product after alert conformation. Product with associated part cannot be deleted*/
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
        Product selectedProduct = MainMenuProductsTableView.getSelectionModel().getSelectedItem();
        if(!selectedProduct.getAllAssociatedParts().isEmpty()){
            Alert hasAssociatedPart = new Alert(Alert.AlertType.ERROR);
            hasAssociatedPart.setTitle("ERROR!");
            hasAssociatedPart.setContentText("Product cannot be deleted with associated part");
            hasAssociatedPart.showAndWait();
            return;
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part will be deleted. Continue?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(selectedProduct);
                MainMenuProductsTableView.setItems(Inventory.getAllProducts());
            }
        }
    }

    /**Exit Button Event. Exits program*/
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);

    }

    /**Modify Part event. Sends selected product to the Modify Part screen and displays modify part screen.*/
    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {

        Part selectedPart = MainMenuPartsTableView.getSelectionModel().getSelectedItem();
        index = Inventory.getAllParts().indexOf(selectedPart);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();

        ModifyPartController ADMController = loader.getController();
        ADMController.sendPart(MainMenuPartsTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Index*/
    private static int index;

    /**Modify Index. Returns index*/
    public static int modifyIndex() {
        return index;
    }



    /**Modify Product Button. Sends product information to modify product screen and displays modify product screen*/
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        Product selectedProduct = MainMenuProductsTableView.getSelectionModel().getSelectedItem();
        index = Inventory.getAllProducts().indexOf(selectedProduct);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        loader.load();

        ModifyProductController ADMController = loader.getController();
        ADMController.sendProduct(MainMenuProductsTableView.getSelectionModel().getSelectedItem());

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**Search Parts Event. Search by ID or Name. Search by ID auto selects matching part. Search by name displays products with characters matching search. No search results displays error */
    @FXML
    void onInputMethodSearchParts(KeyEvent event) {
        String searchText = searchPartsText.getText();
        if (isInteger(searchText)){
            Inventory.lookupPart(searchText);
            MainMenuPartsTableView.getSelectionModel().select(selectPartID(Integer.parseInt(searchText)));

        }
        else if (!isInteger(searchText)){
            Inventory.lookupPart(searchText);
            MainMenuPartsTableView.setItems(Inventory.lookupPart(searchText));
        }
        if (MainMenuPartsTableView.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("No Parts Match Search Terms. Please Try Again");
            alert.showAndWait();
            return;
        }
    }

    /**Select Part ID Method. Returns parts match ID if if present, otherwise it returns null*/
    public Part selectPartID(int id){
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == id)
                return part;
        }
        return null;
    }

    /**Select Product ID Method. Returns prduct matching ID if present, otherwise returns null*/
    public Product selectProductID(int id){
        for(Product product : Inventory.getAllProducts()){
            if(product.getId() == id)
                return product;
        }
        return null;
    }

    /**Search Parts Event. Search by ID or Name. Search by ID auto selects matching product. Search by name displays products with characters matching search. No search results displays error */
    @FXML
    void onInputMethodSearchProducts(KeyEvent event) {
        String searchText = searchProductsTxt.getText();
        if (isInteger(searchText)){
            Inventory.lookupProduct(searchText);
            MainMenuProductsTableView.getSelectionModel().select(selectProductID(Integer.parseInt(searchText)));

        }
        else if (!isInteger(searchText)){
            Inventory.lookupProduct(searchText);
            MainMenuProductsTableView.setItems(Inventory.lookupProduct(searchText));
        }
        if (MainMenuProductsTableView.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("No Parts Match Search Terms. Please Try Again");
            alert.showAndWait();
            return;
        }
    }

    /**Is integer method used to determine if a search input is an integer or not. */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**Initialize method sets the Parts and Product table view with the Parts and Products from Inventory*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainMenuPartsTableView.setItems(Inventory.getAllParts());
        colPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        MainMenuProductsTableView.setItems(Inventory.getAllProducts());

        colProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProductInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
}
