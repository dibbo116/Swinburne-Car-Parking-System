import java.util.*;

/**
 * The CarPark class manages all parking slots in the car park system.
 * Provides methods to add, remove, and find parking slots and cars.
 * 
 * @author [Dibbo Barua Chamak] [105299366]
 * @version 1.0 - May 18, 2025
 */

public class CarPark {
    /** Stores all parking slots, keyed by slot ID */
    private HashMap<String, ParkingSlot> slots = new HashMap<>();

    /**
     * Adds a new parking slot to the car park.
     * @param slot The ParkingSlot to add
     * @return true if slot was added, false if slot ID already exists
     */
    public boolean addSlot(ParkingSlot slot) {
        if (!slots.containsKey(slot.getSlotId())) {
            slots.put(slot.getSlotId(), slot);
            return true;
        }
        return false;
    }

    /**
     * Removes a parking slot from the car park (only if not occupied).
     * @param slotId The ID of the slot to remove
     * @return true if slot was removed, false if slot does not exist or is occupied
     */
    public boolean removeSlot(String slotId) {
        ParkingSlot slot = slots.get(slotId);
        if (slot != null && !slot.isOccupied()) {
            slots.remove(slotId);
            return true;
        }
        return false;
    }

    /**
     * Gets a ParkingSlot by its slot ID.
     * @param slotId The slot ID to search for
     * @return The ParkingSlot object, or null if not found
     */
    public ParkingSlot getSlot(String slotId) {
        return slots.get(slotId);
    }

    /**
     * Gets a collection of all parking slots.
     * @return Collection of all ParkingSlot objects
     */
    public Collection<ParkingSlot> getAllSlots() {
        return slots.values();
    }

    /**
     * Finds which slot a car (by registration number) is parked in.
     * @param regNumber The car's registration number
     * @return The ParkingSlot where the car is parked, or null if not found
     */
    public ParkingSlot findCar(String regNumber) {
        for (ParkingSlot slot : slots.values()) {
            if (slot.isOccupied() &&
                slot.getParkedCar().getRegistrationNumber().equals(regNumber)) {
                return slot;
            }
        }
        return null;
    }
}
