package Model;

public class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    //getters

    /**
     *
     * @return part ID
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return part name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return part price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return part inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return min required inventory
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return max inventory
     */
    public int getMax() {
        return max;
    }

    //setters

    /**
     *
     * @param id set part ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param name set part name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param price set part price
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
     * @param min set min required inventory
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
    public static String isPartValid(String name, int min, int max, int inv, double price, String errorMessage){
        if (name == null) {
            errorMessage = errorMessage + "The name field is required. ";
        }
        if (inv < 1) {
            errorMessage = errorMessage + "The inventory count cannot be less than 1. ";
        }
        if (price <= 0) {
            errorMessage = errorMessage + "The price must be greater than $0. ";
        }
        if (max < min) {
            errorMessage = errorMessage + "The Max must be greater than or equal to the Min. ";
        }
        if (inv < min || inv > max) {
            errorMessage = errorMessage + "The inventory must be between the Min and Max values. ";
        }
        return errorMessage;
    }

}
