import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Main GUI Application for the Swinburne Car Park System.
 *
 *   This class provides the graphical user interface and all event handling for
 *   managing staff and visitor parking slots, parking and removing cars, and displaying
 *   parking slot status with full color coding and proper validation.
 *
 * @author [Dibbo Barua Chamak] [105299366]
 * @version 1.0 - May 20, 2025
 */

public class Application {
    /** The main application window that contains all panels and components */
    private JFrame frame;

    /** Panel displaying the visitor parking slot buttons (top row) */
    private JPanel visitorSlotPanel;

    /** Panel displaying the staff parking slot buttons (bottom row) */
    private JPanel staffSlotPanel;

    /** Label at the bottom of the window to display status and feedback messages */
    private JLabel statusLabel;

    /** The backend model that manages all parking slots and car information */
    private CarPark carPark;

    /** A map linking each slot ID to its corresponding JButton for quick updates */
    private HashMap<String, JButton> slotButtons;

    /**
     * Constructs the main Application.
     * Initializes the backend car park model, sets up the slot button mapping,
     * builds the entire GUI, and refreshes the slot display to match the data.
     */
    public Application() {
        carPark = new CarPark();        // Create a new CarPark to hold all slots and cars
        slotButtons = new HashMap<>();  // Prepare the map to track slot IDs and their buttons
        initGUI();                      // Set up and display the GUI components
        refreshSlots();                 // Draw any slots (empty at startup)
    }

    /**
     * Initializes and arranges all GUI components for the main application window.
     * 
     * This includes building the title area, slot grid display, action buttons,
     * status bar, and wiring up all event handlers.
     * Ensures clear color-coding, logical layouts, readable fonts, and a user-friendly interface.
     */
    private void initGUI() {
        // === Main Frame Setup ===
        frame = new JFrame("Swinburne Car Park System"); // Main application window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close program when window is closed
        frame.setSize(1050, 730); // Set initial window size
        frame.setLayout(new BorderLayout()); // Use BorderLayout for top, center, bottom regions
    
        // === Title and Image Panel (Top) ===
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(235, 235, 235)); // Light gray background for title
    
