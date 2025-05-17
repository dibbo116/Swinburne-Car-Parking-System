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
 * @version 1.0 - May 18, 2025
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


    private void initGUI() {
    /**
     * Initializes all GUI components, sets up the layout, panels, buttons, and event handlers.
     * Ensures color-coding, proper font, and clear arrangement for all controls and displays.
     */

        frame = new JFrame("Swinburne Car Park System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 730);
        frame.setLayout(new BorderLayout());

        // === Title and Image Panel (Top) ===
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(235, 235, 235));

        JLabel titleLabel = new JLabel("SWINBURNE CAR PARK SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 0, 8, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/car.png"));
        
        Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        
        JLabel carLabel = new JLabel(scaledIcon, SwingConstants.CENTER);
        
        // Add to your panel
        titlePanel.add(carLabel, BorderLayout.CENTER);
        
        frame.add(titlePanel, BorderLayout.NORTH);

        // === Slots Grid Area (Center) ===
        JPanel slotsArea = new JPanel();
        slotsArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        visitorSlotPanel = new JPanel(new GridLayout(1, 5, 16, 0));
        visitorSlotPanel.setOpaque(false);
        visitorSlotPanel.setPreferredSize(new Dimension(830, 90));
        slotsArea.add(visitorSlotPanel, gbc);

        gbc.gridy++;
        staffSlotPanel = new JPanel(new GridLayout(1, 5, 16, 0));
        staffSlotPanel.setOpaque(false);
        staffSlotPanel.setPreferredSize(new Dimension(670, 90));
        slotsArea.add(staffSlotPanel, gbc);

        frame.add(slotsArea, BorderLayout.CENTER);

        // === Control Buttons Panel (Bottom) ===
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 4, 0));
        controlPanel.setPreferredSize(new Dimension(830, 130));
        JButton showAllBtn = new JButton("Show All Parking Spots");
        JButton findCarBtn = new JButton("Find Car");
        JButton parkCarBtn = new JButton("Park Car");
        JButton deleteSlotBtn = new JButton("Delete Spot");
        JButton removeCarBtn = new JButton("Remove Car");
        JButton addSlotBtn = new JButton("Add Parking Spot");
        JButton exitBtn = new JButton("Exit Application");
        JButton clearScreenBtn = new JButton("Clear Screen");

        showAllBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        findCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        parkCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteSlotBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        removeCarBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        addSlotBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        exitBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        clearScreenBtn.setFont(new Font("Arial", Font.PLAIN, 18));

        controlPanel.add(showAllBtn);
        controlPanel.add(findCarBtn);
        controlPanel.add(parkCarBtn);
        controlPanel.add(deleteSlotBtn);
        controlPanel.add(removeCarBtn);
        controlPanel.add(addSlotBtn);
        controlPanel.add(exitBtn);
        controlPanel.add(clearScreenBtn);

        showAllBtn.addActionListener(e -> listSlotsDialog());
        findCarBtn.addActionListener(e -> findCarDialog());
        parkCarBtn.addActionListener(e -> parkCarDialog());
        deleteSlotBtn.addActionListener(e -> deleteSlotDialog());
        removeCarBtn.addActionListener(e -> removeCarDialog());
        addSlotBtn.addActionListener(e -> addSlotDialog());
        exitBtn.addActionListener(e -> System.exit(0));
        clearScreenBtn.addActionListener(e -> clearSlotsDisplay());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, BorderLayout.CENTER);
        statusLabel = new JLabel("Welcome to Swinburne Car Park System", SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void refreshSlots() {
    /**
     * Updates and redraws the visual display of all parking slots.
     * Each slot is shown as a button, colored by type and occupancy.
     * Keeps slot and button logic in sync, and keeps the interface clear and up-to-date.
     */
    
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
    
    // Helper for left-click park via slot
    private void parkCarDirectly(ParkingSlot slot) {
        String regNum = JOptionPane.showInputDialog(frame, "Enter Car Registration Number (Letter+5 digits):");
        if (regNum == null || !regNum.matches("[A-Z]\\d{5}")) {
            JOptionPane.showMessageDialog(frame, "Invalid registration number format.");
            return;
        }
        if (carPark.findCar(regNum) != null) {
            JOptionPane.showMessageDialog(frame, "This car is already parked in another slot.");
            return;
        }
        String owner = JOptionPane.showInputDialog(frame, "Enter Owner name:");
        if (owner == null) return;
        String ownerType = slot.getType();
        Car car = new Car(regNum, owner, ownerType.equals("staff"));
        slot.parkCar(car);
        statusLabel.setText("Car " + regNum + " parked in " + slot.getSlotId());
        refreshSlots();
        String parkTimeStr = car.getFormattedParkTime();
        JOptionPane.showMessageDialog(frame,
            "Car " + regNum + " parked at slot " + slot.getSlotId() + ".\n" +
            "Parked at: " + parkTimeStr + "\n"
        );
        statusLabel.setText("Car " + regNum + " parked in " + slot.getSlotId());
    }

    
    private String getDuration(LocalDateTime parkTime) {
        if (parkTime == null) return "";
        Duration duration = Duration.between(parkTime, LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return hours + " hours " + minutes + " minutes " + seconds + " seconds";
        }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    private void addSlotDialog() {
    /**
     * Pops up dialogs for the user to input a new slot's ID and type (staff/visitor).
     * Adds the slot to the car park and refreshes the visual slot display.
     * Ensures proper input validation and error reporting in GUI.
     */

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

    private void parkCarDialog() {
    /**
     * Lets the user park a car into a slot, ensuring:
     * - Input is via dialogs, validated format
     * - Car is not already parked in another slot
     * - Slot is available and types match (staff/visitor)
     * - Park time is recorded
     * All output and errors shown in GUI dialogs/labels.
     */

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
        // CHECK IF CAR IS ALREADY PARKED IN ANY SLOT
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


    private void deleteSlotDialog() {
    /**
     * Allows the user to delete a parking slot (if it is not currently occupied).
     * Handles user input and all feedback through GUI dialogs and the status label.
     */

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

    private void findCarDialog() {
    /**
     * Lets the user search for a car by registration number.
     * Displays slot, owner, and time/duration in a GUI dialog if found.
     * All interaction is via GUI.
     */

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

    private void removeCarDialog() {
    /**
     * Lets the user remove a car by registration number from any slot.
     */

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

    private void listSlotsDialog() {
    /**
     * Displays a table with all parking slot info (slot ID, type, occupancy, car reg/owner).
     * Uses JTable in a scrollable GUI dialog for clarity and to meet requirements.
     */
    
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



    private void clearSlotsDisplay() {
    /**
     * Clears the visual display panels for slots. (Used for demonstration, not deletion.)
     */
        visitorSlotPanel.removeAll();
        staffSlotPanel.removeAll();
        visitorSlotPanel.revalidate();
        visitorSlotPanel.repaint();
        staffSlotPanel.revalidate();
        staffSlotPanel.repaint();
        statusLabel.setText("Slots display cleared.");
    }

    public static void main(String[] args) {
    /**
     * Launches the main application window.
     */

        new Application();
    }
}
