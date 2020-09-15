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
import static View_Controller.MainScreenController.partsToModList;

public class ModifyPartsController implements Initializable {

    @FXML
    private TextField ModifyPartsPriceField;
    @FXML
    private TextField ModifyPartsIDField;
    @FXML
    private TextArea ModifyPartsMaxField;
    @FXML
    private Label ModifyPartDynLabel;
    @FXML
    private Label modifyPartID;
    @FXML
    private RadioButton ModifyPartInHouseRadioBtn;
    @FXML
    private RadioButton ModifyPartOutsourceRadioBtn;
    @FXML
    private Button ModifyPartSaveClick;
    @FXML
    private Button ModifyPartCancelClick;
    @FXML
    private TextField ModifyPartsInvField;
    @FXML
    private TextArea ModifyPartsDynField;
    @FXML
    private TextField ModifyPartsNameField;
    @FXML
    private TextArea ModifyPartsMinField;
    @FXML
    private ToggleGroup in_out_group;

    private boolean isOutsourced;
    private int partID;
    private String exceptionMessage = new String();
    int partIndex = partsToModList();

    @FXML
    void ModifyPartInHouseBtn(ActionEvent event) {
        isOutsourced = false;
        ModifyPartOutsourceRadioBtn.setSelected(false);
        ModifyPartDynLabel.setText("Machine ID");
        ModifyPartsDynField.setText("");
    }

    @FXML
    void ModifyPartOutsourcedBtn(ActionEvent event) {
        isOutsourced = true;
        ModifyPartInHouseRadioBtn.setSelected(false);
        ModifyPartDynLabel.setText("Company Name");
        ModifyPartsDynField.setText("");
    }

    @FXML
    void ModifyPartSaveClick(ActionEvent event) throws IOException {

        if (this.in_out_group.getSelectedToggle().equals(ModifyPartInHouseRadioBtn)) {
            Part inHousePart = new InHouse(Integer.parseInt(ModifyPartsIDField.getText()),
                    (ModifyPartsNameField.getText()), Double.parseDouble(ModifyPartsPriceField.getText()),
                    Integer.parseInt(ModifyPartsInvField.getText()), Integer.parseInt(ModifyPartsMaxField.getText()),
                            Integer.parseInt(ModifyPartsMinField.getText()),
                           Integer.parseInt(ModifyPartsDynField.getText()));

            Inventory.updatePart(Integer.parseInt(ModifyPartsIDField.getText()), inHousePart);
        }
        if (this.in_out_group.getSelectedToggle().equals(ModifyPartOutsourceRadioBtn)) {
            Part outsourcedPart = new Outsourced(Integer.parseInt(ModifyPartsIDField.getText()),
                    (ModifyPartsNameField.getText()), Double.parseDouble(ModifyPartsPriceField.getText()),
                    Integer.parseInt(ModifyPartsInvField.getText()), Integer.parseInt(ModifyPartsMaxField.getText()),
                    Integer.parseInt(ModifyPartsMinField.getText()), (ModifyPartsDynField.getText()));

            Inventory.updatePart(outsourcedPart.getId(), outsourcedPart);

        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ModifyPartsIDField.setText(String.valueOf(getAllParts().size() + 1));
        Part selectedPart = getAllParts().get(partIndex);
        partID = getAllParts().get(partIndex).getId();
        ModifyPartsIDField.setText(Integer.toString(selectedPart.getId()));
        ModifyPartsNameField.setText(selectedPart.getName());
        ModifyPartsInvField.setText(Integer.toString(selectedPart.getStock()));
        ModifyPartsPriceField.setText(Double.toString(selectedPart.getPrice()));
        ModifyPartsMinField.setText(Integer.toString(selectedPart.getMin()));
        ModifyPartsMaxField.setText(Integer.toString(selectedPart.getMax()));
        if (selectedPart instanceof InHouse) {
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