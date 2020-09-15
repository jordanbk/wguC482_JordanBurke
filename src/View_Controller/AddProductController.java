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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.getAllParts;

public class AddProductController implements Initializable {

    @FXML
    private TextArea AddProductsMinField;

    @FXML
    private Button AddProduct_DeletePart;

    @FXML
    private TextField AddProductsIDField;

    @FXML
    private TextField AddProductsInvField;

    @FXML
    private TableColumn<Part, Integer> DeleteProductCurrentPartIDCol;

    @FXML
    private TextField AddProductAddPartSearchField;

    @FXML
    private TableColumn<Part, Integer> DeleteProductCurrentInvCol;

    @FXML
    private TableView<Product> AddProductsAddTableView;

    @FXML
    private TableColumn<Part, String> AddProductPartNameCol;

    @FXML
    private Button AddProduct_AddPart;

    @FXML
    private TableView<Part> AssociatedPartsTable;

    @FXML
    private TableView<Part> partsTable;

    @FXML
    private TableColumn<Part, Double> AddProductPriceCol;

    @FXML
    private TableColumn<Part, Integer> AddProductInvLevelCol;

    @FXML
    private TextField AddProductsPriceField;

    @FXML
    private Button SearchPartsInAddProduct;

    @FXML
    private Button AddProduct_Cancel;

    @FXML
    private TableColumn<Part, Double> DeleteProductCurrentPriceCol;

    @FXML
    private Button AddProduct_Save;

    @FXML
    private TextField AddProductsNameField;

    @FXML
    private TextArea AddProductsMaxField;

    @FXML
    private TableView<Part> AllPartsTable;

    @FXML
    private TableView<Part> AddProductsDeleteTableView;

    @FXML
    private TableColumn<Part, Integer> AddProductPartIDCol;

    @FXML
    private TableColumn<Part, String> DeleteProductCurrentPartNameCol;

    @FXML
    private Button AddProduct_Delete;

    @FXML
    private TableView<Part> AddProduct_AssociatedPartsTable;

    @FXML
    private TableView<Part> AddProduct_SearchPartTable;

    private ObservableList<Part> productParts = FXCollections.observableArrayList();

    private int productID;

    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    public void addFilteredPart(Part searchedPart){
        filteredParts.add(searchedPart);
    }

    public ObservableList<Part> getFilteredParts(){
        return filteredParts;
    }

    @FXML
     void AddProducts_SearchPartBtn(ActionEvent event) throws IOException {
        if (AddProductAddPartSearchField.getText().isEmpty()) {
            partsTable.setItems(Inventory.getAllParts());
        }
        else {
            filteredParts.clear();
            Inventory.filteredParts.clear();
            try{
                addFilteredPart(Inventory.lookupPart(Integer.parseInt(AddProductAddPartSearchField.getText())));
                partsTable.setItems(getFilteredParts());
            }

            catch (Exception e){
                Inventory.lookupPart(AddProductAddPartSearchField.getText());

                partsTable.setItems(Inventory.getFilteredPart());
            }
        }

      }

    @FXML
    void AddProduct_AddPartBtn(ActionEvent event) {
        Part part = AllPartsTable.getSelectionModel().getSelectedItem();
        productParts.add(part);

    }
 
    @FXML
    void AddProduct_DeletePartEvent(ActionEvent event) {
        Part part = partsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete a Part");
        alert.setHeaderText("Please Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getName() + " from part list?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Part has been deleted.");
            productParts.remove(part);
        }
        else {
            System.out.println("Part deletion cancelled.");
        }
    }


    @FXML
    void AddProduct_DeleteBtn(ActionEvent event) {

    }

    Product newProduct = new Product();

    @FXML
    void AddProduct_SaveBtn(ActionEvent event) throws IOException {
        // id auto generator
        int counter = 1;
        int newId = 1;

        for (Product product : Inventory.getAllProducts()) {
            counter = product.getId();
            counter++;
        }
        newId = counter;

        newProduct.setId(newId);
        newProduct.setName(AddProductsNameField.getText());
        newProduct.setStock(Integer.parseInt(AddProductsInvField.getText()));
        newProduct.setPrice(Double.parseDouble(AddProductsPriceField.getText()));
        newProduct.setMax(Integer.parseInt(AddProductsMaxField.getText()));
        newProduct.setMin(Integer.parseInt(AddProductsMinField.getText()));

        try {
            if (newProduct.getMin() <= newProduct.getMax()){
                Inventory.addProduct(newProduct);
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
            else
                throw new Exception();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.show();
            alert.setTitle("Warning Dialog");
            alert.setContentText("Min value cannot be greater than max value.");
        }

    }

    @FXML
    void AddProduct_CancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will remove all text field values, do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
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
        DeleteProductCurrentPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        DeleteProductCurrentPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        DeleteProductCurrentInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        DeleteProductCurrentPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(getAllParts());
        AssociatedPartsTable.setItems(getAllParts());
        AddProductsIDField.setText(String.valueOf(Inventory.getAllProducts().size() + 1));



    }



}
