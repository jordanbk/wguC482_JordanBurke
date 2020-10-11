package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private static ObservableList <Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;

    public Product(int id, String name, double price, int stock, int max, int min) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = max;
        this.max = min;
    }

    public Product() {

    }

    //getters

    /**
     *
     * @return associated parts
     */
    public static ObservableList <Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     *
     * @return product ID
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return current inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return minimum required inventory
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return maximum required inventory
     */
    public int getMax() {
        return max;
    }


    //setters

    /**
     *
     * @param associatedParts set associated parts
     */
    public static void setAssociatedParts(ObservableList <Part> associatedParts) {
        Product.associatedParts = associatedParts;
    }

    /**
     *
     * @param id set product ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param name set product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price set product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @param stock set inventory
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     *
     * @param min set minium required inventory
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     *
     * @param max set max inventory
     */
    public void setMax(int max) {
        this.max = max;
    }

    //methods

    /**
     *
     * @param associatedPart
     */
    public void addAssociatedPart(Part associatedPart){
        this.associatedParts.add(associatedPart);
    }

    public void deleteAssociatedPart(Part id){
        associatedParts.remove(id);

    }
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    public Part lookupAssocParts(int partID)
    {
        for (Part assocParts: associatedParts) {
            if (assocParts.getId() == partID) {
                return assocParts;
            }
        }
        return null;
    }
    private static int idCount = 0;
    private static String error;
    public static int getIdCount() {
        idCount++;
        return idCount;
    }

    /**
     *
     * @param name
     * @param price
     * @param stock
     * @param max
     * @param min
     * @param parts
     * @return error
     */
    public static String productValidation(String name, double price, int stock,  int max, int min, int parts){
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
        if(parts == 0){
            error = "Products must have at least one part";
        }

        return error;
    }

}
