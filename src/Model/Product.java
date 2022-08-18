package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Calls for creating products*/
public class Product {

    /**AssociatedParts observable list*/
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**Product constructor
     * @param id product id
     * @param name product name
     * @param min min stock
     * @param max max stock
     * @param price product price
     * @param stock inventory*/
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**Set method for associated parts
     * @param associatedParts setting associated parts*/
    public void setAssociatedParts(ObservableList <Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /**Get ID*/
    public int getId() {
        return id;
    }

    /**Set Id
     * @param id set id*/
    public void setId(int id) {
        this.id = id;
    }

    /**get name*/
    public String getName() {
        return name;
    }

    /**Set name
     * @param name set name*/
    public void setName(String name) {
        this.name = name;
    }

    /**Get price*/
    public double getPrice() {
        return price;
    }

    /**Set price
     * @param price set price*/
    public void setPrice(double price) {
        this.price = price;
    }

    /**Get stock*/
    public int getStock() {
        return stock;
    }

    /**Set stock
     * @param stock set stock*/
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**Get min*/
    public int getMin() {
        return min;
    }

    /**Set min
     * @param min set min*/
    public void setMin(int min) {
        this.min = min;
    }

    /**Get max*/
    public int getMax() {
        return max;
    }

    /**Set max
     * @param max set max*/
    public void setMax(int max) {
        this.max = max;
    }

    /**Add associated part
     * @param part part being added to as associated part*/
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**Delete method for associatedParts
     * @param partID part ID of part being deleted*/
    public boolean deleteAssociatedPart(int partID) {
        for (Part part : associatedParts) {
            if(part.getId() == partID) {
                associatedParts.remove(part);
                return true;
            }
        }
        return false;
    }

    /**Get all associatedParts. Returns associatedParts*/
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
