package com.atm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChangePinClass {
    private JFrame frame;
    private JPanel cp;
    private CardLayout cardLayout;
    private String cardNumber=atmLogin.getCardno();  // Store card number here

    JPanel changePinPanel = new JPanel();

    // Constructor that takes JFrame as a parameter and card number from login
    public ChangePinClass(JFrame frame, JPanel cp, CardLayout cardLayout, String cardNumber) {
        this.frame = frame;
        this.cp = cp;
        this.cardLayout = cardLayout;
        this.cardNumber = cardNumber;  // Card number comes from login class

        // Creating labels
        JLabel oldPinLabel = new JLabel("Enter Old Pin:");
        JLabel newPinLabel = new JLabel("Enter New Pin:");
        JLabel confirmPinLabel = new JLabel("Confirm Pin:");

        // Password fields
        JPasswordField oldPinField = new JPasswordField();
        JPasswordField newPinField = new JPasswordField();
        JPasswordField confirmPinField = new JPasswordField();

        // Set bounds for labels and fields (absolute positioning)
        oldPinLabel.setBounds(50, 50, 100, 50);
        newPinLabel.setBounds(50, 100, 100, 50);
        confirmPinLabel.setBounds(50, 150, 100, 50);

        oldPinField.setBounds(150, 60, 100, 30);
        newPinField.setBounds(150, 110, 100, 30);
        confirmPinField.setBounds(150, 160, 100, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(130, 220, 85, 30);

        // Adding components to changePin panel
        changePinPanel.add(oldPinLabel);
        changePinPanel.add(newPinLabel);
        changePinPanel.add(confirmPinLabel);
        changePinPanel.add(oldPinField);
        changePinPanel.add(newPinField);
        changePinPanel.add(confirmPinField);
        changePinPanel.add(submitButton);

        // Use setBounds with null layout
        changePinPanel.setLayout(null);

        cp.add(changePinPanel, "changePinPanel");
        cardLayout.show(cp, "changePinPanel");

        // Revalidate and repaint the center panel (cp) to update the UI
        cp.revalidate();
        cp.repaint();

        // Add ActionListener for the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPinText = new String(oldPinField.getPassword());
                String newPinText = new String(newPinField.getPassword());
                String confirmPinText = new String(confirmPinField.getPassword());

                // Validate that the new PIN and confirm PIN match
                if (!newPinText.equals(confirmPinText)) {
                    JOptionPane.showMessageDialog(frame,
                            "New Pin and Confirm Pin do not match!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    newPinField.setText("");  // Clear fields
                    confirmPinField.setText("");
                    return;
                }

                // Validate old PIN with the database
                try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "ramu")) {
                    String query = "SELECT pin FROM customers WHERE cardnumber = ?";
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        stmt.setString(1, cardNumber); // Use the card number passed from login

                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            String currentPin = rs.getString("pin");

                            // Check if old PIN matches the one in the database
                            if (!currentPin.equals(oldPinText)) {
                                JOptionPane.showMessageDialog(frame,
                                        "Old PIN is incorrect!",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                oldPinField.setText("");  // Clear old PIN field
                                return;
                            }

                            // Update the PIN in the database
                            String updateQuery = "UPDATE customers SET pin = ? WHERE cardnumber = ?";
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                                updateStmt.setString(1, newPinText); // Set new PIN
                                updateStmt.setString(2, cardNumber); // Use same card number

                                int rowsAffected = updateStmt.executeUpdate();
                                if (rowsAffected > 0) {
                                    JOptionPane.showMessageDialog(frame,
                                            "PIN changed successfully!",
                                            "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(frame,
                                            "Error changing PIN!",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }

                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Customer not found!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame,
                            "Database error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
