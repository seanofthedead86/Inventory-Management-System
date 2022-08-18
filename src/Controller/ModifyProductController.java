package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.MainMenuController.modifyIndex;

/**Modify Product Controller*/
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField inventoryText;

    @FXML
    private TextField priceText;

    @FXML
    private TextField maxText;

    @FXML
    private TextField minText;

    @FXML
    private TextField searchText;

    @FXML
    private TableView<Part> addPartTableView;

    @FXML
    private TableColumn<Part, Integer> colPartID;

    @FXML
    private TableColumn<Part, String> colPartName;

    @FXML
    private TableColumn<Part, Integer> colInventoryLevel;

    @FXML
    private TableColumn<Part, Double> colPrice;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Part> associatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> colPartIDB;

    @FXML
    private TableColumn<Part, String> colPartNameB;

    @FXML
    private TableColumn<Part, Integer> colInventoryLevelB;

    @FXML
    private TableColumn<Part, Double> colPriceB;

    @FXML
    private Button removeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private ObservableList<Part> modParts = FXCollections.observableArrayList();
    private ObservableList<Part> tempParts = FXCollections.observableArrayList();

    Part part;
    Product product;


    /**
     * Adds selected part in the addPartsTableView to the tempParts observableList
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();
        tempParts.add(selectedPart);
    }

    /**
     * Cancel product with an alert acknowledging changes would be lost
     * Confirming takes the user to the main screen
     * Canceling keeps the user on the products page
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Values will not be saved. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Removes selected part from the associatedPartsTableView
     * Alerts user to confirm
     */
    @FXML
    void onActionRemove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Part will be removed. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Part selectedPart = associatedPartTableView.getSelectionModel().getSelectedItem();
            tempParts.remove(selectedPart);
        }
    }


    /** Returns a part from the parts observableList if it matches a selected part's id*/
    public Part selectPartID(int id){
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == id)
                return part;
        }
        return null;
    }

    /**
     * Search parts by either part name or part ID
     * When searching by part name a filtered list of results is shown in the table
     * When searching by part ID, the part with matching ID is auto-selected
     * When a search result yields nothing, an alert is generated.
     */
    @FXML
    void onInputMethodSearchParts(KeyEvent event) {
        String partSearch = searchText.getText();
        if (MainMenuController.isInteger(partSearch)){
            Inventory.lookupPart(partSearch);
            System.out.println("Suc");
            addPartTableView.getSelectionModel().select(selectPartID(Integer.parseInt(partSearch)));
        }
        else if (!MainMenuController.isInteger(partSearch)){
            Inventory.lookupPart(partSearch);
            System.out.println("Suc2");
            addPartTableView.setItems(Inventory.lookupPart(partSearch));
        }
        if (addPartTableView.getItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("No Parts Match Search Terms. Please Try Again");
            alert.showAndWait();
            return;
        }
    }
    int productIndex = modifyIndex();


    /**
     * Saves product
     * Alerts check for:
     * Max < Min
     * Inv > Max
     * Inv < Min
     * Product name is empty
     * If Min, Max, and Inv are empty or non-numbers
     * Parts in the tempParts observableList observable list are moved to the modPart observableList
     * Those parts are then added to the associatedParts observable list associated to only the part being saved
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {

        try {

            int productID = Integer.parseInt(idText.getText());
            String productName = nameText.getText();
            int productINV = Integer.parseInt(inventoryText.getText());
            double productPrice = Double.parseDouble(priceText.getText());
            int max = Integer.parseInt(maxText.getText());
            int min = Integer.parseInt(minText.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Minimum stock inventory cannot exceed maximum stock inventory.");
                alert.showAndWait();
                return;
            };

            if (productINV > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product inventory cannot exceed maximum inventory.");
                alert.showAndWait();
                return;
            };
            if (productINV < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product inventory cannot be less than minimum inventory.");
                alert.showAndWait();
                return;
            };
            if (productName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product name cannot be empty");
                alert.showAndWait();
                return;
            };
            if (productPrice < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product price cannot be less than 0");
                alert.showAndWait();
                return;
            };
            if (productINV < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Product inventory cannot be less 0");
                alert.showAndWait();
                return;
            };
            Product modProduct = new Product(productID, productName, productPrice, productINV, min, max);

            modParts.addAll(associatedPartTableView.getItems());
            Inventory.updateProduct(productIndex, modProduct);
            System.out.println("Mod Prod Saved");

            for (Part part : modParts) {
                modProduct.addAssociatedPart(part);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }catch (NumberFormatException  e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Cannot leave input fields blank.\rInv. Price, Min, and Max must be numerals");
            alert.showAndWait();
        }

    }

    /**Receives data from the MainMenuProduct page and populates it on the Modify Parts Page
    *Also adds associated parts for the selected product into the tempParts observableList
    */
    public void sendProduct (Product product){

        idText.setText(String.valueOf(product.getId()));
        nameText.setText(product.getName());
        inventoryText.setText(String.valueOf(product.getStock()));
        priceText.setText(String.valueOf(product.getPrice()));
        maxText.setText(String.valueOf(product.getMax()));
        minText.setText(String.valueOf(product.getMin()));

        for (Part part : product.getAllAssociatedParts()) {
            tempParts.add(part);
        }

    }

    /**Top and bottom table views are populated.
    * Top table view is populated with data from the Parts observableList
    * Bottom table view is populated with data from the tempParts observableList
    */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addPartTableView.setItems(Inventory.getAllParts());
        colPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartTableView.setItems(tempParts);
        colPartIDB.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPartNameB.setCellValueFactory(new PropertyValueFactory<>("name"));
        colInventoryLevelB.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colPriceB.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
