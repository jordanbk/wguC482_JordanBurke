package Model;

public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int max;
    private int min;

    public Part(int id, String name, double price, int stock, int max, int min) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.max = max;
        this.min = min;
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

}
