package View_Controller;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.*;
import static Model.Product.getAssociatedParts;
import static View_Controller.MainScreenController.modifiedProduct;
import static java.lang.Integer.parseInt;

public class ModifyProductsController implements Initializable {
    private Product selectedProduct;

    @FXML private TextField ModifyProductsInvField;
    @FXML private TextField ModifyProduct_SearchField;
    @FXML private TextArea ModifyProductsMinField;
    @FXML private Button ModifyProduct_Save;
    @FXML private TableColumn<Part, Double> ModifyProductCurrentPartPriceCol;
    @FXML private TableColumn<Part, Double> ModifyProductPartPriceCol;
    @FXML private TextField ModifyProductsNameField;
    @FXML private TextField ModifyProductsIDField;
    @FXML private TextField ModifyProductsPriceField;
    @FXML private TableColumn<Part, Integer> AddProductInvLevelCol;
    @FXML private TableColumn<Part, Integer> ModifyProductPartIDCol;
    @FXML private TableView<Part> addPartSearchTable;
    @FXML private TextArea ModifyProductsMaxField;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, String> ModifyProductPartNameCol;
    @FXML private TableColumn<Part, String> ModifyProductCurrentPartNameCol;
    @FXML private TableColumn<Part, Integer> ModifyProductCurrentPartInvCol;
    @FXML private TableColumn<Part, Integer> ModifyProductCurrentPartIDCol;
    @FXML
    private int productID;

    private final ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    public static ObservableList<Part> associatedParts = FXCollections.observableArrayList(getAssociatedParts());

    private ObservableList<Part> allParts = FXCollections.observableArrayList(getAllParts());

    /**
     *
     * @param searchedPart for search feature to add part to list
     */
    public void addFilteredPart(Part searchedPart){
        filteredParts.add(searchedPart);
    }
    /**
     *
     * @return filtered parts list
     */
    public ObservableList<Part> getFilteredParts(){
        return filteredParts;
    }

    @FXML
    void ModifyProduct_SearchPartBtn(ActionEvent event) {
        if (ModifyProduct_SearchField.getText().isEmpty()) {
            addPartSearchTable.setItems(getAllParts());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a search value");
            alert.showAndWait();
        }
        else {
            filteredParts.clear();
            try{
                addFilteredPart(Inventory.lookupPart(parseInt(ModifyProduct_SearchField.getText())));

                addPartSearchTable.setItems(getFilteredParts());
            }
            catch (Exception e){
                Inventory.lookupPart(ModifyProduct_SearchField.getText());
                addPartSearchTable.setItems(Inventory.getFilteredPart());
            }
        }
    }

    @FXML
    void ModifyProduct_AddPartBtn(ActionEvent event) {
       Part addPart = addPartSearchTable.getSelectionModel().getSelectedItem();
        if (addPart != null) {
            associatedParts.add(addPart);
//            allParts.remove(addPart);
//            addPartSearchTable.setItems(allParts);
            updateAssociatedPartsTable();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Select a part");
            alert.setContentText("Please select a part");
            alert.showAndWait();
        }
    }
    private void updateAssociatedPartsTable(){
        associatedPartsTable.setItems(associatedParts);
    }
    private void updateAllPartsTable(){
        addPartSearchTable.setItems(getAllParts());
    }
    @FXML
    void ModifyProduct_SaveBtn(ActionEvent event) throws IOException {
        int productId = Integer.parseInt(ModifyProductsIDField.getText());
        String name = ModifyProductsNameField.getText();
        String inv = ModifyProductsInvField.getText();
        String price = ModifyProductsPriceField.getText();
        String max = ModifyProductsMaxField.getText();
        String min = ModifyProductsMinField.getText();
        try {
            int tempAssociatedParts = associatedParts.size();
            Double renderPrice = Double.parseDouble(price);
            Integer renderInv = Integer.parseInt(inv);
            Integer renderMax = Integer.parseInt(max);
            Integer renderMin = Integer.parseInt(min);

            String isValid = Product.productValidation(name, renderPrice, renderInv, renderMax, renderMin, tempAssociatedParts);
            if (isValid == null) {
                Product tempProduct = new Product();
                tempProduct.setId(productId);
                tempProduct.setName(name);
                tempProduct.setPrice(renderPrice);
                tempProduct.setStock(renderInv);
                tempProduct.setMax(renderMax);
                tempProduct.setMin(renderMin);
                tempProduct.setAssociatedParts(associatedParts);
                Inventory.updateProduct(productId - 1, tempProduct);

                Stage stage = (Stage) ModifyProduct_Save.getScene().getWindow();
                Parent saved = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene scene = new Scene(saved);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Product cannot be added");
                alert.setContentText(isValid);
                alert.showAndWait();
            }
        }
        catch(NumberFormatException ex)
        {
            Alert Error = new Alert(Alert.AlertType.INFORMATION);
            Error.setTitle("Error");
            Error.setContentText("Some fields are blank");
            Error.showAndWait();
        }
    }

    @FXML
    void ModifyProduct_CancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete all text, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    @FXML
    void ModifyProduct_DeletePartBtn(ActionEvent event) {
        Part deletePart = associatedPartsTable.getSelectionModel().getSelectedItem();
        if(deletePart != null){
            associatedParts.remove(deletePart);
            associatedPartsTable.setItems(associatedParts);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select a part");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Product modifyNewProduct = getAllProducts().get(modifiedProduct.getId()-1);

        ModifyProductsIDField.setText(String.valueOf(modifyNewProduct.getId()));
        ModifyProductsNameField.setText(modifyNewProduct.getName());
        ModifyProductsInvField.setText(Integer.toString(modifyNewProduct.getStock()));
        ModifyProductsPriceField.setText(Double.toString(modifyNewProduct.getPrice()));
        ModifyProductsMaxField.setText(Integer.toString(modifyNewProduct.getMax()));
        ModifyProductsMinField.setText(Integer.toString(modifyNewProduct.getMin()));
        addPartSearchTable.setItems(allParts);
        associatedPartsTable.setItems(associatedParts);

        ModifyProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProductInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        ModifyProductCurrentPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductCurrentPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProductCurrentPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductCurrentPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateAssociatedPartsTable();
        updateAllPartsTable();

//        addPartSearchTable.setItems(Inventory.getAllParts());
    }


}








