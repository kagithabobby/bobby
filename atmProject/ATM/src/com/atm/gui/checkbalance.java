package com.atm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.LineBorder;

class checkbalance {
    JPanel cp;
    JFrame frame;
    CardLayout cardLayout;
    String cardNumber; // Card number from login details

    public checkbalance(JFrame frame, JPanel cp, CardLayout cardLayout, String cardNumber) {
        this.frame = frame;
        this.cp = cp;
        this.cardLayout = cardLayout;
        this.cardNumber = cardNumber;

        JPanel cbp = new JPanel();
        
        // labels
        JLabel title = new JLabel("Check Balance", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setBounds(100, 10, 150, 50);

        JLabel ammLabel = new JLabel("Your Balance Amount:");
        ammLabel.setBounds(50, 60, 150, 30);

        JLabel balanceAmount = new JLabel(); // Label to display balance
        balanceAmount.setBounds(200, 60, 100, 30);
        cbp.add(balanceAmount);

        // Fetch balance from database
        String amount = fetchBalanceFromDB(cardNumber);
        if (amount != null) {
            balanceAmount.setText(amount);
        } else {
            balanceAmount.setText("Error fetching balance");
        }

        cbp.add(ammLabel);
        cbp.add(title);
        cbp.setLayout(null);

        cp.add(cbp, "cbp");
        cardLayout.show(cp, "cbp");

        // Revalidate and repaint the center panel (cp) to update the UI
        cp.revalidate();
        cp.repaint();
    }

    // Method to fetch balance from database
    private String fetchBalanceFromDB(String cardNumber) {
        String amount = null;
        try {
            // Database connection details
            String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Replace with your database URL
            String username = "SYSTEM"; // Your DB username
            String password = "ramu"; // Your DB password

            Connection conn = DriverManager.getConnection(url, username, password);
            String query = "SELECT amount FROM customers WHERE cardnumber = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cardNumber); // Set the card number parameter

            ResultSet rs = pstmt.executeQuery();

            // Check if a result was returned
            if (rs.next()) {
                amount = rs.getString("amount");
            } else {
                amount = "No balance found";
            }

            // Close resources
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            amount = "Database error";
        }
        return amount;
    }
}
