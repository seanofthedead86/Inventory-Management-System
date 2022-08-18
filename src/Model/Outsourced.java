package Model;

/** Outsourced is an extension of Part
 * Adds Company Name
 */
public class Outsourced extends Part {

    private String companyName;

    /**Constructor
     * @param stock inventory
     * @param price price
     * @param max max inv
     * @param min min inv
     * @param name part name
     * @param id part id
     * @param companyName company name*/
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**Getter*/
    public String getCompanyName() {
        return companyName;
    }

    /**Setter
     * @param companyName set company name*/
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
