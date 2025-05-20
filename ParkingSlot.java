/**
 * The ParkingSlot class represents a single parking slot in the car park.
 * Each slot has an ID, a type (staff or visitor), and may have a car parked in it.
 * 
 * @author [Dibbo Barua Chamak] [105299366]
 * @version 1.0 - May 20, 2025
 */

public class ParkingSlot {
    /** The unique identifier for the slot (e.g., "V001" or "S002") */
    private String slotId;

    /** The type of the slot: "staff" or "visitor" */
    private String type;

    /** The car currently parked in this slot, or null if empty */
    private Car parkedCar;

    /**
     * Constructor for ParkingSlot.
     * @param slotId The unique identifier for the slot
     * @param type The type of slot ("staff" or "visitor")
     */
    public ParkingSlot(String slotId, String type) {
        this.slotId = slotId;
        this.type = type;
        this.parkedCar = null; // Slot is initially empty
    }

    /**
     * Gets the slot ID.
     * @return The slot's unique ID (String)
     */
    public String getSlotId() {
        return slotId;
    }

    /**
     * Gets the type of the slot.
     * @return "staff" or "visitor"
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the car currently parked in this slot.
     * @return The Car object, or null if the slot is empty
     */
    public Car getParkedCar() {
        return parkedCar;
    }

    /**
     * Checks if the slot is currently occupied.
     * @return true if a car is parked, false otherwise
     */
    public boolean isOccupied() {
        return parkedCar != null;
    }

    /**
     * Parks a car in this slot.
     * @param car The Car to park
     */
    public void parkCar(Car car) {
        this.parkedCar = car;
    }

    /**
     * Removes the car from this slot (slot becomes empty).
     */
    public void removeCar() {
        this.parkedCar = null;
    }
}
