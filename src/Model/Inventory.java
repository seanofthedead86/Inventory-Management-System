package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**Inventory of parts and products with getters and setters*/
public class Inventory {

    /** Part and Products observableList*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /*PART METHODS*/

    /** Adds new part to allParts observable list
    * Returns part if found, otherwise it returns null
    * @param newPart new part being added
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /** Search for part based on partID
     * @param partID part ID being searched*/
    public static Part lookupPart(int partID){
        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partID)
                return allParts.get(i);
        }

        return null;
    }

    /** Search part based on name
    *  Returns part if found, otherwise it returns null
     * @param partName part name being searched
    */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> lookupList = FXCollections.observableArrayList();

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getName().toUpperCase().contains(partName.toUpperCase()))
                lookupList.add(allParts.get(i));
        }

        return lookupList;
    }

    /** Update a selected part based on the part's index
     * @param selectedPart part being updated
     * @param Index part index*/
    public static void updatePart(int Index, Part selectedPart){
        allParts.set(Index, selectedPart);
    }

    /** Removes part from observable list
     * @param part  part being removed*/
    public static void deletePart(Part part) {
        allParts.remove(part);
    }

    //PRODUCTS

    /** Adds product to allProducts observable list
     * @param newProduct new product being added*/
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /** Searches part based on productID
    * Returns product if found, otherwise it returns null
     * @param productID product ID being searched
     */
    public static Product lookupProduct(int productID){
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productID)
                return allProducts.get(i);
        }
        return null;
    }

    /** Search products based on product name
    * Returns product if found, otherwise it returns null
     * @param productName
    */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> lookupList = FXCollections.observableArrayList();

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName().toUpperCase().contains(productName.toUpperCase()))
                lookupList.add(allProducts.get(i));
        }

        return lookupList;
    }

    /** Update a selected product based on the part's index
     * @param Index product index
     * @param selectedProduct selected product*/
    public static void updateProduct(int Index, Product selectedProduct){
        allProducts.set(Index, selectedProduct);
    }

    /** Removes part from the observable list
     * @param product product being removed*/
    public static void deleteProduct(Product product){
        allProducts.remove(product);
    }

    /**Return allParts */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**Return allProducts*/
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
