package com.atm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class withdraw {
    private JPanel cp;
    private JFrame frame;
    private CardLayout cardLayout;
    private String cardNumber;  // Store the card number

    // Constructor that initializes the withdrawal panel
    public withdraw(JFrame frame, JPanel cp, CardLayout cardLayout, String cardNumber) {
        this.frame = frame;
        this.cp = cp;
        this.cardLayout = cardLayout;
        this.cardNumber = cardNumber;  // Store the card number

        JPanel wp = new JPanel();
        // Labels
        JLabel title = new JLabel("Withdraw Your Amount", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 12));
        title.setBounds(100, 20, 200, 30);
        
        JLabel amountLabel = new JLabel("Enter Amount:");
        amountLabel.setBounds(50, 70, 150, 30);
        
        JTextField amountField = new JTextField();
        amountField.setBounds(200, 70, 150, 30);

        // Withdraw button
        JButton withButton = new JButton("Withdraw");
        withButton.setBounds(150, 120, 100, 30);

        // Adding components to the panel
        wp.add(title);
        wp.add(amountLabel);
        wp.add(amountField);
        wp.add(withButton);

        wp.setLayout(null);
        cp.add(wp, "wp");
        cardLayout.show(cp, "wp");

        // Revalidate and repaint the center panel (cp) to update the UI
        cp.revalidate();
        cp.repaint();

        // Action listener for the Withdraw button
        withButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountText = amountField.getText().trim();

                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter an amount!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    withdrawAmount(cardNumber, amount);  // Use card number directly
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid amount!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to handle withdrawal logic
    private void withdrawAmount(String cardNumber, double amount) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "ramu")) {
            // Check if the account exists and has sufficient balance
            String checkQuery = "SELECT amount FROM customers WHERE cardnumber = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, cardNumber);
                ResultSet rs = checkStmt.executeQuery();

                /////////////////////////////////////////////
                




                ////////////////////////////////////////////

                if (rs.next()) {
                    double currentBalance = rs.getDouble("amount");  // Get the balance

                    // Check if the withdrawal amount is less than or equal to the current balance
                    if (amount <= currentBalance) {
                        // Perform withdrawal
                        double newBalance = currentBalance - amount;
                        String updateQuery = "UPDATE customers SET amount = ? WHERE cardnumber = ?";
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                            updateStmt.setDouble(1, newBalance);
                            updateStmt.setString(2, cardNumber);
                            updateStmt.executeUpdate();
                            
                            JOptionPane.showMessageDialog(frame,
                                    "Withdrawal successful! New balance: " + newBalance,
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Insufficient balance!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Account not found!",
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
}
