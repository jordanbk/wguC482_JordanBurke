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

public class AddPartController implements Initializable {

    @FXML
    private TextField AddPartsInvField;

    @FXML
    private TextArea AddPartsDynField;

    @FXML
    private RadioButton AddPartInHouseRadioBtn;

    @FXML
    private TextField AddPartsNameField;

    @FXML
    private TextField AddPartsIDField;

    @FXML
    private TextArea AddPartsMinField;

    @FXML
    private RadioButton AddPartOutsourceRadioBtn;

    @FXML
    private Button AddPartsSaveClick;

    @FXML
    private Label AddPartDynLabel;

    @FXML
    private TextField AddPartsPriceField;

    @FXML
    private ToggleGroup in_out_group;

    @FXML
    private TextArea AddPartsMaxField;

    private boolean isOutsourced = true;

    @FXML
    private Button AddPartsCancelClick;
    private int partID;
    @FXML
    void AddPartsInHouseClick(ActionEvent event) {
        isOutsourced = false;
        AddPartDynLabel.setText("Machine ID");
    }

    @FXML
    void AddPartsOutsourcedClick(ActionEvent event) {
        isOutsourced = true;
        AddPartDynLabel.setText("Company Name");
    }

    @FXML
    void AddPartSaveClick(ActionEvent event) {

        int ID = 0;
        for(Part part : Inventory.getAllParts()) {

            if(part.getId() > ID)

                ID = part.getId();

        }

        AddPartsIDField.setText(String.valueOf(++ID));
        String name = AddPartsNameField.getText();
        int inventory = Integer.parseInt(AddPartsInvField.getText());
        double price = Double.parseDouble(AddPartsPriceField.getText());
        int max = Integer.parseInt(AddPartsMaxField.getText());
        int min = Integer.parseInt(AddPartsMinField.getText());

        try{

            if(min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min value cannot be greater than the maximum value.");
                alert.showAndWait();
            }
            else if (inventory > max || inventory < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory amount must be between minimum and maximum values.");
                alert.showAndWait();
            }
            else {

                if (AddPartInHouseRadioBtn.isSelected()) {
                    int machineID = Integer.parseInt(AddPartsDynField.getText());
                    InHouse addPart = new InHouse(ID, name, price, inventory, min, max, machineID);

                    Inventory.addPart(addPart);
                }
                if (AddPartOutsourceRadioBtn.isSelected()) {
                    String companyName = AddPartsDynField.getText();
                    Outsourced addPart = new Outsourced(ID, name, price, inventory, min, max, companyName);

                    Inventory.addPart(addPart);
                }


                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

        }

        catch(NumberFormatException | IOException e){


            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please enter a valid value for each text field.");
            alert.showAndWait();

        }
    }


    @FXML
    void AddPartsCancelClick(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete all text, are you sure you want to continue?");

        Optional <ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

        }

    }

    public void ModifyPartOutsourcedBtn(ActionEvent actionEvent) {
    }

    public void ModifyPartInHouseBtn(ActionEvent actionEvent) {
    }

    public void ModifyPartCancelClick(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AddPartsIDField.setText(String.valueOf(Inventory.getAllParts().size() + 1));

    }
}
