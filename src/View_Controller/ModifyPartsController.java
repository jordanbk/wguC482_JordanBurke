package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
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

import static Model.Inventory.getAllParts;
import static View_Controller.MainScreenController.modifiedPart;
import static View_Controller.MainScreenController.partsToModList;

public class ModifyPartsController implements Initializable {

    @FXML private TextField ModifyPartsPriceField;
    @FXML private TextField ModifyPartsIDField;
    @FXML private TextArea ModifyPartsMaxField;
    @FXML private Label ModifyPartDynLabel;
    @FXML private Label modifyPartID;
    @FXML private RadioButton ModifyPartInHouseRadioBtn;
    @FXML private RadioButton ModifyPartOutsourceRadioBtn;
    @FXML private TextField ModifyPartsInvField;
    @FXML private TextArea ModifyPartsDynField;
    @FXML private TextField ModifyPartsNameField;
    @FXML private TextArea ModifyPartsMinField;
    @FXML private ToggleGroup in_out_group;

    private boolean isOutsourced;
    int partIndex = partsToModList();
    private static String error;

    //inHouse part
    @FXML
    void ModifyPartInHouseBtn(ActionEvent event) {
        isOutsourced = false;
        ModifyPartOutsourceRadioBtn.setSelected(false);
        ModifyPartDynLabel.setText("Machine ID");
        ModifyPartsDynField.setText("");
    }

    //outsourced part
    @FXML
    void ModifyPartOutsourcedBtn(ActionEvent event) {
        isOutsourced = true;
        ModifyPartInHouseRadioBtn.setSelected(false);
        ModifyPartDynLabel.setText("Company Name");
        ModifyPartsDynField.setText("");
    }

    //save changes

    /**
     *
     * @param event
     * @throws IOException the error message was not popping-up when fields
     * were not filled in. I added the try/catch statements and then it functioned as
     * intended.
     */
    @FXML
    void ModifyPartSaveClick(ActionEvent event) throws IOException {

        try {
            int ID = Integer.parseInt(ModifyPartsIDField.getText());
            String name = ModifyPartsNameField.getText();
            int inv = Integer.parseInt(ModifyPartsInvField.getText());
            double price = Double.parseDouble(ModifyPartsPriceField.getText());
            int max = Integer.parseInt(ModifyPartsMaxField.getText());
            int min = Integer.parseInt(ModifyPartsMinField.getText());

            String validatePart = partValidation(name, price, inv, max, min);
            if (validatePart == null) {
                if (ModifyPartInHouseRadioBtn.isSelected()) {
                    int machineID = Integer.parseInt(ModifyPartsDynField.getText());
                    InHouse addPart = new InHouse(ID, name, price, inv, max, min, machineID);

                    Inventory.updatePart(ID - 1, addPart);
                }
                if (ModifyPartOutsourceRadioBtn.isSelected()) {
                    String companyName = ModifyPartsDynField.getText();
                    Outsourced addPart = new Outsourced(ID, name, price, inv, max, min, companyName);

                    Inventory.updatePart(ID - 1, addPart);
                }
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Unable to add part");
                alert.setContentText(validatePart);
                alert.showAndWait();
            }
        }
        catch(NumberFormatException ex)
        {
            Alert Error = new Alert(Alert.AlertType.INFORMATION);
            Error.setTitle("Error");
            Error.setContentText("All fields must be filled in");
            Error.showAndWait();
        }
    }

    //cancel change
    @FXML
    void ModifyPartCancelClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete all text, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
    public static String partValidation(String name, double price, int stock,  int max, int min){
        error = null;
        if(stock > max || stock < min){
            error = "Inventory must be greater than min and less than the max value.";
        }
        if(min > max){
            error = "Min must be less than max value.";
        }
        if(max < min){
            error = "Max must be greater than min value.";
        }
        if(name == null){
            error = "Please fill in the name field";
        }
        if(price < 1){
            error = "Please fill in the price field";
        }

        return error;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Part modifyPart = getAllParts().get(modifiedPart.getId()-1);

        ModifyPartsIDField.setText(Integer.toString(modifyPart.getId()));
        ModifyPartsNameField.setText(modifyPart.getName());
        ModifyPartsInvField.setText(Integer.toString(modifyPart.getStock()));
        ModifyPartsPriceField.setText(Double.toString(modifyPart.getPrice()));
        ModifyPartsMinField.setText(Integer.toString(modifyPart.getMin()));
        ModifyPartsMaxField.setText(Integer.toString(modifyPart.getMax()));
        if (modifyPart instanceof InHouse) {
            ModifyPartDynLabel.setText("Machine ID");
            ModifyPartsDynField.setText(Integer.toString(((InHouse) getAllParts().get(partIndex)).getMachineId()));
            ModifyPartInHouseRadioBtn.setSelected(true);
        }
        else {
            ModifyPartDynLabel.setText("Company Name");
            ModifyPartsDynField.setText(((Outsourced) getAllParts().get(partIndex)).getCompanyName());
            ModifyPartOutsourceRadioBtn.setSelected(true);
        }
    }
}