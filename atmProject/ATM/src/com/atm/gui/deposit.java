package com.atm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;

public class deposit{
    private JFrame frame;
    private JPanel cp;
    private CardLayout cardLayout;
    private String cardNumber;  // Store the card number

    // Constructor that takes JFrame, JPanel, CardLayout, and card number
    public deposit(JFrame frame, JPanel cp, CardLayout cardLayout, String cardNumber) {
        this.frame = frame;
        this.cp = cp;
        this.cardLayout = cardLayout;
        this.cardNumber = cardNumber;  // Store the card number

        // Set up the deposit panel
        System.out.println(cardNumber+" this");
        JPanel depositPanel = new JPanel();
        depositPanel.setLayout(null);

        // Deposit form components
        JLabel depositLabel = new JLabel("Enter Amount:");
        depositLabel.setBounds(50, 100, 150, 30);
        JTextField depositAmountField = new JTextField();
        depositAmountField.setBounds(150, 100, 110, 30);
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(130, 150, 85, 30);

        depositPanel.add(depositLabel);
        depositPanel.add(depositAmountField);
        depositPanel.add(depositButton);

        // Add the deposit panel to the center panel (cp) with CardLayout
        cp.add(depositPanel, "depositPanel");
        cardLayout.show(cp, "depositPanel");

        // Revalidate and repaint the center panel
        cp.revalidate();
        cp.repaint();

        // Action listener for the Deposit button
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountText = depositAmountField.getText().trim();
                
                if (amountText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter an amount!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(amountText);
                    depositAmount(amount);  // Use card number directly
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid amount!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Method to handle deposit logic
    private void depositAmount( double amount) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "ramu")) {
            // Update the amount in the database
            String updateQuery = "UPDATE customers SET amount = amount + ? WHERE cardnumber = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, amount);
                updateStmt.setString(2, cardNumber);
                int rowsAffected = updateStmt.executeUpdate();

                


                if (rowsAffected > 0) {
                    // String selectQuery="select  amount from customers where cardnumber = ?";
                    // PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
                    // ResultSet selectrs=selectStmt.executeQuery();
                    // int amountnew=selectrs.getInt("amount");
                    int transactionid = (int)Math.random();
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    Timestamp timestamp = Timestamp.valueOf(currentDateTime);

                    String setQuery = "insert into transactions values(?,?,?,?,?)";
                PreparedStatement setStmt = connection.prepareStatement(setQuery);
                setStmt.setInt(1,transactionid);
                setStmt.setString(2, cardNumber);
                setStmt.setString(3, "deposit");
                setStmt.setTimestamp(4, timestamp);
                setStmt.setDouble(5, amount);
                setStmt.executeQuery();


                    JOptionPane.showMessageDialog(frame,
                            "Deposit successful! Amount deposited: " + amount,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

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
