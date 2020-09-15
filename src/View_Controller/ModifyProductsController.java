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
import static Model.Inventory.getAllProducts;
import static View_Controller.MainScreenController.productsToModList;
import static java.lang.Integer.parseInt;

public class ModifyProductsController implements Initializable {
    private ObservableList<Part> currentPartsList = FXCollections.observableArrayList();
    private int productIndex = productsToModList();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private TextField ModifyProductsInvField;

    @FXML
    private TextField ModifyProduct_SearchField;

    @FXML
    private TextArea ModifyProductsMinField;

    @FXML
    private TableColumn<Part, Double> ModifyProductCurrentPartPriceCol;

    @FXML
    private TableColumn<Part, Double> ModifyProductPartPriceCol;

    @FXML
    private TextField ModifyProductsNameField;

    @FXML
    private TextField ModifyProductsIDField;

    @FXML
    private TextField ModifyProductsPriceField;

    @FXML
    private TableColumn<Part, Integer> AddProductInvLevelCol;

    @FXML
    private TableColumn<Part, Integer> ModifyProductPartIDCol;

    @FXML
    private TableView<Part> AddPartSearchTable;

    @FXML
    private TextArea ModifyProductsMaxField;

    @FXML
    private TableView<Part> associatedPartsTable;

    @FXML
    private TableColumn<Part, String> ModifyProductPartNameCol;

    @FXML
    private TableColumn<Part, String> ModifyProductCurrentPartNameCol;

    @FXML
    private TableColumn<Part, Integer> ModifyProductCurrentPartInvCol;

    @FXML
    private TableColumn<Part, Integer> ModifyProductCurrentPartIDCol;
    Product newProduct = new Product(0, null, 0.0, 0, 0, 0);
    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();

    public void addFilteredPart(Part searchedPart){
        filteredParts.add(searchedPart);
    }

    public ObservableList<Part> getFilteredParts(){
        return filteredParts;
    }

    public void updateAssociatedParts(){
        for(int i = 0; i < currentPartsList.size(); i++){
            newProduct.addAssociatedPart(currentPartsList.get(i));
        }
    }
    @FXML
    void ModifyProduct_SearchPartBtn(ActionEvent event) {
        if (ModifyProduct_SearchField.getText().isEmpty()) {
            AddPartSearchTable.setItems(Inventory.getAllParts());
        }
        else {
            filteredParts.clear();
            Inventory.filteredParts.clear();
            try{
                addFilteredPart(Inventory.lookupPart(parseInt(ModifyProduct_SearchField.getText())));

                AddPartSearchTable.setItems(getFilteredParts());
            }
            catch (Exception e){
                Inventory.lookupPart(ModifyProduct_SearchField.getText());

                AddPartSearchTable.setItems(Inventory.getFilteredPart());
            }
        }
    }

    @FXML
    void ModifyProduct_AddPartBtn(ActionEvent event) {
        Part parts = AddPartSearchTable.getSelectionModel().getSelectedItem();
        currentPartsList.add(parts);
        populateAddPartsTableView();
    }

    private void populateAddPartsTableView() {
        AddPartSearchTable.setItems(getAllParts());
    }

    public void populateAssociatedTableView() {
        associatedPartsTable.setItems(currentPartsList);
    }

    @FXML
    void ModifyProduct_SaveBtn(ActionEvent event) throws IOException{
        newProduct.setId(Integer.parseInt(ModifyProductsIDField.getText()));
        newProduct.setName(ModifyProductsNameField.getText());
        newProduct.setStock(Integer.parseInt(ModifyProductsInvField.getText()));
        newProduct.setPrice(Double.parseDouble(ModifyProductsPriceField.getText()));
        newProduct.setMax(Integer.parseInt(ModifyProductsMaxField.getText()));
        newProduct.setMin(Integer.parseInt(ModifyProductsMinField.getText()));
        try {
            if (newProduct.getMin() <= newProduct.getMax()){

                Inventory.updateProduct(selectedIndex, newProduct);

            }
            else
                throw new Exception();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.show();
            alert.setTitle("Warning Dialog");
            alert.setContentText("Min value cannot be greater than max value.");
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    Product productSelected;
    int selectedIndex;
    public Product getProductSelection() {
        return productSelected;

    }

    public void setSelectedProduct(Product product, int index) {
        productSelected = product;
        selectedIndex = index;

        if (product instanceof Product) {
            Product newProduct = product;

            this.ModifyProductsIDField.setText(Integer.toString(newProduct.getId()));
            this.ModifyProductsNameField.setText(newProduct.getName());
            this.ModifyProductsInvField.setText((Integer.toString(newProduct.getStock())));
            this.ModifyProductsPriceField.setText((Double.toString(newProduct.getPrice())));
            this.ModifyProductsMaxField.setText((Integer.toString(newProduct.getMax())));
            this.ModifyProductsMinField.setText((Integer.toString(newProduct.getMin())));

            associatedPartsTable.setItems(newProduct.getAssociatedParts());
            currentPartsList = newProduct.getAssociatedParts();
            updateAssociatedParts();
        }
    }


    @FXML
    void ModifyProduct_CancelBtn(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete all text, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    public void ModifyProduct_DeletePartBtn(ActionEvent actionEvent) {
        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();
        Alert alertA = new Alert(Alert.AlertType.CONFIRMATION);
        alertA.initModality(Modality.NONE);
        alertA.setTitle("Part Deletion");
        alertA.setHeaderText("Confirm");
        alertA.setContentText("Are you sure you want to delete " + part.getName() + " from parts?");
        Optional<ButtonType> resultA = alertA.showAndWait();
        if (resultA.get() == ButtonType.OK) {
            currentPartsList.remove(part);
        } else {
            System.out.println("You clicked cancel.");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ModifyProductsIDField.setText(String.valueOf(getAllParts().size() + 1));
        productID = getAllProducts().get(productIndex).getId();
        Product product = getAllProducts().get(productIndex);
        ModifyProductsIDField.setText("Auto-Gen: " + productID);
        ModifyProductsNameField.setText(product.getName());
        ModifyProductsInvField.setText(Integer.toString(product.getStock()));
        ModifyProductsPriceField.setText(Double.toString(product.getPrice()));
        ModifyProductsMaxField.setText(Integer.toString(product.getMin()));
        ModifyProductsMinField.setText(Integer.toString(product.getMax()));
        currentPartsList = Product.getAssociatedParts();
        ModifyProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProductInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        ModifyProductCurrentPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductCurrentPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProductCurrentPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductCurrentPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        populateAddPartsTableView();
        populateAssociatedTableView();
    }
}








