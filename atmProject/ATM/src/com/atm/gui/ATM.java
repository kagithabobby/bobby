package com.atm.gui;
import com.atm.*;
import com.atm.DatabaseManager;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import com.atm.gui.ChangePinClass;

import java.sql.*;

public class ATM {
    public static String cardNumber;
    private CardLayout cardLayout;
    JPanel changePinPanel;
    public JPanel cp;
    private JButton deposit;
    private JButton withdraw;
    private JButton balance;
    private JButton balanceStatementBt;
    private JButton changePinButton;
    private JButton transactionButton;

    

    public ATM() {
        cardLayout  = new CardLayout();

        // Create the main frame
        JFrame frame = new JFrame("ATM System");
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(10, 10));  // Added gap between components
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);  // Center the frame
        System.out.println("this atm cardno"+cardNumber);

        // North panel for title
        JPanel np = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("ATM System",SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        np.setPreferredSize(new Dimension(200, 50));
        np.add(title);
        frame.add(np, BorderLayout.NORTH);

        // Left panel for buttons

        JPanel lp = new JPanel(null);  // Set layout to null to use setBounds
        lp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lp.setPreferredSize(new Dimension(200, 150));
        withdraw = new JButton("Withdraw");
        withdraw.setBounds(20, 100, 150, 40);  
        withdraw.setEnabled(false);
        withdraw.setBackground(new Color(32, 178, 170)); // Set bounds for the button
        deposit = new JButton("Deposit");
        deposit.setBounds(20, 150, 150, 40);
        deposit.setEnabled(false);
        deposit.setBackground(new Color(32, 178, 170)); 
        balance = new JButton("Check Balance");
        balance.setBounds(20, 200, 150, 40);
        balance.setEnabled(false);
        balance.setBackground(new Color(32, 178, 170)); 

        lp.add(withdraw);
        lp.add(deposit);
        lp.add(balance);

        // Action listeners for left panel buttons
        withdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String cardNumber = "2300039038";
                new withdraw(frame,cp,cardLayout,cardNumber);
            }
        });

        deposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String cardNumber = "2300039038";
                new deposit(frame,cp,cardLayout,cardNumber);
            }
        });

        balance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new checkbalance(frame,cp,cardLayout,cardNumber);
            }
        });

        frame.add(lp, BorderLayout.WEST);

        // Center panel

        cp = new JPanel();

        // JPanel changePinPanel = new changePin().getPanel();
        // cp.add(changePinPanel,"changePin");

        cp.setLayout(cardLayout);
        cp.setBorder(LineBorder.createBlackLineBorder());
        cp.setPreferredSize(new Dimension(200, 100));
        
       
        //creating main Panel
      



        //  JLabel centerLabel = new JLabel("Welcome to ATM", SwingConstants.CENTER);
        //  centerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        //  centerLabel.setBounds(100,20,150,30);

        //  cp.add(centerLabel);

        //creation of welcome panel
        JPanel welcomepanel=new  JPanel();
        welcomepanel.setLayout(new BorderLayout());
        JLabel welcome=new JLabel("Welcome to ATM",SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 14));
        welcomepanel.add(welcome,BorderLayout.CENTER);
        


    
        // centerLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        JPanel loginPanel =  new JPanel();
        //loginPanel.add();

        loginPanel.setLayout(null);
         loginPanel.setPreferredSize(new Dimension(200,100));
        JLabel cardNumLabel = new JLabel("CardNumber:  " );
        cardNumLabel.setBounds(50,50,100,40);
        JLabel pinLabel = new JLabel("Pin: ");
        pinLabel.setBounds(50,100,100,40);

        // // adding textfields

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
                     String sql = "SELECT * FROM customers WHERE cardNumber =? AND pin=?";
                     PreparedStatement ps = connect.prepareStatement(sql);
                     ps .setString(1, user);
                     ps.setString(2, pass);
                    
                     // Execute query
                     ResultSet rs = ps.executeQuery();
 
                     // Check if login is successful
                     if (rs.next()) {
                        cardNumber=rs.getString("cardnumber");


                         //new atmLogin(frame, loginPanel, cardLayout);
                        // Open DepositPanel if login is successful
                         JOptionPane.showMessageDialog(frame, "Login successful!");
                         cp.add(welcomepanel,"welcome");
                          cardLayout.show(cp,"welcome"); 
                           
                        //   public void  enableButtons(boolean enable) {
                            deposit.setEnabled(true);
                            withdraw.setEnabled(true);
                            balance.setEnabled(true);
                            balanceStatementBt.setEnabled(true);
                            changePinButton.setEnabled(true);
                            transactionButton.setEnabled(true);
                        // }                     
                    //   loginPanel.setLayout(null);
                    //    frame.add(cp);
                    // cp.setVisible(true);
                    //    frame.dispose();
                    //    new ATM();
 
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

        cp.add(loginPanel,"login");
        cp.setVisible(true);
        
        
        frame.add(cp, BorderLayout.CENTER);
      

         // cp.add(changePinPanel);
        frame.add(cp, BorderLayout.CENTER);


        // Right panel for transaction and pin buttons
        JPanel rp = new JPanel(null); 
        rp.setLayout(null); // Set layout to null to use setBounds
        // rp.setLayout(new BorderLayout());
        // rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
        rp.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rp.setPreferredSize(new Dimension(200, 150));
        //buttons 

        transactionButton = new JButton("Bank Statement");
        transactionButton.setBounds(20, 100, 150, 40);
        transactionButton.setEnabled(false);
        changePinButton = new JButton("Change Pin");
        changePinButton.setBounds(20, 150, 150, 40);
        changePinButton.setEnabled(false);
        balanceStatementBt = new JButton("Exit");
        balanceStatementBt.setBounds(20, 200, 150, 40);
        balanceStatementBt.setEnabled(false);
       // setting background colors to buttons

       transactionButton.setBackground(   new Color(32, 178, 170)  );
       changePinButton.setBackground(new Color(32, 178, 170)   );
       balanceStatementBt.setBackground( new Color(32, 178, 170)   );
        // balance.setBackground(new Color(32, 178, 170));
        // balance.setBackground(new Color(32, 178, 170));
        // balance.setBackground(new Color(32, 178, 170));

        rp.add(transactionButton);
        rp.add(changePinButton);
        rp.add(balanceStatementBt);

        // Action listeners for right panel buttons
        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              new bankStatement(frame, cp, cardLayout,cardNumber);
            }
        });

        changePinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String cardNumber = "2300039038";
                
                new ChangePinClass(frame, cp, cardLayout, cardNumber);
                    
                
                    }
            });
        balanceStatementBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new exit(frame,cp,cardLayout);
            }
        });

        frame.add(rp, BorderLayout.EAST);

        // South panel for footer
        JPanel sp = new JPanel(null);  // Set layout to null to use setBounds
        sp.setPreferredSize(new Dimension(200, 50));

        // JButton button1 = new JButton("1");
        // button1.setBounds(270, 10, 50, 50);
        // JButton button2 = new JButton("2");
        // button2.setBounds(330, 10, 50, 50);
        // JButton button3 = new JButton("3");
        // button3.setBounds(390, 10, 50, 50);
        // JButton button4 = new JButton("4");
        // button4.setBounds(450, 10, 50, 50);
        // JButton button5 = new JButton("5");
        // button5.setBounds(270, 70, 50, 50);
        // JButton button6 = new JButton("6");
        // button6.setBounds(330, 70, 50, 50);
        // JButton button7 = new JButton("7");
        // button7.setBounds(390, 70, 50, 50);
        // JButton button8 = new JButton("8");
        // button8.setBounds(450, 70, 50, 50);
        // JButton button9 = new JButton("9");
        // button9.setBounds(270, 130, 50, 50);
        // JButton button0 = new JButton("0");
        // button0.setBounds(330, 130, 50, 50);
        // JButton buttonE = new JButton("E");
        // buttonE.setBounds(390,  130, 50, 50);
        // JButton buttonClear = new JButton("Clear");
        // buttonClear.setBounds(450, 130, 50, 50);

        // sp.add(button1);
        // sp.add(button2);
        // sp.add(button3);
        // sp.add(button4);
        // sp.add(button5);
        // sp.add(button6);
        // sp.add(button7);
        // sp.add(button8);
        // sp.add(button9);
        // sp.add(button0);
        // sp.add(buttonE);
        // sp.add(buttonClear);
        

        frame.add(sp,BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
    }
   

    protected static String getCardNumber() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCardNumber'");
    }


    public static void main(String[] args) {
        new ATM();
    }
}