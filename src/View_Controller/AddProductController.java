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

import static Model.Inventory.getAllParts;

public class AddProductController implements Initializable {

    @FXML private TextArea AddProductsMinField;
    @FXML private TextField AddProductsIDField;
    @FXML private Button AddProduct_Save;
    @FXML private TextField AddProductsInvField;
    @FXML private TableColumn<Part, Integer> DeleteProductCurrentPartIDCol;
    @FXML private TextField AddProductAddPartSearchField;
    @FXML private TableColumn<Part, Integer> DeleteProductCurrentInvCol;
    @FXML private TableView<Product> AddProductsAddTableView;
    @FXML private TableColumn<Part, String> AddProductPartNameCol;
    @FXML private TableColumn<Part, Double> AddProductPriceCol;
    @FXML private TableColumn<Part, Integer> AddProductInvLevelCol;
    @FXML private TextField AddProductsPriceField;
    @FXML private TableColumn<Part, Double> DeleteProductCurrentPriceCol;
    @FXML private TextField AddProductsNameField;
    @FXML private TextArea AddProductsMaxField;
    @FXML private TableColumn<Part, Integer> AddProductPartIDCol;
    @FXML private TableColumn<Part, String> DeleteProductCurrentPartNameCol;
    @FXML private TableView<Part> AssociatedPartsTable;
    @FXML private TableView<Part> partsTable;

    private final ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private final ObservableList<Part> allParts = FXCollections.observableArrayList(getAllParts());


    /**
     * @param searchedPart add filtered parts
     */
    public void addFilteredPart(Part searchedPart) {
        filteredParts.add(searchedPart);
    }

    /**
     * @return filtered parts
     */
    public ObservableList<Part> getFilteredParts() {
        return filteredParts;
    }

    @FXML
    void AddProducts_SearchPartBtn(ActionEvent event) {
        if (AddProductAddPartSearchField.getText().isEmpty() || AddProductAddPartSearchField.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a search value");
            alert.showAndWait();
            partsTable.setItems(getAllParts());
        } else {
            filteredParts.clear();
            Inventory.filteredParts.clear();
            try {
                addFilteredPart(Inventory.lookupPart(Integer.parseInt(AddProductAddPartSearchField.getText())));
                partsTable.setItems(getFilteredParts());
            } catch (Exception e) {
                Inventory.lookupPart(AddProductAddPartSearchField.getText());
                partsTable.setItems(Inventory.getFilteredPart());
            }
            Alert alert = new Alert(Alert.AlertType.ERROR, "Part not found");
            alert.showAndWait();
        }
    }

    @FXML
    void AddProduct_AddPartBtn(ActionEvent event) {
        Part addPart = partsTable.getSelectionModel().getSelectedItem();
        associatedParts.add(addPart);
        updateAssociatedPartsTable();
    }

/*        if (addPart != null) {
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
    }*/
/*        Part addPart = this.partsTable.getSelectionModel().getSelectedItem();
        associatedParts.add(addPart);
        allParts.remove(addPart);
        partsTable.setItems(allParts);
        updateAssociatedPartsTable();*/
    private void updateAssociatedPartsTable() {
        AssociatedPartsTable.setItems(associatedParts);
    }
    private void updateAllPartsTable(){
        partsTable.setItems(getAllParts());
    }

    @FXML
    void AddProduct_SaveBtn(ActionEvent event) throws IOException {
        String name = AddProductsNameField.getText();
        String price = AddProductsPriceField.getText();
        String inv = AddProductsInvField.getText();
        String max = AddProductsMaxField.getText();
        String min = AddProductsMinField.getText();

        try {
            int tempAssociatedParts = associatedParts.size();
            Double renderPrice = Double.parseDouble(price);
            Integer renderInv = Integer.parseInt(inv);
            Integer renderMax = Integer.parseInt(max);
            Integer renderMin = Integer.parseInt(min);

            String isValid = Product.productValidation(name, renderPrice, renderInv, renderMax, renderMin, tempAssociatedParts);
            if (isValid == null) {
                Product tempProduct = new Product();
                int productId = Product.getIdCount();
                tempProduct.setId(productId);
                tempProduct.setName(name);
                tempProduct.setPrice(renderPrice);
                tempProduct.setStock(renderInv);
                tempProduct.setMax(renderMax);
                tempProduct.setMin(renderMin);
                tempProduct.setAssociatedParts(associatedParts);
                Inventory.addProduct(tempProduct);

                Stage stage = (Stage) AddProduct_Save.getScene().getWindow();
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
    void AddProduct_DeletePartEvent(ActionEvent event) {
        Part deletePart = partsTable.getSelectionModel().getSelectedItem();
        if(deletePart != null){
            associatedParts.remove(deletePart);
            AssociatedPartsTable.setItems(associatedParts);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText("Please select a part");
            alert.showAndWait();
        }
    }

    @FXML
    void AddProduct_CancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will remove all text field values, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AddProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AddProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProductInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AddProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(allParts);
        DeleteProductCurrentPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        DeleteProductCurrentPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        DeleteProductCurrentInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        DeleteProductCurrentPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedPartsTable.setItems(associatedParts);
        updateAllPartsTable();
        updateAssociatedPartsTable();
    }

}




