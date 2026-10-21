package com.library.ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {

        setTitle("Library Management System - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel(
                "<html><center>📚 Library Management System</center></html>"
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        mainPanel.add(titleLabel, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;

        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);

        gbc.gridx = 1;
        gbc.gridy = 1;

        mainPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");

        gbc.gridx = 0;
        gbc.gridy = 2;

        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);

        gbc.gridx = 1;
        gbc.gridy = 2;

        mainPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("LOGIN");

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        mainPanel.add(loginButton, gbc);

        // Temporary frontend login
        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Temporary login for frontend testing
            if (username.equals("admin") && password.equals("admin123")) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful!"
                );

                dispose();

                new MainFrame().setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password!",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        add(mainPanel);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}