import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Car class represents a car that can be parked in the car park system.
 * It records car registration details, owner name, car type, and parking time.
 * 
 * @author [Dibbo Barua Chamak] [105299366]
 * @version 1.0 - May 20, 2025
 */

public class Car {
    /** The car's registration number (e.g. "V12345") */
    private String registrationNumber;

    /** The owner's name of the car */
    private String owner;

    /** True if the owner is staff, false if visitor */
    private boolean isStaff;

    /** The time the car was parked */
    private LocalDateTime parkTime;

    /**
     * Constructor for Car object.
     * Records registration number, owner's name, staff/visitor, and the park time.
     * 
     * @param registrationNumber Car registration number
     * @param owner The name of the car owner
     * @param isStaff True if staff, false if visitor
     */
    public Car(String registrationNumber, String owner, boolean isStaff) {
        this.registrationNumber = registrationNumber;
        this.owner = owner;
        this.isStaff = isStaff;
        this.parkTime = LocalDateTime.now();
    }

    /**
     * Gets the car's registration number.
     * 
     * @return Registration number (String)
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Gets the car owner's name.
     * 
     * @return Owner's name (String)
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Checks if the car owner is staff.
     * 
     * @return True if staff, false if visitor
     */
    public boolean isStaff() {
        return isStaff;
    }

    /**
     * Gets the time the car was parked.
     * 
     * @return Park time (LocalDateTime)
     */
    public LocalDateTime getParkTime() {
        return parkTime;
    }

    /**
     * Gets the formatted park time as a string.
     * 
     * @return Park time in 'yyyy-MM-dd HH:mm:ss' format
     */
    public String getFormattedParkTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return parkTime.format(formatter);
    }
}
