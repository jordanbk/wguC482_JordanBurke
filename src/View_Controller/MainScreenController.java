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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Model.Inventory.*;

public class MainScreenController implements Initializable {

    @FXML
    private TableColumn<Product, String> productName_Col;

    @FXML
    private TableView<Product> MainProductsTableView;

    @FXML
    private AnchorPane mainProducts;

    @FXML
    private Button MainModifyPartsButton;

    @FXML
    private AnchorPane mainParts;

    @FXML
    private TableColumn<Part, Integer> partID_Col;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private TableColumn<Product, Double> productPrice_Col;

    @FXML
    private TextField MainPartsSearchField;

    @FXML
    private Button MainAddPartsButton;

    @FXML
    private TableColumn<Part, Double> partPrice_Col;

    @FXML
    private TableColumn<Part, String> partName_Col;

    @FXML
    private TableColumn<Part, Integer> partInv_Col;

    @FXML
    private TableColumn<Product, Integer> productInv_Col;

    @FXML
    private TextField MainProductsSearchField;

    @FXML
    private TableView <Part> MainPartsTableView;

    @FXML
    private TableColumn<Product, Integer> productID_Col;

    private Inventory currentInventory;
    private static ObservableList<Part> newParts = FXCollections.observableArrayList();
    private static ObservableList<Product> newProducts = FXCollections.observableArrayList();


    public void addNewProducts(Product productSearched){
        newProducts.add(productSearched);
    }
    public ObservableList<Product> getNewProducts(){
        return newProducts;
    }


    //search part
    @FXML
       private void MainSearchPart(ActionEvent event) {
          if (MainPartsSearchField.getText().isEmpty()) {
              MainPartsTableView.setItems(Inventory.getAllParts());
          }
          else {
              filteredParts.clear();
              try {
                  newFilteredPart(Inventory.lookupPart(Integer.parseInt(MainPartsSearchField.getText())));
                  MainPartsTableView.setItems(getFilteredPart());
              }
              catch(Exception e) {
                  Inventory.lookupPart(MainPartsSearchField.getText());
                  MainPartsTableView.setItems(Inventory.getFilteredPart());
              }
          }
    }
    //add part
    @FXML
    private AddPartController MainAddPart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddParts.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stageOne = (Stage)((Node)event.getSource()).getScene().getWindow();
        stageOne.setScene(scene);
        stageOne.show();
        return loader.getController();
    }
    //delete part
    @FXML
    private void MainDeletePart(ActionEvent event) {

        Part part = MainPartsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Delete Part");
        alert.setHeaderText("Confirm delete?");
        alert.setContentText("Are you sure you want to delete " + part.getName());
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            deletePart(part);
            updatePartsTableView();
            System.out.println("Part " + part.getName() + " was removed.");
        }

    }

    //modify parts
    private static int modifyPartsIndex;
    private static int modifyProductsIndex;
    public static Part modifiedPart;
    public static Product modifiedProduct;
    public static int partsToModList(){
        return modifyPartsIndex;
    }
    public static int productsToModList(){
        return modifyProductsIndex;
    }
    public static Product getModifiedProduct() {
        return modifiedProduct;
    }
    public static int getModifyPartsIndex(){
        return modifyPartsIndex;
    }
    @FXML
     void MainModifyPart(ActionEvent event) throws IOException{
        modifiedPart = MainPartsTableView.getSelectionModel().getSelectedItem();
        if(modifiedPart != null) {
            modifyProductsIndex = getAllProducts().indexOf(modifiedPart);
            Parent modifyPartParent = FXMLLoader.load(getClass().getResource("ModifyParts.fxml"));
            Scene modifyPartScene = new Scene(modifyPartParent);
            Stage modifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyPartStage.setScene(modifyPartScene);
            modifyPartStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("A part must be selected");
            alert.showAndWait();
        }
    }

    //modify products
    @FXML
    private void MainModifyProduct(ActionEvent event) throws IOException {
        modifiedProduct = MainProductsTableView.getSelectionModel().getSelectedItem();
        if(modifiedProduct != null) {
                modifyProductsIndex = getAllProducts().indexOf(modifiedProduct);
                Parent modifyProductParent = FXMLLoader.load(getClass().getResource("ModifyProducts.fxml"));
                Scene modifyProductScene = new Scene(modifyProductParent);
                Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                modifyProductStage.setScene(modifyProductScene);
                modifyProductStage.show();
        } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("A part must be selected");
                alert.showAndWait();
        }

    }

    //exit program
    @FXML
    private void MainExitClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Exit Confirmation");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
        else {
            System.out.println("Canceled.");
        }
    }

    //delete product
    @FXML
    private void MainDeleteProduct(ActionEvent event) {
        Product product = MainProductsTableView.getSelectionModel().getSelectedItem();
        if(productValidateDelete(product)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Product cannot be deleted");
            alert.setContentText("Product contains one or more parts.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Deletion");
            alert.setHeaderText("Confirm Delete?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deleteProduct(product);
                updateProductsTableView();
            }
            else{
                Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION);
                newAlert.initModality(Modality.NONE);
                newAlert.setTitle("Select a product");
                newAlert.setHeaderText("Please select a product");
            }
        }
    }

    //add product
    @FXML
    public AddProductController MainAddProduct(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProducts.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        Stage stageOne = (Stage)((Node)event.getSource()).getScene().getWindow();
        stageOne.setScene(scene);
        stageOne.show();
        return loader.getController();
    }



    //update parts/products
    public void updatePartsTableView() {
        MainPartsTableView.setItems(getAllParts());
    }

    public void updateProductsTableView() {
        MainProductsTableView.setItems(Inventory.getAllProducts());
    }

    //search products
    @FXML
    void MainSearchProduct(ActionEvent event) {
        if (MainProductsSearchField.getText().isEmpty()) {
            MainProductsTableView.setItems(Inventory.getAllProducts());
        }
        else {
            newProducts.clear();
            Inventory.filteredProducts.clear();
            try {
                addNewProducts(Inventory.lookupProduct(Integer.parseInt(MainProductsSearchField.getText())));
                MainProductsTableView.setItems(getNewProducts());
            }
            catch(Exception e) {
                MainProductsTableView.setItems(Inventory.lookupProduct(MainProductsSearchField.getText()));
            }
        }

    }

    public void initialize(URL url, ResourceBundle rb) {
        partID_Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName_Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv_Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice_Col.setCellValueFactory(new PropertyValueFactory<>("price"));
        productID_Col.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName_Col.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInv_Col.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice_Col.setCellValueFactory(new PropertyValueFactory<>("price"));
        MainPartsTableView.setItems(Inventory.getAllParts());
        MainProductsTableView.setItems(Inventory.getAllProducts());

    }

}
