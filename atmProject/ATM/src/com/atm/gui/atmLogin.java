package com.atm.gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.atm.gui.ATM;
import javax.swing.border.LineBorder;
import java.sql.*;
class atmLogin{
    JFrame frame;
    CardLayout cardLayout;
   static String cardno;

    public atmLogin(JFrame frame,JPanel cp,CardLayout cardLayout)
    {
        this.frame=frame;
        this.cardLayout=cardLayout;

        JPanel loginPanel =  new JPanel();



         JLabel centerLabel = new JLabel("Welcome to ATM", SwingConstants.CENTER);
         centerLabel.setFont(new Font("Arial", Font.BOLD, 14));
         centerLabel.setBounds(100,20,150,30);
        centerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        loginPanel.add(centerLabel);

        loginPanel.setLayout(null);
        loginPanel.setPreferredSize(new Dimension(200,100));
        JLabel cardNumLabel = new JLabel("CardNumber:  " );
        cardNumLabel.setBounds(50,50,100,40);
        JLabel pinLabel = new JLabel("Pin: ");
        pinLabel.setBounds(50,100,100,40);

        // adding textfields

        JTextField  cardNumField = new JTextField();
        cardNumField.setBounds(150,60,100,30);
        JPasswordField  pin = new JPasswordField();
        pin.setBounds(150,110,100,30);
         
        //adding  button to panel
        JButton login = new JButton("Login");
        login.setBounds(150,160,80,30);
        login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = cardNumField.getText();
                String pass = new String(pin.getPassword());
                try{
                    Connection connect = DatabaseManager.getConnection();
                     // Add your database operations here
                     // Prepare SQL query
                     String sql = "SELECT * FROM customer WHERE Name =? AND AccountNumber=?";
                     PreparedStatement ps = connect.prepareStatement(sql);
                     ps.setString(1, user);
                     ps.setString(2, pass);
                    
                     // Execute query
                     ResultSet rs = ps.executeQuery();
 
                     // Check if login is successful
                     if (rs.next()) {
                        cardno=rs.getString("cardnumber");

                        new atmLogin(frame, loginPanel, cardLayout);
                         // Open DepositPanel if login is successful
                        //  JOptionPane.showMessageDialog(frame, "Login successful!");
                        
                     //  loginPanel.setLayout(null);
                      frame.add(cp);
                    cp.setVisible(true);
                    //    frame.dispose();
                    new ATM();
 
                     } else {
                         // Show error message if login fails
                         JOptionPane.showMessageDialog(frame, "Invalid username or password.");
                     }
 
                     // Close resources
                     rs.close();
                     ps.close();
                     connect.close(); // Close the connection after use
                } catch(SQLException ex) {
                    // Handle the SQL exception
                    System.out.println("Error connecting to database: " + ex.getMessage());
                } catch(Exception ex) {
                    // Handle other exceptions
                    System.out.println("An error occurred: " + ex.getMessage());
                }
            }
        });
       


        loginPanel.add(cardNumField);
        loginPanel.add(pin);
        loginPanel.add(pinLabel);
        loginPanel.add(cardNumLabel);
        loginPanel.add(login); 

        // cp.add(loginPanel,"login");
        // cp.setVisible(true);
        JPanel mainPanel = new JPanel();
        mainPanel.add(loginPanel);
        
        mainPanel.setLayout(cardLayout);
        //mainPanel.setVisible(true);
        cp.add(mainPanel, "mainPanel");

       
        // cp.add(loginPanel,"bsp");
        cardLayout.show(cp, "bsp");
        frame.add(cp, BorderLayout.CENTER);

        // Revalidate and repaint the center panel (cp) to update the UI
               cp.revalidate();
               cp.repaint();
        

    }
    static String getCardno(){
        return cardno;
    }
}