        JLabel titleLabel = new JLabel("SWINBURNE CAR PARK SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36)); // Large, bold title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0)); // Spacing above and below title
        titlePanel.add(titleLabel, BorderLayout.NORTH);
    
        // Load and scale the car image for the top center
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/car.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel carLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
        titlePanel.add(carLabel, BorderLayout.CENTER);
    
        // Add the title panel to the top (NORTH) of the window
        frame.add(titlePanel, BorderLayout.NORTH);
    
        // === Slots Grid Area (Center) ===
        JPanel slotsArea = new JPanel();
        slotsArea.setLayout(new GridBagLayout()); // Flexible layout for rows
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = 0; // First row
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch rows to fill width
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around each row
    
        // Visitor slots row (top)
        visitorSlotPanel = new JPanel(new GridLayout(1, 5, 16, 0)); // 1 row, 5 columns, 16px spacing
        visitorSlotPanel.setOpaque(false);
        visitorSlotPanel.setPreferredSize(new Dimension(830, 90));
        slotsArea.add(visitorSlotPanel, gbc); // Place in first row
    
        // Staff slots row (below visitor row)
        gbc.gridy++; // Move to second row
        staffSlotPanel = new JPanel(new GridLayout(1, 5, 16, 0)); // 1 row, 5 columns
        staffSlotPanel.setOpaque(false);
        staffSlotPanel.setPreferredSize(new Dimension(670, 90));
        slotsArea.add(staffSlotPanel, gbc); // Place in second row
    
        // Add slots area to the center of the main frame
        frame.add(slotsArea, BorderLayout.CENTER);
    
        // === Control Buttons Panel (Bottom) ===
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 4, 0)); // 4 rows, 2 columns for 8 buttons
        controlPanel.setPreferredSize(new Dimension(830, 130));
        
        // Create all action buttons
        JButton showAllBtn = new JButton("Show All Parking Spots");
        JButton findCarBtn = new JButton("Find Car");
        JButton parkCarBtn = new JButton("Park Car");
        JButton deleteSlotBtn = new JButton("Delete Spot");
        JButton removeCarBtn = new JButton("Remove Car");
        JButton addSlotBtn = new JButton("Add Parking Spot");
        JButton exitBtn = new JButton("Exit Application");
        JButton clearScreenBtn = new JButton("Clear Screen");
    
        // Set a large, readable font for all buttons
        showAllBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        findCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        parkCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteSlotBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        removeCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        addSlotBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        exitBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        clearScreenBtn.setFont(new Font("Arial", Font.PLAIN, 18));
    
        // Add all buttons to the control panel in order
        controlPanel.add(showAllBtn);
        controlPanel.add(findCarBtn);
        controlPanel.add(parkCarBtn);
        controlPanel.add(deleteSlotBtn);
        controlPanel.add(removeCarBtn);
        controlPanel.add(addSlotBtn);
        controlPanel.add(exitBtn);
        controlPanel.add(clearScreenBtn);
    
        // === Event Handlers for Buttons ===
        showAllBtn.addActionListener(e -> listSlotsDialog());
        findCarBtn.addActionListener(e -> findCarDialog());
        parkCarBtn.addActionListener(e -> parkCarDialog());
        deleteSlotBtn.addActionListener(e -> deleteSlotDialog());
        removeCarBtn.addActionListener(e -> removeCarDialog());
        addSlotBtn.addActionListener(e -> addSlotDialog());
        exitBtn.addActionListener(e -> System.exit(0)); // Quit app
        clearScreenBtn.addActionListener(e -> clearSlotsDisplay());
    
        // === Bottom Panel: Buttons + Status Bar ===
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
    
        // Status bar at the bottom for feedback
        statusLabel = new JLabel("Welcome to Swinburne Car Park System", SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
    
        // Add bottom panel to the main frame (bottom region)
        frame.add(bottomPanel, BorderLayout.SOUTH);
    
        // === Show the Completed Window ===
        frame.setVisible(true);
    }

    
    /**
     * Updates and redraws the visual display of all parking slots.
     * Each slot is shown as a button, colored by type and occupancy.
     * Keeps slot and button logic in sync, and keeps the interface clear and up-to-date.
     */
    private void refreshSlots() {
        visitorSlotPanel.removeAll();
        staffSlotPanel.removeAll();
        for (ParkingSlot slot : carPark.getAllSlots()) {
            JButton btn = new JButton("<html>" + slot.getSlotId() + "<br>" + capitalize(slot.getType()) + "<br>" + (slot.isOccupied() ? "Occupied" : "Vacant") + "</html>");
            btn.setPreferredSize(new Dimension(150, 70));
            btn.setFont(new Font("Arial", Font.PLAIN, 15));
            if (slot.isOccupied()) {
                btn.setBackground(new Color(255, 170, 170));
            } else if (slot.getType().equalsIgnoreCase("staff")) {
                btn.setBackground(new Color(170, 220, 255));
            } else {
                btn.setBackground(new Color(190, 255, 190));
            }
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        // Right click: delete slot if empty
                        if (!slot.isOccupied()) {
                            if (carPark.removeSlot(slot.getSlotId())) {
                                statusLabel.setText("Slot " + slot.getSlotId() + " deleted (right-click).");
                                refreshSlots();
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Cannot delete occupied slot.");
                        }
                    } else {
                        // Left click: park or remove car
                        if (slot.isOccupied()) {
                            int result = JOptionPane.showConfirmDialog(frame, "Remove car from " + slot.getSlotId() + "?", "Remove Car", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION) {
                                slot.removeCar();
                                statusLabel.setText("Car removed from " + slot.getSlotId());
                                refreshSlots();
                            }
                        } else {
                            parkCarDirectly(slot);
                        }
                    }
                }
            });
    
            if (slot.getType().equalsIgnoreCase("staff")) {
                staffSlotPanel.add(btn);
            } else {
                visitorSlotPanel.add(btn);
            }
            slotButtons.put(slot.getSlotId(), btn);
        }
        visitorSlotPanel.revalidate();
        visitorSlotPanel.repaint();
        staffSlotPanel.revalidate();
        staffSlotPanel.repaint();
    }
    
    /**
     * Parks a car directly in the given parking slot after prompting the user for car details.
     * 
     * This helper is typically triggered by left-clicking a slot button. It prompts the user
     * for the car registration number and owner name, validates input, ensures the car isn't
     * already parked, creates the Car object, records the parking time, and displays a dialog
     * confirming the slot and time parked.
     *
     * @param slot The ParkingSlot object where the car will be parked
     */
    private void parkCarDirectly(ParkingSlot slot) {
        // Prompt user for car registration number, require format Letter+5 digits
        String regNum = JOptionPane.showInputDialog(frame, "Enter Car Registration Number (Letter+5 digits):");
        if (regNum == null || !regNum.matches("[A-Z]\\d{5}")) {
            JOptionPane.showMessageDialog(frame, "Invalid registration number format.");
            return; // Stop if input is invalid or canceled
        }
    
        // Check that this car isn't already parked somewhere else
        if (carPark.findCar(regNum) != null) {
            JOptionPane.showMessageDialog(frame, "This car is already parked in another slot.");
            return;
        }
    
        // Prompt user for owner's name
        String owner = JOptionPane.showInputDialog(frame, "Enter Owner name:");
        if (owner == null) return; // Stop if input is canceled
    
        // Determine the slot type (staff/visitor) for proper car creation
        String ownerType = slot.getType();
    
        // Create a new Car object; park time is automatically recorded in Car's constructor
        Car car = new Car(regNum, owner, ownerType.equals("staff"));
    
        // Park the car in the selected slot
        slot.parkCar(car);
    
        // Update status label to reflect the successful parking action
        statusLabel.setText("Car " + regNum + " parked in " + slot.getSlotId());
    
        // Refresh the slot panels so the change appears visually
        refreshSlots();
    
        // Prepare formatted time for dialog
        String parkTimeStr = car.getFormattedParkTime();
    
        // Show a confirmation dialog with slot and parking time information
        JOptionPane.showMessageDialog(frame,
            "Car " + regNum + " parked at slot " + slot.getSlotId() + ".\n" +
            "Parked at: " + parkTimeStr + "\n"
        );
    
        // Update the status label again for user feedback
        statusLabel.setText("Car " + regNum + " parked in " + slot.getSlotId());
    }

    
    /**
     * Returns a string representing the duration between the given park time
     * and the current time, formatted as hours, minutes, and seconds.
     *
     * @param parkTime The time the car was parked (LocalDateTime)
     * @return A formatted string, e.g. "0 hours 25 minutes 20 seconds"
     */
    private String getDuration(LocalDateTime parkTime) {
        if (parkTime == null) return "";
        // Calculate the duration since the car was parked
        Duration duration = Duration.between(parkTime, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        // Format the duration as a readable string
        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
        }
    
    /**
     * Capitalizes the first letter of the input string and makes all other letters lowercase.
     *
     * @param str The input string to capitalize
     * @return The capitalized string, or the original if null or empty
     */
    private static String capitalize(String str) {
        // If the input is null or empty, return it as is
        if (str == null || str.isEmpty()) return str;
        // Capitalize first character, make the rest lowercase
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Pops up dialogs for the user to input a new slot's ID and type (staff/visitor).
     * Adds the slot to the car park and refreshes the visual slot display.
     * Ensures proper input validation and error reporting in GUI.
     */
    private void addSlotDialog() {
        String slotId = JOptionPane.showInputDialog(frame, "Enter Slot ID (Letter+3 digits):");
        if (slotId == null || !slotId.matches("[A-Z]\\d{3}")) {
            JOptionPane.showMessageDialog(frame, "Invalid Slot ID format.");
            return;
        }
        String[] options = {"staff", "visitor"};
        String type = (String) JOptionPane.showInputDialog(frame, "Select Slot Type:", "Slot Type", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (type == null) return;
        if (carPark.addSlot(new ParkingSlot(slotId, type))) {
            statusLabel.setText("Slot " + slotId + " added.");
            refreshSlots();
        } else {
            JOptionPane.showMessageDialog(frame, "Slot ID already exists.");
        }
    }
    
    /**
     * Lets the user park a car into a slot, ensuring:
     * - Input is via dialogs, validated format
     * - Car is not already parked in another slot
     * - Slot is available and types match (staff/visitor)
     * - Park time is recorded
     * All output and errors shown in GUI dialogs/labels.
     */
    private void parkCarDialog() {
        String slotId = JOptionPane.showInputDialog(frame, "Enter Slot ID to park car:");
        if (slotId == null) return;
        ParkingSlot slot = carPark.getSlot(slotId);
        if (slot == null) {
            JOptionPane.showMessageDialog(frame, "Slot not found.");
            return;
        }
        if (slot.isOccupied()) {
            JOptionPane.showMessageDialog(frame, "Slot is already occupied.");
            return;
        }
        String regNum = JOptionPane.showInputDialog(frame, "Enter Car Registration Number (Letter+5 digits):");
        if (regNum == null || !regNum.matches("[A-Z]\\d{5}")) {
            JOptionPane.showMessageDialog(frame, "Invalid registration number format.");
            return;
        }
        // Check if car is already parked at any spot
        if (carPark.findCar(regNum) != null) {
            JOptionPane.showMessageDialog(frame, "This car is already parked in another slot.");
            return;
        }
        String owner = JOptionPane.showInputDialog(frame, "Enter Owner name:");
        if (owner == null) return;
        String[] ownerTypes = {"staff", "visitor"};
        String ownerType = (String) JOptionPane.showInputDialog(frame, "Select Owner Type:", "Owner Type", JOptionPane.PLAIN_MESSAGE, null, ownerTypes, ownerTypes[0]);
        if (ownerType == null) return;
    
        if (!slot.getType().equals(ownerType)) {
            JOptionPane.showMessageDialog(frame, "Car type mismatch with slot type.");
            return;
        }
    
        Car car = new Car(regNum, owner, ownerType.equals("staff"));
        slot.parkCar(car);
        refreshSlots();
        String parkTimeStr = car.getFormattedParkTime();
        JOptionPane.showMessageDialog(frame,
            "Car " + regNum + " parked at slot " + slotId + ".\n" +
            "Parked at: " + parkTimeStr + "\n"
        );
        statusLabel.setText("Car " + regNum + " parked in " + slotId);
    }

    /**
     * Allows the user to delete a parking slot (if it is not currently occupied).
     * Handles user input and all feedback through GUI dialogs and the status label.
     */
    private void deleteSlotDialog() {
        String slotId = JOptionPane.showInputDialog(frame, "Enter Slot ID to delete:");
        if (slotId == null) return;
        ParkingSlot slot = carPark.getSlot(slotId);
        if (slot == null) {
            JOptionPane.showMessageDialog(frame, "Slot not found.");
            return;
        }
        if (slot.isOccupied()) {
            JOptionPane.showMessageDialog(frame, "Cannot delete occupied slot.");
            return;
        }
        if (carPark.removeSlot(slotId)) {
            statusLabel.setText("Slot " + slotId + " deleted.");
            refreshSlots();
        } else {
            JOptionPane.showMessageDialog(frame, "Error deleting slot.");
        }
    }

    /**
     * Lets the user search for a car by registration number.
     * Displays slot, owner, and time/duration in a GUI dialog if found.
     */
    private void findCarDialog() {
        String regNum = JOptionPane.showInputDialog(frame, "Enter Car Registration Number:");
        if (regNum == null) return;
        ParkingSlot slot = carPark.findCar(regNum);
        if (slot != null) {
            Car car = slot.getParkedCar();
            JOptionPane.showMessageDialog(frame, "Car " + regNum + " is parked at Slot " + slot.getSlotId() +
                "\nOwner: " + car.getOwner() +
                "\nParked at: " + car.getFormattedParkTime() +
                "\nDuration: " + getDuration(car.getParkTime()));
        } else {
            JOptionPane.showMessageDialog(frame, "Car not found.");
        }
    }

    /**
     * Lets the user remove a car by registration number from any slot.
     */
    private void removeCarDialog() {
        String regNum = JOptionPane.showInputDialog(frame, "Enter Car Registration Number to remove:");
        if (regNum == null) return;
        ParkingSlot slot = carPark.findCar(regNum);
        if (slot != null) {
            slot.removeCar();
            statusLabel.setText("Car " + regNum + " removed from Slot " + slot.getSlotId());
            refreshSlots();
        } else {
            JOptionPane.showMessageDialog(frame, "Car not found.");
        }
    }

    /**
     * Displays a table with all parking slot info (slot ID, type, occupancy, car reg, owner, parked at, duration).
     * Uses JTable in a scrollable GUI dialog for clarity and to meet requirements.
     */
    private void listSlotsDialog() {
        java.util.List<ParkingSlot> slots = new ArrayList<>(carPark.getAllSlots());
        String[] columnNames = {"Slot ID", "Slot Type", "Occupied", "Car Registration", "Owner", "Parked At", "Duration"};
        String[][] data = new String[slots.size()][7];
    
        for (int i = 0; i < slots.size(); i++) {
            ParkingSlot slot = slots.get(i);
            data[i][0] = slot.getSlotId();
            data[i][1] = capitalize(slot.getType());
            if (slot.isOccupied()) {
                data[i][2] = "Yes";
                data[i][3] = slot.getParkedCar().getRegistrationNumber();
                data[i][4] = slot.getParkedCar().getOwner();
                data[i][5] = slot.getParkedCar().getFormattedParkTime();
                data[i][6] = getDuration(slot.getParkedCar().getParkTime());
            } else {
                data[i][2] = "No";
                data[i][3] = "";
                data[i][4] = "";
                data[i][5] = "";
                data[i][6] = "";
            }
        }
    
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false); // Read-only
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1100, 450));
    
        JOptionPane.showMessageDialog(frame, scrollPane, "All Parking Slots", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clears the visual display panels for all parking slots.
     * 
     * This method removes all slot buttons from both the visitor and staff panels,
     * and refreshes the panels to update the GUI. Note: This does not delete slots
     * from the backend CarPark model; it only clears the GUI display.
     * 
     * Used for demonstration or to temporarily hide all slots.
     */
    private void clearSlotsDisplay() {
        // Remove all buttons from the visitor slot panel
        visitorSlotPanel.removeAll();
    
        // Remove all buttons from the staff slot panel
        staffSlotPanel.removeAll();
    
        // Refresh the visitor slot panel to update the GUI
        visitorSlotPanel.revalidate();
        visitorSlotPanel.repaint();
    
        // Refresh the staff slot panel to update the GUI
        staffSlotPanel.revalidate();
        staffSlotPanel.repaint();
    
        // Update the status bar to notify the user
        statusLabel.setText("Slots display cleared.");
    }


    /**
     * Launches the main application window.
     */
    public static void main(String[] args) {
        new Application();
    }
}
