package View_Controller;


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

import static java.lang.Integer.parseInt;

public class AddPartController implements Initializable {
    @FXML private TextField AddPartsInvField;
    @FXML private TextArea AddPartsDynField;
    @FXML private RadioButton AddPartInHouseRadioBtn;
    @FXML private TextField AddPartsNameField;
    @FXML private TextField AddPartsIDField;
    @FXML private TextArea AddPartsMinField;
    @FXML private RadioButton AddPartOutsourceRadioBtn;
    @FXML private Label AddPartDynLabel;
    @FXML private TextField AddPartsPriceField;
    @FXML private ToggleGroup in_out_group;
    @FXML private TextArea AddPartsMaxField;

    private boolean isOutsourced = true;

    @FXML
    private Button AddPartsCancelClick;
    private int partID;
    private static String error;

    /**
     *
     * @param event setting up inHouse part
     */
    @FXML
    void AddPartsInHouseClick(ActionEvent event) {
        isOutsourced = false;
        AddPartDynLabel.setText("Machine ID");
    }

    /**
     *
     * @param event setting up outsourced part
     */
    @FXML
    void AddPartsOutsourcedClick(ActionEvent event) {
        isOutsourced = true;
        AddPartDynLabel.setText("Company Name");
    }

    /**
     *
     * @param event
     * @throws IOException the error message was not popping-up when fields
     * were not filled in. I added the try/catch statements and then it functioned as
     * intended.
     */
    @FXML
    void AddPartSaveClick(ActionEvent event) throws IOException {

      try {
          int ID = Integer.parseInt(AddPartsIDField.getText());
          String name = AddPartsNameField.getText();
          int inv = Integer.parseInt(AddPartsInvField.getText());
          double price = Double.parseDouble(AddPartsPriceField.getText());
          int max = Integer.parseInt(AddPartsMaxField.getText());
          int min = Integer.parseInt(AddPartsMinField.getText());

          String isValid = AddPartController.partValidation(name, price, inv, max, min);
          if (AddPartInHouseRadioBtn.isSelected()) {
                  int machineID = parseInt(AddPartsDynField.getText());
                  InHouse addPart = new InHouse(ID, name, price, inv, max, min, machineID);

                  Inventory.addPart(addPart);
          }

          if (AddPartOutsourceRadioBtn.isSelected()) {
              String companyName = AddPartsDynField.getText();
              Outsourced addPart = new Outsourced(ID, name, price, inv, max, min, companyName);

              Inventory.addPart(addPart);
          }

          Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
          Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
          stage.setScene(new Scene((Parent) scene));
          stage.show();
      }
      catch(NumberFormatException ex)
      {
          Alert Error = new Alert(Alert.AlertType.INFORMATION);
          Error.setTitle("Error");
          Error.setContentText("All fields must be filled in");
          Error.showAndWait();
      }
    }

    /**
     *
     * @param event cancel and load main screen
     * @throws IOException
     */
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
        AddPartsIDField.setText(String.valueOf(Inventory.getAllParts().size() + 1));

    }


}
