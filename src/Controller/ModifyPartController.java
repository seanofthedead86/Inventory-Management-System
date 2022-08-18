package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.MainMenuController.modifyIndex;

/**Modify Part Controller*/
public class ModifyPartController implements Initializable {
    Stage stage;
    Parent scene;

    /**In House Radio Button*/
    @FXML
    private RadioButton ModifyPartInHouseRBT;

    /**Out Sourced Radio Button*/
    @FXML
    private RadioButton OutsourcedRBT;

    /**Part Id Text*/
    @FXML
    private TextField ModifyPartIDText;

    /**Part Name Text*/
    @FXML
    private TextField ModifyPartName;

    /**Part Inventory Text*/
    @FXML
    private TextField ModifyPartInv;

    /**Part Price Text*/
    @FXML
    private TextField ModifyPartPrice;

    /**Part Inv. Max Text*/
    @FXML
    private TextField ModifyPartMax;

    /**Part Machine ID Text*/
    @FXML
    private TextField ModifyPartMachineID;

    /**Part Inv Min*/
    @FXML
    private TextField ModifyPartMin;

    /**Save Button*/
    @FXML
    private Button ModifyPartSave;

    /**Cancel Button*/
    @FXML
    private Button ModifyPartCancel;

    /**MachineId or Company Name Label*/
    @FXML
    private Label MachOSLBL;


    /**Cancel button. Returns user to the main screen on confirmation and changes are not saved. */
    @FXML
    void OnActionModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Values will not be saved. Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**In House radio button. Selects part to be an In House part and changes MachOSLBL to Machine ID*/
    @FXML
    void OnActionModifyPartInHouse(ActionEvent event) {
        MachOSLBL.setText("Machine ID");
        ModifyPartInHouseRBT.setSelected(true);
    }

    /**Outsourced radio button. Selects part to be an Outsourced part and changes MachOSLBL to Company Name*/
    @FXML
    void OnActionModifyPartOutsourced(ActionEvent event) {
        MachOSLBL.setText("Company\rName");
        OutsourcedRBT.setSelected(true);

    }

    /**Part Index*/
    int partIndex = modifyIndex();

    /**
     * Part is saved as In House or Outsourced based on the radio button selection.
     * Alerts for errors include checking for:
     * Max < Min
     * Inv > Max
     * Inv < 0
     * Part Name empty
     * Part Price less than 0
     * Inv. Min, Max, and Machine ID either empty or a non-number
    */
    @FXML
    void OnActionModifyPartSave(ActionEvent event) throws IOException {

        /* If statement to add part as In House if In House radio button is selected */
            if (ModifyPartInHouseRBT.isSelected()) {
                try {

                    /* Generating part information from text inputs */
                    int partID = Integer.parseInt(ModifyPartIDText.getText());
                    String partName = ModifyPartName.getText();
                    int partINV = Integer.parseInt(ModifyPartInv.getText());
                    double partPrice = Double.parseDouble(ModifyPartPrice.getText());
                    int max = Integer.parseInt(ModifyPartMax.getText());
                    int min = Integer.parseInt(ModifyPartMin.getText());
                    int machineID = Integer.parseInt(ModifyPartMachineID.getText());

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
                    /** Checks if part name is true. Alerts if true. */
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
                    ;

                    Inventory.updatePart(partIndex, new InHouse(partID, partName, partPrice, partINV, min, max, machineID) {
                    });

                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                    /** Checks if any field is empty of if Inv, Min, Max, machine ID are numerals. */
                }catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Inputs cannot be left blank\rInv., Min, Max, and Machine ID must be numerals");
                    alert.showAndWait();
                }
            }

        /** If statement to add part as Outsourced if Outsourced radio button is selected */
        if (OutsourcedRBT.isSelected()) {
            try {
                /* Generating part information from text inputs */
                int partID = Integer.parseInt(ModifyPartIDText.getText());
                String partName = ModifyPartName.getText();
                int partINV = Integer.parseInt(ModifyPartInv.getText());
                double partPrice = Double.parseDouble(ModifyPartPrice.getText());
                int max = Integer.parseInt(ModifyPartMax.getText());
                int min = Integer.parseInt(ModifyPartMin.getText());
                String companyName = ModifyPartMachineID.getText();

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
                /**Check to see if company name is empty*/
                if (companyName.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Company name cannot be empty");
                    alert.showAndWait();
                    return;
                }
                ;

                Inventory.updatePart(partIndex, new Outsourced(partID, partName, partPrice, partINV, min, max, companyName) {
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

    /**SendPart receives part data selected from the MainMenuPartTableView*/
    public void sendPart (Part part) {

        /**Populates part data selected from the MainMenuPartsTableView if it is an In House part */
        if(part instanceof InHouse) {
            ModifyPartInHouseRBT.setSelected(true);
            ModifyPartIDText.setText(String.valueOf(part.getId()));
            ModifyPartName.setText(part.getName());
            ModifyPartInv.setText(String.valueOf(part.getStock()));
            ModifyPartPrice.setText(String.valueOf(part.getPrice()));
            ModifyPartMax.setText(String.valueOf(part.getMax()));
            ModifyPartMin.setText(String.valueOf(part.getMin()));
            ModifyPartMachineID.setText((String.valueOf(((InHouse) part).getMachineId())));
            System.out.println(((InHouse) part).getMachineId());
        }

        /**Populates part data selected from the MainMenuPartsTableView if it is an Outsourced part */
        if(part instanceof Outsourced){
            MachOSLBL.setText("Company\rName");
            OutsourcedRBT.setSelected(true);
            ModifyPartIDText.setText(String.valueOf(part.getId()));
            ModifyPartName.setText(part.getName());
            ModifyPartInv.setText(String.valueOf(part.getStock()));
            ModifyPartPrice.setText(String.valueOf(part.getPrice()));
            ModifyPartMax.setText(String.valueOf(part.getMax()));
            ModifyPartMin.setText(String.valueOf(part.getMin()));
            ModifyPartMachineID.setText(((Outsourced) part).getCompanyName());
            System.out.println(((Outsourced) part).getCompanyName());
        }
    }


    /**Initialize Method*/
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
