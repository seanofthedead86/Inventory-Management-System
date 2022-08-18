package Controller;

import Model.InHouse;
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

/** Add Product Controller dictates how a new product is added*/
public class AddProductController implements Initializable {
    Stage stage;
    Parent scene;


    /**Product ID Text*/
    @FXML
    private TextField idText;

    /**Product Name Text*/
    @FXML
    private TextField nameText;

    /**Product Inventory Text*/
    @FXML
    private TextField inventoryText;

    /**Product Price Text*/
    @FXML
    private TextField priceText;

    /**Product Inv Max Text*/
    @FXML
    private TextField maxText;

    /**Product Inv Min Text*/
    @FXML
    private TextField minText;

    /**Search Box Text*/
    @FXML
    private TextField searchText;

    /**Add Parts Table View*/
    @FXML
    private TableView<Part> addPartTableView;

    /**Part ID Column*/
    @FXML
    private TableColumn<Part, Integer> colPartID;

    /**Part Name Column*/
    @FXML
    private TableColumn<Part, String> colPartName;

    /**Part Inventory Column*/
    @FXML
    private TableColumn<Part, Integer> colInventoryLevel;

    /**Part Price Column*/
    @FXML
    private TableColumn<Part, Double> colPrice;

    /**Add Button*/
    @FXML
    private Button addButton;

    /**Associated Parts Table View*/
    @FXML
    private TableView<Part> associatedPartTableView;

    /**Associated Parts ID Column*/
    @FXML
    private TableColumn<Part, Integer> colPartIDB;

    /**Associated Part Name Column*/
    @FXML
    private TableColumn<Part, String> colPartNameB;

    /**Associated Parts Inventory Column*/
    @FXML
    private TableColumn<Part, Integer> colInventoryLevelB;

    /**Associated Parts Price Column*/
    @FXML
    private TableColumn<Part, Double> colPriceB;

    /**Remove Button*/
    @FXML
    private Button removeButton;

    /**Save Button*/
    @FXML
    private Button saveButton;

    /**Cancel Button*/
    @FXML
    private Button cancelButton;

    /**Add Parts Observable List*/
    private ObservableList<Part> modParts = FXCollections.observableArrayList();
    /**Temporary Parts Observable List*/
    private ObservableList<Part> tempParts = FXCollections.observableArrayList();

    /** event
     * Adds selected part in the addPartsTableView to the tempParts observableList
     */
    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = addPartTableView.getSelectionModel().getSelectedItem();
        tempParts.add(selectedPart);
    }

    /** o be used with the search function in selecting a part with a matching part ID */
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
            addPartTableView.getSelectionModel().select(selectPartID(Integer.parseInt(partSearch)));
        }
        else if (!MainMenuController.isInteger(partSearch)){
            Inventory.lookupPart(partSearch);
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

    private int productAutoID;

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
     * An issue that I had with this was getting the parts in the Associated Parts Table View to be associated with the
     * product. My first problem I ran in to was when a part was added to the table, and the product was saved, the part
     * would then disappear when the product was reopened for modification. The second issue I ran in to was when troubelshooting
     * the first issue was I then was able to get the part to save, but it would appear across all products. My solution was to have two
     * observable lists on the in the controller. The first is the tempParts observable list in which the part is added when moved
     * from the top to bottom table. When the save button is pressed, the modParts observable list receives all the parts in the
     * bottom associatedPartsTableView. Those parts are then added to the associatedParts observable for the product being modified.
     * A compatible feature suitable to this application that would extend functionality to the next version if I were to update the application
     * would be adding a database. Adding a database and then allowing that database to accessed on a closed network would allow different
     * departments/divisions of a company to be able to access it. Adding the ability for user login to the program could regulate read/write
     * access for users.
     */
    @FXML
    void onActionSave(ActionEvent event) {
        if (!Inventory.getAllProducts().isEmpty()){
            productAutoID = Inventory.getAllParts().get(Inventory.getAllParts().size() - 1).getId();
        } else {
            productAutoID = 0;
        }
        int productID = productAutoID + 1;

        try {
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
            Inventory.addProduct(modProduct);
            System.out.println("Prod Added");

            for (Part part : modParts) {
                modProduct.addAssociatedPart(part);
            }

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }catch (NumberFormatException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Cannot leave input fields blank.\rInv. Price, Min, and Max must be numerals");
            alert.showAndWait();
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
