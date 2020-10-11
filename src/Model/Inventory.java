package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    private static ObservableList <Part> allParts = FXCollections.observableArrayList();
    private static ObservableList <Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @return all parts
     */
    public static ObservableList <Part> getAllParts() {
        return allParts;
    }

    /**
     *
     * @return all products
     */
    public static ObservableList <Product> getAllProducts() {

        return allProducts;
    }

    /**
     *
     * @param part
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     *
     * @param product
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     *
     * @param
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     *
     * @param index
     * @param product
     */
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }

    /**
     *
     * @param
     * @return if true - delete part
     */
    public static void deletePart(Part selectedParts){
        allParts.remove(selectedParts);
    }

    /**
     *
     * @param selectedProduct
     * @return
     */
    public static void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }


    /**
     *
     * @param part
     * @return
     */
    public static boolean partValidateDelete(Part part) {
        boolean isFound = false;
        for (Product allProduct : allProducts) {
            if (allProduct.getAssociatedParts().contains(part)) {
                isFound = true;
            }
        }
        return isFound;
    }

    /**
     *
     * @param product
     * @return
     * check if product has associated parts
     */
    public static boolean productValidateDelete(Product product) {
        boolean isFound = false;
        int productID = product.getId();
        for (Product allProduct : allProducts) {
            if (allProduct.getId() == productID) {
                if (!allProduct.getAssociatedParts().isEmpty()) {
                    isFound = true;
                }
            }
        }
        return isFound;
    }
    //looking up parts
    public static ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    public static void newFilteredPart(Part part){
        filteredParts.add(part);
    }
    public static ObservableList<Part> getFilteredPart(){
        return filteredParts;
    }

    /**
     *
     * @param partID
     * @return
     */
    public static Part lookupPart(int partID) {
       for(Part part: allParts){
            if(part.getId() == partID){
                return part;
            }
       }
       return null;
    }

    /**
     *
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName){
        for (Part part : allParts){
            if (partName.toLowerCase().equals(part.getName().toLowerCase())){
                Inventory.newFilteredPart(part);
            }
        }
        return filteredParts;
    }

    //looking up products
    public static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    /**
     *
     * @param product add
     */
    public static void newFilteredProduct(Product product){
        filteredProducts.add(product);
    }

    /**
     *
     * @return filteredProducts
     */
    public static ObservableList<Product> getFilteredProduct(){
        return filteredProducts;
    }

    /**
     *
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId){
        for(Product searchProduct: allProducts){
            if(searchProduct.getId() == productId){
                return searchProduct;
            }
        }
        return null;
    }

    /**
     *
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> tempList = FXCollections.observableArrayList();
        for (Product product : allProducts){
            if (productName.compareTo(product.getName()) == 0){
                tempList.add(product);
            }
        }
        return tempList;
    }

}