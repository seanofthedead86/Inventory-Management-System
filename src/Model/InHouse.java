package Model;

/** In House is an extension of Part
* Adds Machine ID
*/
public class InHouse extends Model.Part {

    private int machineId;

    /**Constructor
     * @param id part id
     * @param machineID machineID
     * @param name part name
     * @param min stock min
     * @param max stock max
     * @param price part price
     * @param stock inventory */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineId = machineID;
    }

    /**Getter*/
    public int getMachineId() {
        return machineId;
    }
    /**Setter*/
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
