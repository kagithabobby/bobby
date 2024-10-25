package com.atm.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bankStatement {
    JFrame frame;
    CardLayout cardLayout;
    String cardNumber;  // Store the card number of the logged-in user

    public bankStatement(JFrame frame, JPanel cp, CardLayout cardLayout, String cardNumber) {
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.cardNumber = cardNumber;

        JPanel tp = new JPanel();

        // Adding labels
        JLabel title = new JLabel("Transaction History", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setBounds(100, 20, 150, 50);
        tp.add(title);

        // Adding JTextArea to display transactions
        JTextArea transactionArea = new JTextArea();
        transactionArea.setBounds(50, 100, 300, 200);
        transactionArea.setEditable(false);
        tp.add(transactionArea);

        // Set layout
        tp.setLayout(null);
        cp.add(tp, "bsp");
        cardLayout.show(cp, "bsp");

        // Revalidate and repaint the center panel (cp) to update the UI
        cp.revalidate();
        cp.repaint();

        // Fetch and display transaction history automatically
        List<String> transactions = fetchTransactionHistory(this.cardNumber);
        if (transactions != null) {
            transactionArea.setText(String.join("\n", transactions));
        } else {
            transactionArea.setText("No transactions found for this account.");
        }
    }

    // Method to fetch transaction history from the database
    private List<String> fetchTransactionHistory(String cardNumber) {
        List<String> transactions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Register the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Step 2: Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "ramu");

            // Step 3: Prepare the SQL query to fetch transaction history
            String sql = "SELECT transaction_Id, transaction_type, transaction_date, amount FROM transactions WHERE cardnumber = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cardNumber);

            // Step 4: Execute the query
            rs = pstmt.executeQuery();

            // Step 5: Process the results
            while (rs.next()) {
                String transactionId = rs.getString("transaction_Id");
                String transactionType = rs.getString("transaction_type");
                Timestamp transactionDate = rs.getTimestamp("transaction_date");
                double amount = rs.getDouble("amount");

                String transactionRecord = String.format("ID: %s | Type: %s | Date: %s | Amount: %.2f",
                        transactionId, transactionType, transactionDate, amount);
                transactions.add(transactionRecord);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Step 6: Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return transactions;
    }
}
