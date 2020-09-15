package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private static ObservableList <Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
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
    public static void addAssociatedPart(Part associatedPart){
        associatedParts.add(associatedPart);
    }

    public static boolean deleteAssociatedPart(int id){
        for(Part p : associatedParts){
            if(p.getId() == id){
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }
    public static void getAllAssociatedParts(){

    }
}
