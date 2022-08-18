package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class adds a part. */
public class AddPartController implements Initializable {
    Stage stage;
    Parent scene;


    @FXML
    private RadioButton AddPartInHouseRBT;

    @FXML
    private RadioButton AddPartOutsourcedRBT;

    @FXML
    private TextField AddPartIDText;

    @FXML
    private TextField AddPartName;

    @FXML
    private TextField AddPartInv;

    @FXML
    private TextField AddPartPrice;

    @FXML
    private TextField AddPartMax;

    @FXML
    private TextField AddPartMachineID;

    @FXML
    private TextField AddPartMin;

    @FXML
    private Button AddPartSave;

    @FXML
    private Button AddPartCancel;

    @FXML
    private Label MachOSLBL;

    private int partAutoID;

    /** Cancel part with an alert acknowledge changes would be lost.
     * Confirming takes the user to the main screen. Canceling keeps the user on the parts page.
     * @param event Canceling */
    @FXML
    void OnActionAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Values will not be saved. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }


    /** Radio buttons to select a part as In House or Outsourced and change the MachOSLBL to reflect that.
     */
    @FXML
    void OnActionAddPartInHouse(ActionEvent event) {
        MachOSLBL.setText("Machine ID");
        AddPartInHouseRBT.setSelected(true);

        }
    /** Radio buttons to select a part as In House or Outsourced and change the MachOSLBL to reflect that.
     */
    @FXML
    void OnActionAddPartOutsourced(ActionEvent event) {
        MachOSLBL.setText("Company\rName");
        AddPartOutsourcedRBT.setSelected(true);
    }

    /**
     Save part. Part ID increases +1.
     Part is saved as In House or Outsourced based on the radio button selection.
     Alerts for errors include checking for:
     Max < Min
     Inv > Max
     Inv < 0
     Part Name empty
     Part Price less than 0
     Inv. Min, Max, and Machine ID either empty or a non-number
     */
    @FXML
    void OnActionAddPartSave(ActionEvent event) throws IOException {
        /** Auto generate partID +1*/
        if (!Inventory.getAllParts().isEmpty()){
            partAutoID = Inventory.getAllParts().get(Inventory.getAllParts().size() - 1).getId();
        } else {
            partAutoID = 0;
        }
        int partID = partAutoID + 1;

        try {
            /** If statement to add part as In House if In House radio button is selected */
            if (AddPartInHouseRBT.isSelected()) {

                /* Generating part information from text inputs */

                String partName = AddPartName.getText();
                int partINV = Integer.parseInt(AddPartInv.getText());
                double partPrice = Double.parseDouble(AddPartPrice.getText());
                int max = Integer.parseInt(AddPartMax.getText());
                int min = Integer.parseInt(AddPartMin.getText());
                int machineID = Integer.parseInt(AddPartMachineID.getText());

                /** Checks if min > max. Alerts if true. */
                if (min > max) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Minimum stock inventory cannot exceed maximum stock inventory.");
                    alert.showAndWait();
                    return;
                }
                ;
                /** Checks if partINV > max. Alerts if true. */
                if (partINV > max) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part inventory cannot exceed maximum inventory.");
                    alert.showAndWait();
                    return;
                }
                /** Checks if partINV < min. Alerts if true. */
                if (partINV < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part inventory cannot be less than minimum inventory.");
                    alert.showAndWait();
                    return;
                }
                /** Checks if part name is true. Alerts if true. */
                if (partName.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part name cannot be empty");
                    alert.showAndWait();
                    return;
                }
                /** Checks if parts price is < 0. Alerts if true.*/
                if (partPrice < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Price cannot be less than 0");
                    alert.showAndWait();
                    return;
                }
                /** Checks if partINV is < 0. Alerts if true. */
                if (partINV < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Price cannot be less than 0");
                    alert.showAndWait();
                    return;
                };

                Inventory.addPart(new InHouse(partID, partName, partPrice, partINV, min, max, machineID) {
                });

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            /** Checks if any field is empty of if Inv, Min, Max, machine ID are numerals. */
        }catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Inputs cannot be left blank\rInv., Min, Max, and Machine ID must be numerals");
            alert.showAndWait();
        }
        ;

        /** If statement to add part as Outsourced if Outsourced radio button is selected */
        if (AddPartOutsourcedRBT.isSelected()){
            /* Generating part information from text inputs */
            try {
                String partName = AddPartName.getText();
                int partINV = Integer.parseInt(AddPartInv.getText());
                double partPrice = Double.parseDouble(AddPartPrice.getText());
                int max = Integer.parseInt(AddPartMax.getText());
                int min = Integer.parseInt(AddPartMin.getText());
                String companyName = AddPartMachineID.getText();

                /** Checks if min > max. Alerts if true. */
                if (min > max) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Minimum stock inventory cannot exceed maximum stock inventory.");
                    alert.showAndWait();
                    return;
                }
                ;
                /** Checks if partINV > max. Alerts if true. */
                if (partINV > max) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part inventory cannot exceed maximum inventory.");
                    alert.showAndWait();
                    return;
                }
                ;
                /** Checks if partINV < min. Alerts if true. */
                if (partINV < min) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part inventory cannot be less than minimum inventory.");
                    alert.showAndWait();
                    return;
                }
                /** Checks if part name is empty. Alerts if true. */
                if (partName.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Part name cannot be empty");
                    alert.showAndWait();
                    return;
                }
                /** Checks if parts price is < 0. Alerts if true. */
                if (partPrice < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Price cannot be less than 0");
                    alert.showAndWait();
                    return;
                }
                /** Checks if partINV is < 0. Alerts if true. */
                if (partINV < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Price cannot be less than 0");
                    alert.showAndWait();
                    return;
                }
                /** Checks if company name is empty. Alerts if true. */
                if (companyName.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Company name cannot be empty");
                    alert.showAndWait();
                    return;
                };

                Inventory.addPart(new Outsourced(partID, partName, partPrice, partINV, min, max, companyName) {
                });

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();


            /** Checks if any field is empty of if Inv, Min, Max, are numerals. */
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Inputs cannot be left blank\rInv., Min, Max, must be numerals");
                alert.showAndWait();
            }
        }
    }

    /**
     Set Part ID text to tell user ID number is auto generated
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AddPartIDText.setText("ID is Auto Generated");

    }

}
